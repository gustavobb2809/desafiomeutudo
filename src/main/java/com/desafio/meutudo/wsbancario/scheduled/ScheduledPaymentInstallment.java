package com.desafio.meutudo.wsbancario.scheduled;

import com.desafio.meutudo.wsbancario.dto.constants.InstallmentStatusEnum;
import com.desafio.meutudo.wsbancario.dto.constants.TransfersStatusEnum;
import com.desafio.meutudo.wsbancario.models.Account;
import com.desafio.meutudo.wsbancario.models.Installments;
import com.desafio.meutudo.wsbancario.models.Transfer;
import com.desafio.meutudo.wsbancario.respository.AccountRepository;
import com.desafio.meutudo.wsbancario.respository.InstallmentRepository;
import com.desafio.meutudo.wsbancario.respository.TransferRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

// Scheduled responsável por verificar as parcelas não pagas agendadas e realizar a debitação.
@Component
public class ScheduledPaymentInstallment {
    private Logger logger = LogManager.getLogger(ScheduledPaymentInstallment.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Scheduled(fixedRate = 18000)
    public void checkPaymentInstallment() {
        LocalDate localDate = LocalDate.now();

        logger.info("Check date: {} transfer payments", localDate);

        List<Installments> installmentsList = installmentRepository.checkPaymentScheduledDate(localDate);

        if (installmentsList.size() >= 1) {
            int count = 1;
            for (Installments installments : installmentsList) {
                logger.info("Check transfer: {} installment: {} with value: {}", installments.getTransfer().getUuid(), count, installments.getValue());

                Account accountReferenced = accountRepository.findByAccount(installments.getTransfer().getToAccount());
                Account accountOrigin = installments.getTransfer().getAccount();

                accountReferenced.setBalance(accountReferenced.getBalance().add(installments.getValue()));
                accountRepository.save(accountReferenced);

                accountOrigin.setBalance(accountOrigin.getBalance().subtract(installments.getValue()));
                accountRepository.save(accountOrigin);

                installments.setStatus(InstallmentStatusEnum.PAGO.toString());
                installmentRepository.save(installments);

                int installmentsTotal = installmentRepository.countByTransfer(installments.getTransfer());
                int installmentsPaidOut = installmentRepository.countByStatus(InstallmentStatusEnum.PAGO.toString());

                if(installmentsTotal == installmentsPaidOut) {
                    Transfer transfer = installments.getTransfer();
                    transfer.setStatus(TransfersStatusEnum.FINALIZADO.toString());

                    transferRepository.save(transfer);
                }

                count++;
            }

            logger.info("Successfully scheduled transfer");
        }

        logger.info("There are no scheduled transfer installments to be paid on the date: {}", localDate);
    }
}
