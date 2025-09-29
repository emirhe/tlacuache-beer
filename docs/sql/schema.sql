CREATE DATABASE IF NOT EXISTS tlacuache_beer;

USE tlacuache_beer;





-- prima creiamo le entità catalogo
CREATE TABLE stile(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(80) NOT NULL UNIQUE,
	descrizione VARCHAR(255)
);

CREATE TABLE produttore(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(150) NOT NULL,
	indirizzo VARCHAR(255),
	website VARCHAR(255)
);

CREATE TABLE formato(
	id INT AUTO_INCREMENT PRIMARY KEY,
	codice VARCHAR(20) NOT NULL UNIQUE
);

-- insert dei formati
INSERT INTO formato (codice) VALUES ('bottiglia'), ('lattina'), ('fusto');

CREATE TABLE origine(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(150)
);


-- creazioni di entità dei dati
CREATE TABLE birra(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(150) NOT NULL,
	stile_id INT NOT NULL,
	origine_id INT NOT NULL,
	produttore_id INT NOT NULL,
	gradazione DECIMAL(4,2),
	senza_glutine BOOLEAN,
	biologico BOOLEAN	
);

-- relazioni table birra
ALTER TABLE birra
ADD CONSTRAINT fk_birra_produttore
FOREIGN KEY (produttore_id) REFERENCES 
produttore(id);

ALTER TABLE birra
ADD CONSTRAINT fk_birra_origine
FOREIGN KEY (origine_id) REFERENCES 
origine(id);

ALTER TABLE birra
ADD CONSTRAINT fk_birra_stile
FOREIGN KEY (stile_id) REFERENCES 
stile(id);

-- creazioni di entità dei dati
CREATE TABLE prodotto(
	id INT AUTO_INCREMENT PRIMARY KEY,
	birra_id INT NOT NULL,
	sku VARCHAR(70) UNIQUE,
	formato_id INT NOT NULL,
	volume_ml INT UNSIGNED NOT NULL,
	pack_size INT UNSIGNED NOT NULL DEFAULT 1,
	prezzo DECIMAL(10,2) NOT NULL,
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	stock INT UNSIGNED NOT NULL DEFAULT 0,
	url_img VARCHAR(255) NULL
);

ALTER TABLE prodotto
ADD CONSTRAINT chk_prodotto_pack CHECK (pack_size IN (1,4,6,12,24));

-- relazioni della table prodotto

ALTER TABLE prodotto
ADD CONSTRAINT fk_prodotto_birra
FOREIGN KEY (birra_id) REFERENCES 
birra(id);

ALTER TABLE prodotto
ADD CONSTRAINT fk_prodotto_formato
FOREIGN KEY (formato_id) REFERENCES 
formato(id);





