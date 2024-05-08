package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Bank;
import com.banking.dev.repository.AccountRepository;
import com.banking.dev.repository.BankRepository;
import jakarta.transaction.Transactional;
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

    public BankService(BankRepository bankRepository, AccountRepository accountRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
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
}
