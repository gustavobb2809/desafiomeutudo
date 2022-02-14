package com.desafio.meutudo.wsbancario.respository;

import com.desafio.meutudo.wsbancario.models.Installments;
import com.desafio.meutudo.wsbancario.models.Transfer;
import com.desafio.meutudo.wsbancario.respository.constants.ConstantsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentRepository extends JpaRepository<Installments, Long> {
    @Query(ConstantsQuery.QUERY_GET_INSTALLMENTS)
    public List<Installments> getInstallments(@Param("transfer") long transfer);

    @Query(ConstantsQuery.QUERY_SCHEDULED_INSTALLMENT)
    public List<Installments> checkPaymentScheduledDate(@Param("scheduled") LocalDate scheduledDate);

    public Integer countByTransfer(Transfer transfer);

    public Integer countByStatus(String status);

    public Optional<Installments> findByTransfer(Transfer transfer);
}
