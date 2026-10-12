BEGIN;

CREATE TABLE IF NOT EXISTS account_categories (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL,
    UNIQUE (organization_id, category_name)
);

CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    account_code VARCHAR(25) NOT NULL,
    account_category_id UUID NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    balance_type VARCHAR(15) NOT NULL,
    is_active BOOLEAN NOT NULL,
    UNIQUE (organization_id, account_code),
    CONSTRAINT fk_accounts_account_category
        FOREIGN KEY (account_category_id)
            REFERENCES account_categories(id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS currencies (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_primary BOOLEAN NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    currency_name VARCHAR(255) NOT NULL,
    UNIQUE (organization_id, currency_code)
);

CREATE TABLE IF NOT EXISTS journal_entries (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    currency_id UUID NOT NULL,
    entry_description text not null,
    entry_date date not null,
    total_debit decimal(15, 2) not null,
    total_credit decimal(15, 2) not null,
    journal_entry_type varchar(50) not null,
    CONSTRAINT fk_journal_entries_currency
        FOREIGN KEY (currency_id)
            REFERENCES currencies(id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS journal_entry_lines (
    id UUID PRIMARY KEY,
    journal_entry_id UUID NOT NULL,
    account_id UUID NOT NULL,
    debit decimal(15, 2) not null,
    credit decimal(15, 2) not null,
    line_position INTEGER NOT NULL CHECK (line_position >= 0),
    CONSTRAINT fk_journal_entry_lines_entry
        FOREIGN KEY (journal_entry_id)
            REFERENCES journal_entries(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_journal_entry_lines_account
        FOREIGN KEY (account_id)
            REFERENCES accounts(id)
            ON DELETE CASCADE
);

COMMIT;