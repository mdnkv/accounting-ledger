package dev.mednikov.accounting.ledger.currencies.repositories;

import dev.mednikov.accounting.ledger.currencies.models.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

    boolean existsByCurrencyCodeAndOrganizationId (String code, UUID organizationId);

    @Query("SELECT c FROM Currency c WHERE c.organizationId = :organizationId AND c.primary = true")
    Optional<Currency> getPrimaryCurrency(UUID organizationId);

    Page<Currency> findAllByOrganizationId (UUID organizationId, Pageable pageable);

}
