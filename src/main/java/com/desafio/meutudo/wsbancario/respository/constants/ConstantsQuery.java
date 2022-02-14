package com.desafio.meutudo.wsbancario.respository.constants;

public class ConstantsQuery {
    public static final String QUERY_SEARCH_ACC0UNT = "SELECT a FROM Account AS a WHERE a.account = :acc AND a.agency = :ag";
    public static final String QUERY_SEARCH_TRANSFER_UUID = "SELECT tr FROM Transfer AS tr INNER JOIN Account AS acc ON acc.id = tr.account.id INNER JOIN User AS us ON us.id = acc.user.id WHERE tr.uuid = :uuid AND us.id = :user";
    public static final String QUERY_GET_INSTALLMENTS = "SELECT ins FROM Installments AS ins INNER JOIN Transfer AS tr ON tr.id = ins.transfer.id WHERE ins.status = 'PAGO' AND tr.id = :transfer";
    public static final String QUERY_SCHEDULED_INSTALLMENT = "SELECT inst FROM Installments AS inst WHERE inst.scheduledDate = :scheduled AND inst.status = 'NAO_PAGO'";
}
