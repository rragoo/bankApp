package com.banking.dev.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Transaction;
import com.banking.dev.repository.AccountRepository;
import com.banking.dev.repository.TransactionRepository;
import com.banking.dev.web.rest.vm.DepositRequest;
import com.banking.dev.web.rest.vm.TransferRequest;
import com.banking.dev.web.rest.vm.WithdrawalRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @InjectMocks
    private BankService bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindOne() {
        // Mock data
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // Test
        Optional<Transaction> result = transactionService.findOne(1L);

        // Assertion
        Assertions.assertTrue(result.isPresent());
        assertEquals(1L, result.get().getTransactionId());
    }

    @Test
    void testFindAll() {
        // Mock data
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Test
        List<Transaction> result = transactionService.findAll();

        // Assertion
        assertEquals(transactions.size(), result.size());
    }

    @Test
    void testCreateTransaction() {
        // Mock data
        Transaction transaction = new Transaction();
        when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        // Test
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Assertion
        assertNotNull(createdTransaction);
    }

    @Test
    void testUpdateTransaction() {
        // Mock data
        Long transactionId = 1L;
        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(transactionId);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionId(transactionId);
        updatedTransaction.setAmount(BigDecimal.valueOf(100.00));

        //Mock repository save method to return the updated transaction
        when(transactionRepository.save(Mockito.any())).thenReturn(updatedTransaction);

        // Test
        Transaction result = transactionService.updateTransaction(transactionId, updatedTransaction);

        //Assertion
        assertEquals(updatedTransaction, result);
    }

    @Test
    void testDelete() {
        // Mock data
        Long transactionId = 1L;
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        // Test
        transactionService.delete(transactionId);

        // Assertion
        verify(transactionRepository).deleteById(transactionId);
    }

    @Test
    void testProcessWithdrawal_InsufficientFunds() {
        // Mock data
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setAccountId(1L);
        withdrawalRequest.setAmount(BigDecimal.valueOf(1000.00));

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500.00)); // Account balance less than withdrawal amount
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Test and assertion
        Assertions.assertThrows(ResponseStatusException.class, () -> transactionService.processWithdrawal(withdrawalRequest));
    }

    @Test
    void testProcessTransfer_InsufficientFunds() {
        // Mock data
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(BigDecimal.valueOf(1000.00));

        Account sourceAccount = new Account();
        sourceAccount.setBalance(BigDecimal.valueOf(500.00)); // Source account balance less than transfer amount
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        Account targetAccount = new Account();
        targetAccount.setBalance(BigDecimal.valueOf(2000.00)); // Target account balance
        when(accountRepository.findById(2L)).thenReturn(Optional.of(targetAccount));

        // Test and assertion
        Assertions.assertThrows(ResponseStatusException.class, () -> transactionService.processTransfer(transferRequest));
    }

    @Test
    void testProcessDeposit() {
        // Mock data
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId(1L);
        depositRequest.setAmount(BigDecimal.valueOf(100.00));

        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(BigDecimal.valueOf(500.00)); // Initial account balance
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Test
        Transaction transaction = transactionService.processDeposit(depositRequest);

        // Assertion
        assertNotNull(transaction);
        assertEquals(depositRequest.getAmount(), transaction.getAmount());
        assertEquals("Deposit", transaction.getTransactionReason());
        assertEquals(account, transaction.getOriginatingAccount());
        assertEquals(account.getBalance().add(depositRequest.getAmount()), transaction.getOriginatingAccount().getBalance());
    }
}
