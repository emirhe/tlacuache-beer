
package com.emirhg.sistema.ecommerce.dao;


public class Test {
    public static void main(String[] args) throws Exception{
        
          
    var dao = new com.emirhg.sistema.ecommerce.dao.jdbc.ProdottoDao();
    var list = dao.findAll(); // o findAllActive()
    System.out.println("Prodotti = " + list.size());
    if (!list.isEmpty()) {
      var p = list.get(0);
      System.out.println("First: id=" + p.getId() + " sku=" + p.getSku());
    }
  }
        
        
    }

