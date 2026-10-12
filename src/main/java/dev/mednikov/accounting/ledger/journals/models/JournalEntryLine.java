package dev.mednikov.accounting.ledger.journals.models;

import dev.mednikov.accounting.ledger.accounts.models.Account;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "journal_entry_lines")
public class JournalEntryLine implements Comparable<JournalEntryLine> {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    @Column(name = "debit", nullable = false)
    private BigDecimal debit;

    @Column(name = "credit", nullable = false)
    private BigDecimal credit;

    @Column(name = "line_position", nullable = false)
    private int position;

    @Override
    public int compareTo(JournalEntryLine other) {
        return Integer.compare(this.position, other.getPosition());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JournalEntryLine that)) return false;

        return position == that.position
                && account.equals(that.account)
                && debit.equals(that.debit)
                && credit.equals(that.credit);
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + debit.hashCode();
        result = 31 * result + credit.hashCode();
        result = 31 * result + position;
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
