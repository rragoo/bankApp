package com.banking.dev.service;

import com.banking.dev.domain.Account;
import com.banking.dev.repository.AccountRepository;
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

class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindOne() {
        // Mock data
        Account account = new Account();
        account.setAccountId(1L);
        account.setUserName("Test User");
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Test
        Optional<Account> result = accountService.findOne(1L);

        // Assertion
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test User", result.get().getUserName());
    }

    @Test
    void testFindAll() {
        // Mock data
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        accounts.add(new Account());
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        // Test
        List<Account> result = accountService.findAll();

        // Assertion
        Assertions.assertEquals(accounts.size(), result.size());
    }

    @Test
    void testCreateAccount() {
        // Mock data
        Account account = new Account();
        account.setUserName("Test User");
        account.setBalance(BigDecimal.valueOf(1000.00));
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);

        // Test
        Account createdAccount = accountService.createAccount(account);

        // Assertion
        Assertions.assertNotNull(createdAccount);
        Assertions.assertEquals("Test User", createdAccount.getUserName());
        Assertions.assertEquals(BigDecimal.valueOf(1000.00), createdAccount.getBalance());
    }

    @Test
    void testUpdateAccount() {
        // Mock data
        Account existingAccount = new Account();
        existingAccount.setAccountId(1L);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        Account updatedAccount = new Account();
        updatedAccount.setAccountId(1L);
        updatedAccount.setUserName("Updated User");
        updatedAccount.setBalance(BigDecimal.valueOf(1000.00));

        // Mock repository save method to return the updated account
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(updatedAccount);

        // Test
        Account result = accountService.updateAccount(1L, updatedAccount);

        // Assertion
        Assertions.assertEquals(updatedAccount, result);
    }

    @Test
    void testDelete() {
        // Mock data
        Long accountId = 1L;
        Mockito.when(accountRepository.existsById(accountId)).thenReturn(true);

        // Test
        accountService.delete(accountId);

        // Assertion
        Mockito.verify(accountRepository).deleteById(accountId);
    }
}
