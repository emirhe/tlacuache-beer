# Scripts SQL

- `schema.sql`: crea le tabelle del sistema e-commerce.
- `seed.sql`: inserta i prodotti di esempi.

- Importare in MySQL:
  ```bash
  mysql -u tlacuache -p tlacuache_beer < schema.sql
  mysql -u tlacuache -p tlacuache_beer < seed.sql