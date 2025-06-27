INSERT INTO balance (id, date, balance)
SELECT 1, '2025-01-30', '0'
WHERE NOT EXISTS (
    SELECT 1 FROM balance WHERE id = 1
);