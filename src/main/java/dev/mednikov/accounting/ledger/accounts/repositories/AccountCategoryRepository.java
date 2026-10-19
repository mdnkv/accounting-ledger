package dev.mednikov.accounting.ledger.accounts.repositories;

import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, UUID> {

    boolean existsByNameAndOrganizationId(String name, UUID organizationId);

    Page<AccountCategory> findAllByOrganizationId (UUID organizationId, Pageable pageable);

}
