package dev.mednikov.accounting.ledger.accounts.repositories;

import dev.mednikov.accounting.ledger.accounts.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByCodeAndOrganizationId (String code, UUID organizationId);

    Page<Account> findAllByOrganizationId (UUID organizationId, Pageable pageable);

}
