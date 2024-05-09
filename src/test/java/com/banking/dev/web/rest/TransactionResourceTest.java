package com.banking.dev.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.banking.dev.domain.Transaction;
import com.banking.dev.service.TransactionService;
import com.banking.dev.web.rest.vm.DepositRequest;
import com.banking.dev.web.rest.vm.TransferRequest;
import com.banking.dev.web.rest.vm.WithdrawalRequest;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TransactionResourceTest {

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionResource transactionResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createWithdrawalTransactionTest() throws URISyntaxException {
        // Mock data
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        when(transactionService.processWithdrawal(withdrawalRequest)).thenReturn(new Transaction());

        // Test
        ResponseEntity<Transaction> response = transactionResource.createWithdrawalTransaction(withdrawalRequest);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createDepositTransactionTest() throws URISyntaxException {
        // Mock data
        DepositRequest depositRequest = new DepositRequest();
        when(transactionService.processDeposit(depositRequest)).thenReturn(new Transaction());

        // Test
        ResponseEntity<Transaction> response = transactionResource.createDepositTransaction(depositRequest);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createTransferTransactionTest() throws URISyntaxException {
        // Mock data
        TransferRequest transferRequest = new TransferRequest();
        when(transactionService.processTransfer(transferRequest)).thenReturn(new Transaction());

        // Test
        ResponseEntity<Transaction> response = transactionResource.createTransferTransaction(transferRequest);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getTransactionTest() {
        // Mock data
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(transactionService.findOne(transactionId)).thenReturn(Optional.of(transaction));

        // Test
        ResponseEntity<Transaction> response = transactionResource.getTransaction(transactionId);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    void getAllTransactionsTest() {
        // Mock data
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());
        when(transactionService.findAll()).thenReturn(transactions);

        // Test
        ResponseEntity<List<Transaction>> response = transactionResource.getAllTransactions();

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void createTransactionTest() throws URISyntaxException {
        // Mock data
        Transaction transaction = new Transaction();
        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        // Test
        ResponseEntity<Transaction> response = transactionResource.createTransaction(transaction);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateTransactionTest() {
        // Mock data
        Long transactionId = 1L;
        Transaction updatedTransaction = new Transaction();
        when(transactionService.updateTransaction(transactionId, updatedTransaction)).thenReturn(updatedTransaction);

        // Test
        ResponseEntity<Transaction> response = transactionResource.updateTransaction(transactionId, updatedTransaction);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTransaction, response.getBody());
    }

    @Test
    void deleteTransactionTest() {
        // Mock data
        Long transactionId = 1L;
        doNothing().when(transactionService).delete(transactionId);

        // Test
        ResponseEntity<Void> response = transactionResource.deleteTransaction(transactionId);

        // Assertion
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
