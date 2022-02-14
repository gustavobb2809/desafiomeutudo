package com.desafio.meutudo.wsbancario.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "status")
    private String status;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "toAccount")
    private String toAccount;

    @OneToMany(mappedBy = "transfer")
    private List<Installments> installments;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updateAt")
    private LocalDateTime updateAt;
}
