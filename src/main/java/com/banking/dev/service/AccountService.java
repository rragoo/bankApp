package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.repository.AccountRepository;
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
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findOne(Long id) {
        log.debug("Request to find account by ID: {}", id);
        return accountRepository.findById(id);
    }

    public List<Account> findAll() {
        log.debug("Request to find all Accounts");
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        log.debug("Request to create Account: {}", account);
        // Optionally perform any additional logic/validation here
        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, Account updatedAccount) {
        log.debug("Service request to update Account with ID: {}", accountId);
        Account existingAccount = accountRepository
            .findById(accountId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        existingAccount.setUserName(updatedAccount.getUserName());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setBank(updatedAccount.getBank());

        log.debug("Updated Account: {}", existingAccount);
        return accountRepository.save(existingAccount);
    }

    public void delete(Long id) {
        log.debug("Service request to delete Account with ID: {}", id);

        boolean exists = accountRepository.existsById(id);

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        accountRepository.deleteById(id);
    }
}
