package com.banking.dev.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "account")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bank_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bank bank;

    public Account() {
        // Default constructor with no arguments
    }

    public Account(Long accountId, String userName, BigDecimal balance, Bank bank) {
        this.accountId = accountId;
        this.userName = userName;
        this.balance = balance;
        this.bank = bank;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + ", userName='" + userName + '\'' + ", balance=" + balance + ", bank=" + bank + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return (
            Objects.equals(accountId, account.accountId) &&
            Objects.equals(userName, account.userName) &&
            Objects.equals(balance, account.balance) &&
            Objects.equals(bank, account.bank)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, userName, balance, bank);
    }
}
