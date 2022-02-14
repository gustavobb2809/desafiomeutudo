package com.desafio.meutudo.wsbancario.controllers;

import com.desafio.meutudo.wsbancario.dto.account.BalanceDTO;
import com.desafio.meutudo.wsbancario.dto.constants.InstallmentStatusEnum;
import com.desafio.meutudo.wsbancario.dto.constants.TransfersStatusEnum;
import com.desafio.meutudo.wsbancario.dto.installments.InstallmentsListDTO;
import com.desafio.meutudo.wsbancario.dto.transfer.TransferCancelDTO;
import com.desafio.meutudo.wsbancario.dto.transfer.TransferDTO;
import com.desafio.meutudo.wsbancario.dto.transfer.TransferListDTO;
import com.desafio.meutudo.wsbancario.dto.transfer.TransferResponseDTO;
import com.desafio.meutudo.wsbancario.models.Account;
import com.desafio.meutudo.wsbancario.models.Installments;
import com.desafio.meutudo.wsbancario.models.Transfer;
import com.desafio.meutudo.wsbancario.models.User;
import com.desafio.meutudo.wsbancario.respository.AccountRepository;
import com.desafio.meutudo.wsbancario.respository.InstallmentRepository;
import com.desafio.meutudo.wsbancario.respository.TransferRepository;
import com.desafio.meutudo.wsbancario.respository.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("transfer")
public class TransferController {
    private Logger logger = LogManager.getLogger(TransferController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    InstallmentRepository installmentRepository;

    @Operation(summary = "Endpoint para realizar uma transfêrencia de dinheiro para outra conta.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Transferência efetuada com sucesso."),
            @ApiResponse(code = 400, message = "Campos obrigatórios não informados ou a conta não possui saldo."),
            @ApiResponse(code = 404, message = "Agência ou conta não existe."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @ExceptionHandler(ConstraintViolationException.class)
    @Transactional
    @PostMapping(value = "do", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TransferResponseDTO> doTransfer(@Valid @RequestBody TransferDTO transferDTO, @ApiIgnore Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Account accountOrigin = accountRepository.findByUser(user);

        logger.info("Request to perform a transfer from account: {} to account: {}", accountOrigin.getAccount(), transferDTO.getAccount());

        Optional<Account> accountOptionalReferenced = accountRepository.findByAccountAndAgency(transferDTO.getAccount(), transferDTO.getAgency());

        if (!accountOptionalReferenced.isPresent()) {
            logger.warn("Agency: {} or account: {} does not exist.", transferDTO.getAgency(), transferDTO.getAccount());

            return new ResponseEntity(new TransferResponseDTO(String.format("Agência: %s ou Conta: %s não existe.", transferDTO.getAgency(), transferDTO.getAccount())), HttpStatus.NOT_FOUND);
        }

        if (accountOrigin.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            logger.warn("Account: {} does not have enough balance", accountOrigin.getAccount());

            return new ResponseEntity(new TransferResponseDTO("Você não possui saldo suficiente."), HttpStatus.BAD_REQUEST);
        }

        int installments = 1;
        if (transferDTO.getInstallments() != null) {
            installments = transferDTO.getInstallments();
        }
        String statusTransfer = installments == 1 ? TransfersStatusEnum.FINALIZADO.toString() : TransfersStatusEnum.ABERTO.toString();

        Transfer transfer = new Transfer();
        transfer.setStatus(statusTransfer);
        transfer.setAccount(accountOrigin);
        transfer.setToAccount(transferDTO.getAccount());
        transfer.setUuid(UUID.randomUUID().toString());
        transfer.setUpdateAt(LocalDateTime.now());
        transfer.setCreatedAt(LocalDateTime.now());

        transferRepository.saveAndFlush(transfer);

        BigDecimal value = transferDTO.getValue().divide(new BigDecimal(installments));
        LocalDate scheduledDate = LocalDate.now();
        boolean transferIsScheduled = false;

        if (transferDTO.getScheduledDate() != null) {
            scheduledDate = transferDTO.getScheduledDate().toLocalDate();
            transferIsScheduled = true;

            logger.info("Generating a scheduled transfer");
        }

        for (int i = 1; i <= installments; i++) {
            logger.info("Generating installment: {} with value: {}", i, value);
            String statusInstallment = "";

            if (i == 1) {
                if (transferIsScheduled) {
                    statusInstallment = InstallmentStatusEnum.NAO_PAGO.toString();
                } else {
                    statusInstallment = InstallmentStatusEnum.PAGO.toString();
                }
            } else {
                statusInstallment = InstallmentStatusEnum.NAO_PAGO.toString();
            }

            if (i > 1) {
                scheduledDate = scheduledDate.plusMonths(i);
            }

            Installments installmentsEntity = new Installments();
            installmentsEntity.setValue(value);
            installmentsEntity.setTransfer(transfer);
            installmentsEntity.setStatus(statusInstallment);
            installmentsEntity.setScheduledDate(scheduledDate);
            installmentsEntity.setCreatedAt(LocalDateTime.now());

            installmentRepository.saveAndFlush(installmentsEntity);
        }

        if (!transferIsScheduled) {
            accountOptionalReferenced.get().setBalance(accountOptionalReferenced.get().getBalance().add(value));
            accountRepository.save(accountOptionalReferenced.get());

            accountOrigin.setBalance(accountOrigin.getBalance().subtract(value));
            accountRepository.save(accountOrigin);
        }

        logger.info("Transfer: {} performed successfully", transfer.getUuid());
        return new ResponseEntity(new TransferResponseDTO(String.format("Transferência: %s efetuada com sucesso!", transfer.getUuid())), HttpStatus.CREATED);
    }

    @Operation(summary = "Endpoint para cancelar uma transferência.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transferência cancelada."),
            @ApiResponse(code = 400, message = "Campo obrigatório não informado ou a transferência já foi cancelada."),
            @ApiResponse(code = 404, message = "Transferência não existe."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @ExceptionHandler({ConstraintViolationException.class})
    @Transactional
    @PutMapping(value = "cancel", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TransferResponseDTO> cancelTransfer(@Valid @RequestBody TransferCancelDTO transferCancelDTO, @ApiIgnore Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        logger.info("Received a request to cancel the transfer: {}", transferCancelDTO.getUuid());

        Optional<Transfer> transfer = transferRepository.findByUuid(transferCancelDTO.getUuid(), user.getId());

        if (transfer.isPresent()) {
            if (!transfer.get().getStatus().equals(TransfersStatusEnum.CANCELADO.toString())) {
                transfer.get().setStatus(TransfersStatusEnum.CANCELADO.toString());
                transfer.get().setUpdateAt(LocalDateTime.now());
                transferRepository.save(transfer.get());

                List<Installments> installmentsList = installmentRepository.getInstallments(transfer.get().getId());

                if (installmentsList.size() >= 1) {
                    BigDecimal value = installmentsList.get(0).getValue().multiply(new BigDecimal(installmentsList.size()));

                    Account accountOrigin = transfer.get().getAccount();
                    accountOrigin.setBalance(accountOrigin.getBalance().add(value));
                    accountRepository.save(accountOrigin);

                    Account accountReferenced = accountRepository.findByAccount(transfer.get().getToAccount());
                    accountReferenced.setBalance(accountReferenced.getBalance().subtract(value));
                    accountRepository.save(accountReferenced);
                }

                logger.info("Transfer: {} canceled successfully", transferCancelDTO.getUuid());
                return new ResponseEntity(new TransferResponseDTO(String.format("Transferência: %s foi cancelada com sucesso.", transferCancelDTO.getUuid())), HttpStatus.OK);
            }

            logger.warn("Transfer: {} has already been canceled", transferCancelDTO.getUuid());
            return new ResponseEntity(new TransferResponseDTO(String.format("Transferência: %s já foi cancelada.", transferCancelDTO.getUuid())), HttpStatus.BAD_REQUEST);
        }

        logger.warn("Transfer: {} does not exists", transferCancelDTO.getUuid());
        return new ResponseEntity(new TransferResponseDTO(String.format("Transferência: %s não existe.", transferCancelDTO.getUuid())), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Endpoint para consulta de transferências.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de transferências."),
            @ApiResponse(code = 404, message = "Lista de transferências não existe."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @GetMapping(value = "getTransfers", produces = "application/json")
    public ResponseEntity<List<TransferListDTO>> getTransfers() {
        logger.info("Request list transfers");

        List<Transfer> transferList = transferRepository.findAll();
        List<TransferListDTO> transferListDTOS = new ArrayList<>();

        if (transferList.size() >= 1) {
            for (Transfer transfer : transferList) {
                TransferListDTO transferListDTO = new TransferListDTO();
                transferListDTO.setAccount(transfer.getAccount().getAccount());
                transferListDTO.setUuid(transfer.getUuid());
                transferListDTO.setStatus(transfer.getStatus());
                transferListDTO.setValue(transfer.getInstallments().get(0).getValue().multiply(new BigDecimal(transfer.getInstallments().size())));
                transferListDTO.setToAccount(transfer.getToAccount());

                transferListDTOS.add(transferListDTO);
            }
            return new ResponseEntity(transferListDTOS, HttpStatus.OK);
        }

        return new ResponseEntity(transferListDTOS, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Endpoint para consulta de parcelas de transferências.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de parcelas transferências."),
            @ApiResponse(code = 404, message = "Lista de parcelas transferências não existe."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @GetMapping(value = "getInstallmentsTransfers", produces = "application/json")
    public ResponseEntity<List<InstallmentsListDTO>> getInstallmentsTransfers() {
        logger.info("Request list installments transfers");

        List<Installments> installmentsList = installmentRepository.findAll();
        List<InstallmentsListDTO> installmentsListDTOS = new ArrayList<>();

        if (installmentsList.size() >= 1) {
            for (Installments installments : installmentsList) {
                InstallmentsListDTO installmentsListDTO = new InstallmentsListDTO();
                installmentsListDTO.setTransfer(installments.getTransfer().getUuid());
                installmentsListDTO.setStatus(installments.getStatus());
                installmentsListDTO.setValue(installments.getValue());
                installmentsListDTO.setScheduledDate(installments.getScheduledDate().toString());
                installmentsListDTO.setAccount(installments.getTransfer().getAccount().getAccount());
                installmentsListDTO.setToAccount(installments.getTransfer().getToAccount());

                installmentsListDTOS.add(installmentsListDTO);
            }

            return new ResponseEntity(installmentsListDTOS, HttpStatus.OK);
        }

        return new ResponseEntity(installmentsListDTOS, HttpStatus.BAD_REQUEST);
    }
}
