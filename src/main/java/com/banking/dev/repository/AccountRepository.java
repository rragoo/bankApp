package com.banking.dev.repository;

import com.banking.dev.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Account} entity.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}
