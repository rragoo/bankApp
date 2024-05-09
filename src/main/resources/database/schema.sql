CREATE TABLE bank (
    bank_id SERIAL PRIMARY KEY,
    bank_name VARCHAR(255) NOT NULL,
    total_transaction_fee_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_transfer_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    transaction_flat_fee_amount DECIMAL(10, 2) NOT NULL,
    transaction_percent_fee_value DECIMAL(5, 2) NOT NULL
);

CREATE TABLE account (
    account_id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    bank_id INTEGER,
    FOREIGN KEY (bank_id) REFERENCES bank(bank_id)
);

CREATE TABLE transaction (
    transaction_id SERIAL PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    originating_account_id INTEGER,
    resulting_account_id INTEGER,
    transaction_reason VARCHAR(255),
    FOREIGN KEY (originating_account_id) REFERENCES account(account_id),
    FOREIGN KEY (resulting_account_id) REFERENCES account(account_id)
);

ALTER TABLE account
ADD CONSTRAINT chk_balance_non_negative CHECK (balance >= 0);

