package com.example.SegurosFalabella.repository.interfaces;

import java.util.List;

import com.example.SegurosFalabella.dto.Producto;


public interface IProductoRepository {

    List<Producto> listarProductosVendidos();


    List<Producto> listarProductos();

    public void addProductoVendido (Producto vender);

}
