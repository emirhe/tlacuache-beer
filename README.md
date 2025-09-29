# Tlacuache Beer - E-commerce (Demo)

Aplicazione web **CRUD** per la gestione di un catalogo prodotti di e-commerce (birre artigianali).
Progetto sviluppato con **Jakarta EE + JSP/Servlet + JDBC** e un **connection pool**

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9-orange)


## Tech Stack
- Java 21
- Apache Tomcat 10.1
- Maven (packaging: `war`)
- JSP/JSTL, Servlets (Jakarta)
- MySQL 8.4
- DBCP2 (pool di connessioni JDBC)
- Bootstrap 5 (UI)

## Funzionalità
- Inizio (demo login)
- Home con barra di navigazione e filtri
- Lista prodotti con tabella 
- Inserimento e modifica prodotti tramite form
- Eliminazione prodotti con conferma
- Ricerca con filtri
- Configurazione connessione DB tramite `config.properties` (non hardcoded)

## Screenshots app
![Inizio](docs/img/login-demo.png)
*Pagina iniziale con login demo*

![Home](docs/img/home-page.png)
*Pagina home con barra di navigazione e filtri*

![Details](docs/img/view-product.png)
*Pagina detagli del prodotto*

![Lista Prodotti](docs/img/list-products.png)
*Tabella dei prodotti*

![Form Prodotto](docs/img/new-product.png)
*Form per aggiungere / modificare prodotto*

![Menu](docs/img/view-menu.png)
*Menu dei cataloghi*

## Configurare database MySQL (opzionale)
```sql
CREATE DATABASE tlacuache_beer;
CREATE USER 'tlacuache'@'localhost' IDENTIFIED BY '********';
GRANT ALL PRIVILEGES ON tlacuache_beer.* TO 'tlacuache'@'localhost';
FLUSH PRIVILEGES;
```

## Script SQL
### Esempio di importazione schema e dati
```bash
mysql -u tlacuache -p tlacuache_beer < docs/sql/schema.sql
mysql -u tlacuache -p tlacuache_beer < docs/sql/seed.sql
```

## Configurare credenziali

- Copiare il file di esempio:
```bash
cp src/main/resources/config.properties.example src/main/resources/config.properties
```
- Modificare `config.properties` con le proprie credenziali MySQL:
```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=tlacuache_beer
DB_USER=admin
DB_PASSWORD=root
```



## Autore
Emir Hernandez
- Progetto didattico - sviluppo di applicazioni web con Java

## Licenza
- Uso didattico / demo - utilizzo libero per fini accademici e di apprendimento