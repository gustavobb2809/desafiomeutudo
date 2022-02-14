package com.desafio.meutudo.wsbancario.respository;

import com.desafio.meutudo.wsbancario.models.Installments;
import com.desafio.meutudo.wsbancario.models.Transfer;
import com.desafio.meutudo.wsbancario.respository.constants.ConstantsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepository  extends JpaRepository<Transfer, Long> {
    @Query(ConstantsQuery.QUERY_SEARCH_TRANSFER_UUID)
    public Optional<Transfer> findByUuid(@Param("uuid") String uuid, @Param("user") long user);
}
