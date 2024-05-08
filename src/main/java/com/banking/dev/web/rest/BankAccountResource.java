package com.banking.dev.web.rest;

import com.banking.dev.domain.Account;
import com.banking.dev.security.AuthoritiesConstants;
import com.banking.dev.service.AccountService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private final AccountService accountService;

    public BankAccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Retrieves a specific account by ID.
     *
     * @param id the ID of the account to retrieve.
     * @return the specified account, or a 404 response if not found.
     */
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        log.debug("REST request to get Account by ID: {}", id);
        return accountService
            .findOne(id)
            .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts.
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.debug("REST request to get all Accounts");
        List<Account> accounts = accountService.findAll();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Creates a new account.
     *
     * @param account the account to create.
     * @return the created account with a 201 response, or a 400 response if the account is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accounts")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) throws URISyntaxException {
        log.debug("REST request to create Account: {}", account);
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.created(new URI("/api/accounts/" + createdAccount.getAccountId())).body(createdAccount);
    }

    /**
     * Updates an existing account.
     *
     * @param id      the ID of the account to update.
     * @param updatedAccount the updated account data.
     * @return the updated account with a 200 response, or a 400 response if the update is invalid.
     */
    @PutMapping("/accounts/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account updatedAccount) {
        log.debug("REST request to update Account with ID: {}", id);

        Account updated = accountService.updateAccount(id, updatedAccount);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /**
     * Deletes an account by ID.
     *
     * @param id the ID of the account to delete.
     * @return a 204 response indicating successful deletion.
     */
    @DeleteMapping("/accounts/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        log.debug("REST request to delete Account with ID: {}", id);
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
