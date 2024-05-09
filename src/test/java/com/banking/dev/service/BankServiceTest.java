package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Bank;
import com.banking.dev.domain.Transaction;
import com.banking.dev.repository.AccountRepository;
import com.banking.dev.repository.BankRepository;
import com.banking.dev.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BankServiceTest {

    @Mock
    BankRepository bankRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    private BankService bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindOne() {
        // Mock data
        Bank bank = new Bank();
        bank.setBankId(1L);
        bank.setBankName("Test Bank");
        Mockito.when(bankRepository.findById(1L)).thenReturn(Optional.of(bank));

        // Test
        Optional<Bank> result = bankService.findOne(1L);

        // Assertion
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test Bank", result.get().getBankName());
    }

    @Test
    void testFindAll() {
        // Mock data
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank());
        banks.add(new Bank());
        Mockito.when(bankRepository.findAll()).thenReturn(banks);

        // Test
        List<Bank> result = bankService.findAll();

        // Assertion
        Assertions.assertEquals(banks.size(), result.size());
    }

    @Test
    void testUpdateBank() {
        // Mock data
        Long bankId = 1L;
        Bank existingBank = new Bank();
        existingBank.setBankId(bankId);
        Mockito.when(bankRepository.findById(bankId)).thenReturn(Optional.of(existingBank));

        Bank updatedBank = new Bank();
        updatedBank.setBankId(bankId);
        updatedBank.setBankName("Updated Bank");
        updatedBank.setTotalTransactionFeeAmount(BigDecimal.valueOf(100.00));

        // Mock repository save method to return the updated bank
        Mockito.when(bankRepository.save(Mockito.any())).thenReturn(updatedBank);

        // Test
        Bank result = bankService.updateBank(bankId, updatedBank);

        // Assertion
        Assertions.assertEquals(updatedBank, result);
    }

    @Test
    void testCreateBank() {
        // Mock data
        Bank bank = new Bank();
        bank.setBankName("Test Bank");

        // Mock repository save method to return the saved bank
        Mockito.when(bankRepository.save(Mockito.any())).thenReturn(bank);

        // Test
        Bank result = bankService.createBank(bank);

        // Assertion
        Assertions.assertEquals(bank, result);
    }

    @Test
    void testGetAllAccounts() {
        // Mock data
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        accounts.add(new Account());
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        // Test
        List<Account> result = bankService.getAllAccounts();

        // Assertion
        Assertions.assertEquals(accounts.size(), result.size());
    }

    @Test
    void testCalculateTotalTransferAmount() {
        // Mock data
        Transaction transferTransaction1 = new Transaction();
        transferTransaction1.setTransactionReason("Transfer");
        transferTransaction1.setAmount(BigDecimal.valueOf(150.00));
        Transaction transferTransaction2 = new Transaction();
        transferTransaction2.setTransactionReason("Transfer");
        transferTransaction2.setAmount(BigDecimal.valueOf(250.00));
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setTransactionReason("Withdrawal");
        withdrawalTransaction.setAmount(BigDecimal.valueOf(100.00));
        List<Transaction> transactions = Arrays.asList(transferTransaction1, transferTransaction2, withdrawalTransaction);
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);

        // Test
        BigDecimal result = bankService.calculateTotalTransferAmount();

        // Assertion - Calculate the expected total transfer amount manually and compare it with the result
        BigDecimal expectedTotal = BigDecimal.valueOf(400.00); // Sum of transfer amounts
        Assertions.assertEquals(expectedTotal, result);
    }

    @Test
    void testDelete() {
        // Mock data
        Long bankId = 1L;
        Mockito.when(bankRepository.existsById(bankId)).thenReturn(true);

        // Test
        bankService.delete(bankId);

        // Assertion
        Mockito.verify(bankRepository).deleteById(bankId);
    }
}
