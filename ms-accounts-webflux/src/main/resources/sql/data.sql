-- ============================================================
-- 🌱 SEED: datos de prueba para desarrollo
-- 10 cuentas de distintos clientes, tipos, monedas y estados
-- ============================================================
INSERT INTO accounts (account_number, customer_id, account_type, currency, balance, status, alias)
VALUES ('00219100230000000001', 'CLI-00001', 'SAVINGS', 'PEN', 1500.00, 'ACTIVE', 'Mi cuenta de ahorros'),
       ('00219100230000000002', 'CLI-00001', 'CHECKING', 'USD', 3200.50, 'ACTIVE', 'Cuenta corriente USD'),
       ('00219100230000000003', 'CLI-00002', 'SAVINGS', 'PEN', 800.00, 'ACTIVE', NULL),
       ('00219100230000000004', 'CLI-00002', 'FIXED_TERM', 'PEN', 10000.00, 'BLOCKED', 'Plazo fijo bloqueado'),
       ('00219100230000000005', 'CLI-00003', 'SAVINGS', 'USD', 2500.75, 'ACTIVE', 'Ahorros en dólares'),
       ('00219100230000000006', 'CLI-00003', 'CHECKING', 'PEN', 0.00, 'CLOSED', 'Cuenta cerrada'),
       ('00219100230000000007', 'CLI-00004', 'SAVINGS', 'PEN', 4750.25, 'ACTIVE', 'Fondo de emergencia'),
       ('00219100230000000008', 'CLI-00004', 'FIXED_TERM', 'USD', 15000.00, 'ACTIVE', 'Inversión a plazo'),
       ('00219100230000000009', 'CLI-00005', 'SAVINGS', 'PEN', 250.00, 'BLOCKED', NULL),
       ('00219100230000000010', 'CLI-00005', 'CHECKING', 'PEN', 1100.00, 'ACTIVE', 'Cuenta principal');
