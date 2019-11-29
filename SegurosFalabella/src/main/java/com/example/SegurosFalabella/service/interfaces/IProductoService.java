package com.example.SegurosFalabella.service.interfaces;

import java.util.List;

import com.example.SegurosFalabella.dto.Producto;


public interface IProductoService {

    String evaluarProductos(Integer day);

    List<Producto> mostrarProductosVendidos();

    Producto venderProducto(Integer id);


}
