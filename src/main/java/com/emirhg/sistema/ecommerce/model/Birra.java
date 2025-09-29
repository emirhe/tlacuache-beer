
package com.emirhg.sistema.ecommerce.model;

import com.emirhg.sistema.ecommerce.model.cataloghi.Origine;
import com.emirhg.sistema.ecommerce.model.cataloghi.Stile;
import com.emirhg.sistema.ecommerce.model.cataloghi.Produttore;


public class Birra {
    
    private int id;
    private String nome;
    private Stile stile;
    private Origine origine;
    private Produttore produttore;
    private double gradazione;
    private boolean senzaGlutine;
    private boolean biologico;

    public Birra() {
    }

    public Birra(int id, String nome, Stile stile, Origine origine, Produttore produttore, double gradazione, boolean senzaGlutine, boolean biologico) {
        this.id = id;
        this.nome = nome;
        this.stile = stile;
        this.origine = origine;
        this.produttore = produttore;
        this.gradazione = gradazione;
        this.senzaGlutine = senzaGlutine;
        this.biologico = biologico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stile getStile() {
        return stile;
    }

    public void setStile(Stile stile) {
        this.stile = stile;
    }

    public Origine getOrigine() {
        return origine;
    }

    public void setOrigine(Origine origine) {
        this.origine = origine;
    }

    public Produttore getProduttore() {
        return produttore;
    }

    public void setProduttore(Produttore produttore) {
        this.produttore = produttore;
    }

    public double getGradazione() {
        return gradazione;
    }

    public void setGradazione(double gradazione) {
        this.gradazione = gradazione;
    }

    public boolean isSenzaGlutine() {
        return senzaGlutine;
    }

    public void setSenzaGlutine(boolean senza_glutine) {
        this.senzaGlutine = senza_glutine;
    }

    public boolean isBiologico() {
        return biologico;
    }

    public void setBiologico(boolean biologico) {
        this.biologico = biologico;
    }

   
    
    
    
    
}
