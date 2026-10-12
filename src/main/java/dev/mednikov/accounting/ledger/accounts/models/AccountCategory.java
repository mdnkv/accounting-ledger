package dev.mednikov.accounting.ledger.accounts.models;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "account_categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "category_name"})})
public class AccountCategory implements Comparable<AccountCategory>{

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AccountCategory that)) return false;

        return organizationId.equals(that.organizationId) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(AccountCategory other) {
        return this.name.compareTo(other.getName());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
