package com.banking.dev.repository;

import com.banking.dev.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Bank} entity.
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {}
