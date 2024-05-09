package com.banking.dev.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.banking.dev.domain.Account;
import com.banking.dev.domain.Bank;
import com.banking.dev.service.BankService;
import java.math.BigDecimal;
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

public class BankResourceTest {

    @Mock
    BankService bankService;

    @InjectMocks
    BankResource bankResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllBankAccounts() {
        // Mock data
        List<Account> accounts = new ArrayList<>();
        when(bankService.getAllAccounts()).thenReturn(accounts);

        // Test
        ResponseEntity<List<Account>> responseEntity = bankResource.getAllBankAccounts();

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accounts, responseEntity.getBody());
    }

    @Test
    void testGetTotalTransactionFeeAmount() {
        // Mock data
        BigDecimal totalTransactionFeeAmount = BigDecimal.valueOf(100.00);
        when(bankService.calculateTotalTransactionFeeAmount()).thenReturn(totalTransactionFeeAmount);

        // Test
        ResponseEntity<BigDecimal> responseEntity = bankResource.getTotalTransactionFeeAmount();

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(totalTransactionFeeAmount, responseEntity.getBody());
    }

    @Test
    void testGetTotalTransferAmount() {
        // Mock data
        BigDecimal totalTransferAmount = BigDecimal.valueOf(1000.00);
        when(bankService.calculateTotalTransferAmount()).thenReturn(totalTransferAmount);

        // Test
        ResponseEntity<BigDecimal> responseEntity = bankResource.getTotalTransferAmount();

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(totalTransferAmount, responseEntity.getBody());
    }

    @Test
    void testGetBank() {
        // Mock data
        Long id = 1L;
        Bank bank = new Bank();
        when(bankService.findOne(id)).thenReturn(Optional.of(bank));

        // Test
        ResponseEntity<Bank> responseEntity = bankResource.getBank(id);

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bank, responseEntity.getBody());
    }

    @Test
    void testGetAllBanks() {
        // Mock data
        List<Bank> banks = new ArrayList<>();
        when(bankService.findAll()).thenReturn(banks);

        // Test
        ResponseEntity<List<Bank>> responseEntity = bankResource.getAllBanks();

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(banks, responseEntity.getBody());
    }

    @Test
    void testCreateBank() throws URISyntaxException {
        // Mock data
        Bank bank = new Bank();
        when(bankService.createBank(bank)).thenReturn(bank);

        // Test
        ResponseEntity<Bank> responseEntity = bankResource.createBank(bank);

        // Assertion
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bank, responseEntity.getBody());
    }

    @Test
    void testUpdateBank() {
        // Mock data
        Long id = 1L;
        Bank bank = new Bank();
        when(bankService.updateBank(id, bank)).thenReturn(bank);

        // Test
        ResponseEntity<Bank> responseEntity = bankResource.updateBank(id, bank);

        // Assertion
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bank, responseEntity.getBody());
    }

    @Test
    void testDeleteBank() {
        // Mock data
        Long id = 1L;

        // Test
        ResponseEntity<Void> responseEntity = bankResource.deleteBank(id);

        // Assertion
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(bankService, times(1)).delete(id);
    }
}
