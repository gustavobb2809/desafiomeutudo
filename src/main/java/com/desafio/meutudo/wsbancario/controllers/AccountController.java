package com.desafio.meutudo.wsbancario.controllers;

import com.desafio.meutudo.wsbancario.dto.account.BalanceDTO;
import com.desafio.meutudo.wsbancario.mapper.AccountMapper;
import com.desafio.meutudo.wsbancario.models.Account;
import com.desafio.meutudo.wsbancario.respository.AccountRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("account")
public class AccountController {
    private Logger logger = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Operation(summary = "Endpoint para realizar uma consulta do saldo através do número da conta.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o saldo da conta."),
            @ApiResponse(code = 404, message = "Conta não existe."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @GetMapping(value = "balance/{account}", produces = "application/json")
    public ResponseEntity<BalanceDTO> getBalanceAccount(@PathVariable(name = "account") String account) {
        logger.info("look up account: {} balance", account);
        Optional<Account> accountEntity = Optional.ofNullable(this.accountRepository.findByAccount(account));

        if(accountEntity.isPresent()) {
            return new ResponseEntity(accountMapper.mapperBalance(accountEntity.get()), HttpStatus.OK);
        }

        logger.warn("account: {} does not exist", account);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
