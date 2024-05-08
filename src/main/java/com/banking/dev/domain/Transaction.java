package com.banking.dev.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "originating_account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account originatingAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resulting_account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account resultingAccount;

    @Column(name = "transaction_reason", nullable = false)
    private String transactionReason;

    public Transaction() {
        // Default constructor with no arguments
    }

    public Transaction(
        Long transactionId,
        Account originatingAccount,
        BigDecimal amount,
        Account resultingAccount,
        String transactionReason
    ) {
        this.transactionId = transactionId;
        this.originatingAccount = originatingAccount;
        this.amount = amount;
        this.resultingAccount = resultingAccount;
        this.transactionReason = transactionReason;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Account getResultingAccount() {
        return resultingAccount;
    }

    public void setResultingAccount(Account resultingAccount) {
        this.resultingAccount = resultingAccount;
    }

    public Account getOriginatingAccount() {
        return originatingAccount;
    }

    public void setOriginatingAccount(Account originatingAccount) {
        this.originatingAccount = originatingAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(String transactionReason) {
        this.transactionReason = transactionReason;
    }

    @Override
    public String toString() {
        return (
            "Transaction{" +
            "transactionId=" +
            transactionId +
            ", amount=" +
            amount +
            ", originatingAccount=" +
            originatingAccount +
            ", resultingAccount=" +
            resultingAccount +
            ", transactionReason='" +
            transactionReason +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return (
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(originatingAccount, that.originatingAccount) &&
            Objects.equals(resultingAccount, that.resultingAccount) &&
            Objects.equals(transactionReason, that.transactionReason)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, amount, originatingAccount, resultingAccount, transactionReason);
    }
}
