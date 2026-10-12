package dev.mednikov.accounting.ledger.journals.models;

import dev.mednikov.accounting.ledger.currencies.models.Currency;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "journal_entries")
public class JournalEntry implements Comparable<JournalEntry> {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Currency currency;

    @Column(name = "journal_entry_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private JournalEntryType journalEntryType;

    @Column(name = "entry_description", nullable = false)
    private String description;

    @Column(name = "entry_date", nullable = false)
    private LocalDate date;

    @Column(name = "total_debit", nullable = false)
    private BigDecimal totalDebit;

    @Column(name = "total_credit", nullable = false)
    private BigDecimal totalCredit;

    @Override
    public int compareTo(JournalEntry other) {
        return this.date.compareTo(other.getDate());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JournalEntry that)) return false;

        return organizationId.equals(that.organizationId)
                && currency.equals(that.currency)
                && journalEntryType == that.journalEntryType
                && description.equals(that.description)
                && date.equals(that.date)
                && totalDebit.equals(that.totalDebit)
                && totalCredit.equals(that.totalCredit);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + journalEntryType.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + totalDebit.hashCode();
        result = 31 * result + totalCredit.hashCode();
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public JournalEntryType getJournalEntryType() {
        return journalEntryType;
    }

    public void setJournalEntryType(JournalEntryType journalEntryType) {
        this.journalEntryType = journalEntryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
}
