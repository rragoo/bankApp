package com.banking.dev.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.banking.dev.domain.Account;
import com.banking.dev.service.AccountService;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BankAccountResourceTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BankAccountResource bankAccountResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccount_ValidId() {
        // Given
        Long accountId = 1L;
        Account account = new Account();
        account.setAccountId(accountId);
        when(accountService.findOne(accountId)).thenReturn(Optional.of(account));

        // When
        ResponseEntity<Account> responseEntity = bankAccountResource.getAccount(accountId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(account, responseEntity.getBody());
    }

    @Test
    void testGetAccount_InvalidId() {
        // Given
        Long accountId = 1L;
        when(accountService.findOne(accountId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Account> responseEntity = bankAccountResource.getAccount(accountId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllAccounts() {
        // Given
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(accountService.findAll()).thenReturn(accounts);

        // When
        ResponseEntity<List<Account>> responseEntity = bankAccountResource.getAllAccounts();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accounts, responseEntity.getBody());
    }

    @Test
    void testCreateAccount() throws URISyntaxException {
        // Given
        Account account = new Account();
        when(accountService.createAccount(account)).thenReturn(account);

        // When
        ResponseEntity<Account> responseEntity = bankAccountResource.createAccount(account);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey("Location"));
        assertEquals("/api/accounts/" + account.getAccountId(), responseEntity.getHeaders().getLocation().toString());
        assertEquals(account, responseEntity.getBody());
    }

    @Test
    void testUpdateAccount() {
        // Given
        Long accountId = 1L;
        Account updatedAccount = new Account();
        when(accountService.updateAccount(accountId, updatedAccount)).thenReturn(updatedAccount);

        // When
        ResponseEntity<Account> responseEntity = bankAccountResource.updateAccount(accountId, updatedAccount);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAccount, responseEntity.getBody());
    }

    @Test
    void testDeleteAccount() {
        // Given
        Long accountId = 1L;

        // When
        ResponseEntity<Void> responseEntity = bankAccountResource.deleteAccount(accountId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(accountService, times(1)).delete(accountId);
    }
}
