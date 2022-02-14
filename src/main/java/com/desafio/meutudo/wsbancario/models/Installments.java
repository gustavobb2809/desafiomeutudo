package com.desafio.meutudo.wsbancario.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "installments")
public class Installments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "status")
    private String status;

    @Column(name = "scheduledDate")
    private LocalDate scheduledDate;

    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
