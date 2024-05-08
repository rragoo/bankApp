package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Transaction;
import com.banking.dev.repository.AccountRepository;
import com.banking.dev.repository.TransactionRepository;
import com.banking.dev.web.rest.vm.DepositRequest;
import com.banking.dev.web.rest.vm.TransferRequest;
import com.banking.dev.web.rest.vm.WithdrawalRequest;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to find transaction by ID: {}", id);
        return transactionRepository.findById(id);
    }

    public List<Transaction> findAll() {
        log.debug("Request to find all Transactions");
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        log.debug("Request to create Transaction: {}", transaction);
        // Optionally perform any additional logic/validation here
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long transactionId, Transaction updatedTransaction) {
        log.debug("Service request to update Transaction with ID: {}", transactionId);
        Transaction existingTransaction = transactionRepository
            .findById(transactionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setOriginatingAccount(updatedTransaction.getOriginatingAccount());
        existingTransaction.setResultingAccount(updatedTransaction.getResultingAccount());
        existingTransaction.setTransactionReason(updatedTransaction.getTransactionReason());

        log.debug("Updated Transaction: {}", existingTransaction);
        return transactionRepository.save(existingTransaction);
    }

    public void delete(Long id) {
        log.debug("Service request to delete Transaction with ID: {}", id);

        boolean exists = transactionRepository.existsById(id);

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        transactionRepository.deleteById(id);
    }

    public Transaction processWithdrawal(WithdrawalRequest withdrawalRequest) {
        log.debug("Service request to process Withdrawal: {}", withdrawalRequest);

        Account account = accountRepository
            .findById(withdrawalRequest.getAccountId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BigDecimal withdrawalAmount = withdrawalRequest.getAmount();
        BigDecimal currentBalance = account.getBalance();

        if (currentBalance.compareTo(withdrawalAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        account.setBalance(currentBalance.subtract(withdrawalAmount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawalAmount.negate()); // Negative amount for withdrawal
        transaction.setOriginatingAccount(account);
        transaction.setTransactionReason("Withdrawal");

        return transactionRepository.save(transaction);
    }

    public Transaction processDeposit(DepositRequest depositRequest) {
        log.debug("Service request to process Deposit: {}", depositRequest);

        Account account = accountRepository
            .findById(depositRequest.getAccountId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BigDecimal depositAmount = depositRequest.getAmount();
        BigDecimal currentBalance = account.getBalance();

        account.setBalance(currentBalance.add(depositAmount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(depositAmount);
        transaction.setOriginatingAccount(account);
        transaction.setTransactionReason("Deposit");

        return transactionRepository.save(transaction);
    }

    public Transaction processTransfer(TransferRequest transferRequest) {
        log.debug("Service request to process Transfer: {}", transferRequest);

        Long sourceAccountId = transferRequest.getSourceAccountId();
        Long targetAccountId = transferRequest.getDestinationAccountId();

        Account sourceAccount = accountRepository
            .findById(sourceAccountId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));

        Account targetAccount = accountRepository
            .findById(targetAccountId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found"));

        BigDecimal transferAmount = transferRequest.getAmount();
        BigDecimal sourceBalance = sourceAccount.getBalance();

        if (sourceBalance.compareTo(transferAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        sourceAccount.setBalance(sourceBalance.subtract(transferAmount));
        targetAccount.setBalance(targetAccount.getBalance().add(transferAmount));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount.negate()); // Negative amount for transfer
        transaction.setOriginatingAccount(sourceAccount);
        transaction.setResultingAccount(targetAccount);
        transaction.setTransactionReason("Transfer");

        return transactionRepository.save(transaction);
    }
}
