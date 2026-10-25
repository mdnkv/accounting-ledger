package dev.mednikov.accounting.ledger.periods.models;

import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
name = "periods",
uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "period_start_date", "period_end_date"})}
)
public class Period implements Comparable<Period> {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "closing_journal_entry_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private JournalEntry closingJournalEntry;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "is_closed", nullable = false)
    private boolean closed;

    @Override
    public int compareTo(Period other) {
        return this.periodStart.compareTo(other.getPeriodStart());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Period period)) return false;

        return organizationId.equals(period.organizationId)
                && periodStart.equals(period.periodStart)
                && periodEnd.equals(period.periodEnd);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + periodStart.hashCode();
        result = 31 * result + periodEnd.hashCode();
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public JournalEntry getClosingJournalEntry() {
        return closingJournalEntry;
    }

    public void setClosingJournalEntry(JournalEntry closingJournalEntry) {
        this.closingJournalEntry = closingJournalEntry;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
