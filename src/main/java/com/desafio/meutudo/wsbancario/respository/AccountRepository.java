package com.desafio.meutudo.wsbancario.respository;

import com.desafio.meutudo.wsbancario.models.Account;
import com.desafio.meutudo.wsbancario.models.User;
import com.desafio.meutudo.wsbancario.respository.constants.ConstantsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long> {
    public Account findByAccount(String account);

    public Account findByUser(User user);

    @Query(ConstantsQuery.QUERY_SEARCH_ACC0UNT)
    public Optional<Account> findByAccountAndAgency(@Param("acc") String account, @Param("ag") String agency);
}
