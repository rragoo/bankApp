package com.banking.dev.web.rest;

import com.banking.dev.domain.Transaction;
import com.banking.dev.security.AuthoritiesConstants;
import com.banking.dev.service.TransactionService;
import com.banking.dev.web.rest.vm.DepositRequest;
import com.banking.dev.web.rest.vm.TransferRequest;
import com.banking.dev.web.rest.vm.WithdrawalRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Creates a withdrawal transaction.
     *
     * @param withdrawalRequest the withdrawal request data.
     * @return the created transaction with a 201 response, or a 400 response if the request is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions/withdrawal")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> createWithdrawalTransaction(@RequestBody WithdrawalRequest withdrawalRequest)
        throws URISyntaxException {
        log.debug("REST request to create Withdrawal Transaction: {}", withdrawalRequest);
        Transaction createdTransaction = transactionService.processWithdrawal(withdrawalRequest);
        return ResponseEntity.created(new URI("/api/transactions/" + createdTransaction.getTransactionId())).body(createdTransaction);
    }

    /**
     * Creates a deposit transaction.
     *
     * @param depositRequest the deposit request data.
     * @return the created transaction with a 201 response, or a 400 response if the request is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions/deposit")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> createDepositTransaction(@RequestBody DepositRequest depositRequest) throws URISyntaxException {
        log.debug("REST request to create Deposit Transaction: {}", depositRequest);
        Transaction createdTransaction = transactionService.processDeposit(depositRequest);
        return ResponseEntity.created(new URI("/api/transactions/" + createdTransaction.getTransactionId())).body(createdTransaction);
    }

    /**
     * Creates a transfer transaction.
     *
     * @param transferRequest the transfer request data.
     * @return the created transaction with a 201 response, or a 400 response if the request is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions/transfer")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> createTransferTransaction(@RequestBody TransferRequest transferRequest) throws URISyntaxException {
        log.debug("REST request to create Transfer Transaction: {}", transferRequest);
        Transaction createdTransaction = transactionService.processTransfer(transferRequest);
        return ResponseEntity.created(new URI("/api/transactions/" + createdTransaction.getTransactionId())).body(createdTransaction);
    }

    /**
     * Retrieves a specific transaction by ID.
     *
     * @param id the ID of the transaction to retrieve.
     * @return the specified transaction, or a 404 response if not found.
     */
    @GetMapping("/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction by ID: {}", id);
        Optional<Transaction> transaction = transactionService.findOne(id);
        return transaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        log.debug("REST request to get all Transactions");
        List<Transaction> transactions = transactionService.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction the transaction to create.
     * @return the created transaction with a 201 response, or a 400 response if the transaction is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to create Transaction: {}", transaction);
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.created(new URI("/api/transactions/" + createdTransaction.getTransactionId())).body(createdTransaction);
    }

    /**
     * Updates an existing transaction.
     * Access restricted to administrators only.
     *
     * @param id                 the ID of the company to update.
     * @param updatedTransaction the updated company data.
     * @return the updated company with a 200 response, or a 400 response if the update is invalid.
     */
    @PutMapping("/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        log.debug("REST request to update Transaction with ID: {}", id);
        Transaction updated = transactionService.updateTransaction(id, updatedTransaction);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /**
     * Deletes a transaction by ID.
     *
     * @param id the ID of the transaction to delete.
     * @return a 204 response indicating successful deletion.
     */
    @DeleteMapping("/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction with ID: {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
