package dev.mednikov.accounting.ledger.periods.repositories;

import dev.mednikov.accounting.ledger.periods.models.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PeriodRepository extends JpaRepository<Period, UUID> {

    boolean existsByOrganizationIdAndPeriodStartAndPeriodEnd(UUID organizationId, LocalDate periodStart, LocalDate periodEnd);

    Page<Period> findAllByOrganizationId(UUID organizationId, Pageable pageable);


}
