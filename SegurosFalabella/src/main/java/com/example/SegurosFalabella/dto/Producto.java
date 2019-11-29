package com.example.SegurosFalabella.dto;

public class Producto {
    private Integer id;
    private String nombreProducto;
    private Integer sellIn;
    private Integer price;

    public Producto(Integer id, Integer sellIn, String nombreProducto, Integer price) {
        this.id = id;
        this.sellIn = sellIn;
        this.nombreProducto = nombreProducto;
        this.price = price;
    }



    public String getNombreProducto() {
        return nombreProducto;
    }
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getSellIn() {
        return sellIn;
    }
    public void setSellIn(int sellIn) {
        this.sellIn = sellIn;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getid() {
        return id;
    }
    public void setid(Integer id) {
        this.id = id;
    }
}
