# Scripts SQL

- `schema.sql`: crea le tabelle del sistema e-commerce.
- `seed.sql`: inserta i prodotti di esempi.

- Importare in MySQL:
  ```bash
  mysql -u ecom -pecom123! ecommerce < schema.sql
  mysql -u ecom -pecom123! ecommerce < seed.sql