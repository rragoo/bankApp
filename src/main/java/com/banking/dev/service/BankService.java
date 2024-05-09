package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Bank;
import com.banking.dev.domain.Transaction;
import com.banking.dev.repository.AccountRepository;
import com.banking.dev.repository.BankRepository;
import com.banking.dev.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BankService {

    private final Logger log = LoggerFactory.getLogger(BankService.class);

    private final BankRepository bankRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final BigDecimal totalTransactionFeeAmount = BigDecimal.ZERO;

    public BankService(BankRepository bankRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Optional<Bank> findOne(Long id) {
        log.debug("Request to find bank by ID: {}", id);
        return bankRepository.findById(id);
    }

    public List<Bank> findAll() {
        log.debug("Request to find all Banks");
        return bankRepository.findAll();
    }

    public Bank createBank(Bank bank) {
        log.debug("Request to create Bank: {}", bank);
        // Optionally perform any additional logic/validation here
        return bankRepository.save(bank);
    }

    public Bank updateBank(Long bankId, Bank updatedBank) {
        log.debug("Service request to update Bank with ID: {}", bankId);
        Bank existingBank = bankRepository
            .findById(bankId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found"));

        existingBank.setBankName(updatedBank.getBankName());
        existingBank.setTotalTransactionFeeAmount(updatedBank.getTotalTransactionFeeAmount());
        existingBank.setTotalTransferAmount(updatedBank.getTotalTransferAmount());
        existingBank.setTransactionFlatFeeAmount(updatedBank.getTransactionFlatFeeAmount());
        existingBank.setTransactionPercentFeeValue(updatedBank.getTransactionPercentFeeValue());

        log.debug("Updated Bank: {}", existingBank);
        return bankRepository.save(existingBank);
    }

    public void delete(Long id) {
        log.debug("Service request to delete Bank with ID: {}", id);

        boolean exists = bankRepository.existsById(id);

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");
        }

        bankRepository.deleteById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public BigDecimal calculateTotalTransactionFeeAmount() {
        BigDecimal totalTransactionFeeAmount = BigDecimal.ZERO;

        // Iterate over all transactions
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            BigDecimal transactionAmount = transaction.getAmount();
            BigDecimal transactionFee = calculateTransactionFee(transaction);

            totalTransactionFeeAmount = totalTransactionFeeAmount.add(transactionFee);
        }

        return totalTransactionFeeAmount;
    }

    private BigDecimal calculateTransactionFee(Transaction transaction) {
        BigDecimal transactionAmount = transaction.getAmount();

        // Calculate flat fee
        BigDecimal flatFee = BigDecimal.TEN;

        // Calculate percentage fee (5%)
        BigDecimal percentageFee = transactionAmount.multiply(BigDecimal.valueOf(0.05));

        // Total transaction fee amount for this transaction
        BigDecimal totalFeeAmount = flatFee.add(percentageFee);

        return totalFeeAmount;
    }

    public BigDecimal calculateTotalTransferAmount() {
        List<Transaction> allTransactions = transactionRepository.findAll();
        BigDecimal totalTransferAmount = BigDecimal.ZERO;

        for (Transaction transaction : allTransactions) {
            if ("Transfer".equals(transaction.getTransactionReason())) {
                totalTransferAmount = totalTransferAmount.add(transaction.getAmount().abs());
            }
        }

        return totalTransferAmount;
    }
}
