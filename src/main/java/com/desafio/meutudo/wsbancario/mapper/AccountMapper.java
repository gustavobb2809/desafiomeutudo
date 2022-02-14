package com.desafio.meutudo.wsbancario.mapper;

import com.desafio.meutudo.wsbancario.dto.account.BalanceDTO;
import com.desafio.meutudo.wsbancario.models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public BalanceDTO mapperBalance(Account account) {
        return new BalanceDTO(account.getAccount(), account.getBalance());
    }
}
