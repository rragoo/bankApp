package com.banking.dev.web.rest;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Bank;
import com.banking.dev.security.AuthoritiesConstants;
import com.banking.dev.service.BankService;
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
@RequestMapping("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private final BankService bankService;

    public BankResource(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Retrieves a specific bank by ID.
     *
     * @param id the ID of the bank to retrieve.
     * @return the specified bank, or a 404 response if not found.
     */
    @GetMapping("/banks/{id}")
    public ResponseEntity<Bank> getBank(@PathVariable Long id) {
        log.debug("REST request to get Bank by ID: {}", id);
        return bankService
            .findOne(id)
            .map(bank -> new ResponseEntity<>(bank, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all banks.
     *
     * @return a list of all banks.
     */
    @GetMapping("/banks")
    public ResponseEntity<List<Bank>> getAllBanks() {
        log.debug("REST request to get all Banks");
        List<Bank> banks = bankService.findAll();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    /**
     * Creates a new bank.
     *
     * @param bank the bank to create.
     * @return the created bank with a 201 response, or a 400 response if the bank is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banks")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank) throws URISyntaxException {
        log.debug("REST request to create Bank: {}", bank);
        Bank createdBank = bankService.createBank(bank);
        return ResponseEntity.created(new URI("/api/banks/" + createdBank.getBankId())).body(createdBank);
    }

    /**
     * Updates an existing bank.
     *
     * @param id   the ID of the bank to update.
     * @param updatedBank the updated bank data.
     * @return the updated bank with a 200 response, or a 400 response if the update is invalid.
     */
    @PutMapping("/banks/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Bank> updateBank(@PathVariable Long id, @RequestBody Bank updatedBank) {
        log.debug("REST request to update Bank with ID: {}", id);

        Bank updated = bankService.updateBank(id, updatedBank);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /**
     * Deletes a bank by ID.
     *
     * @param id the ID of the bank to delete.
     * @return a 204 response indicating successful deletion.
     */
    @DeleteMapping("/banks/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        log.debug("REST request to delete Bank with ID: {}", id);
        bankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
