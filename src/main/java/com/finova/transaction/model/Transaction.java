package com.finova.transaction.model;

import com.finova.account.model.Account;
import com.finova.transaction.roles.TransactionStatuses;
import com.finova.transaction.roles.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "transactions")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false, unique = true, length = 30)
    private String reference;

    /* * Many transactions belong to one account. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    // Destination account for transfers
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counterparty_account_id")
    private Account counterpartyAccount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private BigDecimal balanceBefore;
    @Column(nullable = false)
    private BigDecimal balanceAfter;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatuses status;
    private String description;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
