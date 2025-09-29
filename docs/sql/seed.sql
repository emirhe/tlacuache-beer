USE tlacuache_beer;

START TRANSACTION;

-- === 1) Cataloghi ===
INSERT INTO stile (nome, descrizione) VALUES
 ('IPA','India Pale Ale, luppolata e aromatica'),
 ('Stout','Scura, tostada'),
 ('Pilsner','Rubia, lager, amara limpia'),
 ('Pale Ale','Maltosa y cítrica'),
 ('Witbier','Trigo belga, especiada'),
 ('Lager','Fermentazione bassa, limpia');

INSERT INTO produttore (nome, indirizzo) VALUES
 ('Tlacuache Brewing','Oaxaca, MX'),
 ('Cervecería de Colima','Colima, MX'),
 ('Sierra Nevada Brewing','Chico, CA, USA'),
 ('BrewDog','Ellon, UK'),
 ('Baladin','Piozzo, IT');

INSERT INTO origine (nome) VALUES
 ('Messico'), ('USA'), ('Regno Unito'), ('Italia'), ('Germania'), ('Belgio'), ('Rep. Ceca');

-- Formato 
INSERT INTO formato (codice) VALUES
 ('bottiglia'), ('lattina'), ('fusto');

-- Birra
INSERT INTO birra (nome, stile_id, produttore_id, gradazione, senza_glutine, biologico, origine_id)
VALUES
 ('Tlacuache IPA',
   (SELECT id FROM stile WHERE nome='IPA'),
   (SELECT id FROM produttore WHERE nome='Tlacuache Brewing'),
   6.5, FALSE, TRUE, (SELECT id FROM origine WHERE nome='Messico'));

INSERT INTO birra (nome, stile_id, produttore_id, gradazione, senza_glutine, biologico, origine_id)
VALUES
 ('Tlacuache Stout',
   (SELECT id FROM stile WHERE nome='Stout'),
   (SELECT id FROM produttore WHERE nome='Tlacuache Brewing'),
   5.8, FALSE, FALSE, (SELECT id FROM origine WHERE nome='Messico'));


-- === 3) Prodotti  ===

-- Tlacuache IPA
INSERT INTO prodotto (birra_id, sku, formato_id, volume_ml, pack_size, prezzo, is_active, stock, url_img)
VALUES
 ((SELECT id FROM birra WHERE nome='Tlacuache IPA'),
  'TLA-IPA-330-BOT-P1', (SELECT id FROM formato WHERE codice='bottiglia'),
  330, 1, 59.90, TRUE, 120, NULL),
 ((SELECT id FROM birra WHERE nome='Tlacuache IPA'),
  'TLA-IPA-330-CAN-P6', (SELECT id FROM formato WHERE codice='lattina'),
  330, 6, 329.00, TRUE, 45, NULL);

-- Tlacuache Stout
INSERT INTO prodotto (birra_id, sku, formato_id, volume_ml, pack_size, prezzo, is_active, stock, url_img)
VALUES
 ((SELECT id FROM birra WHERE nome='Tlacuache Stout'),
  'TLA-STO-500-BOT-P1', (SELECT id FROM formato WHERE codice='bottiglia'),
  500, 1, 69.90, TRUE, 60, NULL),
 ((SELECT id FROM birra WHERE nome='Tlacuache Stout'),
  'TLA-STO-20000-FUS-P1', (SELECT id FROM formato WHERE codice='fusto'),
  20000, 1, 1899.00, TRUE, 5, NULL);


COMMIT;
