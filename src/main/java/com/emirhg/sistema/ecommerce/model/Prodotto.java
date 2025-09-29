
package com.emirhg.sistema.ecommerce.model;

import com.emirhg.sistema.ecommerce.model.cataloghi.Formato;


public class Prodotto {
    
    private int id;
    private Birra birra;
    private String sku;
    private Formato formato;
    private int volumeMl;
    private int packSize;
    private double prezzo;
    private boolean active; 
    private int stock;
    private String urlImg;

    public Prodotto() {
    }

    public Prodotto(int id, Birra birra, String sku, Formato formato, int volumeMl, int packSize, double prezzo, boolean active, int stock, String urlImg) {
        this.id = id;
        this.birra = birra;
        this.sku = sku;
        this.formato = formato;
        this.volumeMl = volumeMl;
        this.packSize = packSize;
        this.prezzo = prezzo;
        this.active = active;
        this.stock = stock;
        this.urlImg = urlImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Birra getBirra() {
        return birra;
    }

    public void setBirra(Birra birra) {
        this.birra = birra;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public int getPackSize() {
        return packSize;
    }

    public void setPackSize(int packSize) {
        this.packSize = packSize;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

   
    
    
    
    
}
