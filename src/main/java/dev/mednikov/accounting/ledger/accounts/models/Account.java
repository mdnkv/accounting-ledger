package dev.mednikov.accounting.ledger.accounts.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "account_code"})})
public class Account implements Comparable<Account> {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_category_id", nullable = false)
    private AccountCategory accountCategory;

    @Column(name = "balance_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountBalanceType balanceType;

    @Column(name = "account_name", nullable = false)
    private String name;

    @Column(name = "account_code", nullable = false)
    private String code;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;

        return organizationId.equals(account.organizationId) && code.equals(account.code);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }

    @Override
    public int compareTo(Account other) {
        return this.code.compareTo(other.getCode());
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

    public AccountCategory getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(AccountCategory accountCategory) {
        this.accountCategory = accountCategory;
    }

    public AccountBalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(AccountBalanceType balanceType) {
        this.balanceType = balanceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
