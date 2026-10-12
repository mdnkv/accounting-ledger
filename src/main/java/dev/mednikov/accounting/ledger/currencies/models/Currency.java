package dev.mednikov.accounting.ledger.currencies.models;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "currencies", uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "currency_code"})})
public class Currency implements Comparable<Currency>{

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "currency_name", nullable = false)
    private String name;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Currency currency)) return false;

        return organizationId.equals(currency.organizationId) && currencyCode.equals(currency.currencyCode);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + currencyCode.hashCode();
        return result;
    }

    @Override
    public int compareTo(Currency other) {
        return this.currencyCode.compareTo(other.getCurrencyCode());
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
