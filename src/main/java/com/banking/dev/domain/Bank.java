package com.banking.dev.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bank")
public class Bank implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long bankId;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "total_transaction_fee_amount", nullable = false)
    private BigDecimal totalTransactionFeeAmount;

    @Column(name = "total_transfer_amount", nullable = false)
    private BigDecimal totalTransferAmount;

    @Column(name = "transaction_flat_fee_amount", nullable = false)
    private BigDecimal transactionFlatFeeAmount;

    @Column(name = "transaction_percent_fee_value", nullable = false)
    private BigDecimal transactionPercentFeeValue;

    public Bank() {}

    public Bank(
        Long bankId,
        BigDecimal transactionPercentFeeValue,
        BigDecimal transactionFlatFeeAmount,
        BigDecimal totalTransferAmount,
        String bankName,
        BigDecimal totalTransactionFeeAmount
    ) {
        this.bankId = bankId;
        this.transactionPercentFeeValue = transactionPercentFeeValue;
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.bankName = bankName;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public void setTotalTransactionFeeAmount(BigDecimal totalTransactionFeeAmount) {
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getTransactionFlatFeeAmount() {
        return transactionFlatFeeAmount;
    }

    public void setTransactionFlatFeeAmount(BigDecimal transactionFlatFeeAmount) {
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
    }

    public BigDecimal getTransactionPercentFeeValue() {
        return transactionPercentFeeValue;
    }

    public void setTransactionPercentFeeValue(BigDecimal transactionPercentFeeValue) {
        this.transactionPercentFeeValue = transactionPercentFeeValue;
    }

    @Override
    public String toString() {
        return (
            "Bank{" +
            "bankId=" +
            bankId +
            ", bankName='" +
            bankName +
            '\'' +
            ", totalTransactionFeeAmount=" +
            totalTransactionFeeAmount +
            ", totalTransferAmount=" +
            totalTransferAmount +
            ", transactionFlatFeeAmount=" +
            transactionFlatFeeAmount +
            ", transactionPercentFeeValue=" +
            transactionPercentFeeValue +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return (
            Objects.equals(bankId, bank.bankId) &&
            Objects.equals(bankName, bank.bankName) &&
            Objects.equals(totalTransactionFeeAmount, bank.totalTransactionFeeAmount) &&
            Objects.equals(totalTransferAmount, bank.totalTransferAmount) &&
            Objects.equals(transactionFlatFeeAmount, bank.transactionFlatFeeAmount) &&
            Objects.equals(transactionPercentFeeValue, bank.transactionPercentFeeValue)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            bankId,
            bankName,
            totalTransactionFeeAmount,
            totalTransferAmount,
            transactionFlatFeeAmount,
            transactionPercentFeeValue
        );
    }
}
