CREATE TABLE IF NOT EXISTS invoices (
  id VARCHAR(36) PRIMARY KEY,
  amount DECIMAL(19,4) NOT NULL,
  paid_amount DECIMAL(19,4) NOT NULL,
  due_date DATE NOT NULL,
  status VARCHAR(10) NOT NULL,
  overdue_days INTEGER
);


INSERT INTO invoices (id, amount, paid_amount, due_date, status, overdue_days) VALUES
  ('inv-1', 199.99, 0.0000, '2025-10-15', 'PENDING', 0),
  ('inv-2', 300.00, 100.0000, '2025-10-30', 'PENDING', 0),
  ('inv-3', 150.50, 150.5000, '2025-12-01', 'PAID', 0);