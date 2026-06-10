-- ============================================================
-- 🗑️ DROP: eliminar tabla si existe (para recrear desde cero)
-- Se ejecuta cada vez que la aplicación se levanta
-- ============================================================
DROP TABLE IF EXISTS accounts;

-- ============================================================
-- 🏗️ CREATE: creación de la tabla accounts
-- ============================================================
CREATE TABLE accounts
(
    id             BIGSERIAL      NOT NULL,
    account_number VARCHAR(20)    NOT NULL,
    customer_id    VARCHAR(20)    NOT NULL,
    account_type   VARCHAR(20)    NOT NULL,
    currency       VARCHAR(3)     NOT NULL,
    balance        NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    status         VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    alias          VARCHAR(50),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_accounts PRIMARY KEY (id),
    CONSTRAINT uq_accounts_account_number UNIQUE (account_number),
    CONSTRAINT chk_accounts_type CHECK (account_type IN ('SAVINGS', 'CHECKING', 'FIXED_TERM')),
    CONSTRAINT chk_accounts_currency CHECK (currency IN ('PEN', 'USD')),
    CONSTRAINT chk_accounts_status CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
    CONSTRAINT chk_accounts_balance CHECK (balance >= 0)
);
