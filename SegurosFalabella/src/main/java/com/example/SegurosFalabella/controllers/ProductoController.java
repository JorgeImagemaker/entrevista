package com.example.SegurosFalabella.controllers;

import com.example.SegurosFalabella.dto.Producto;
import com.example.SegurosFalabella.service.interfaces.IProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluateProducts")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    //simula el comportamiento de los productos vendidos segun Dia(s) ingresados
    @RequestMapping(value = "/{day}", method = RequestMethod.GET)
    public String getEvaluarProductos(@PathVariable Integer day){
        return productoService.evaluarProductos(day);
    }
    //muestra los productos vendidos, extrayendolo del archivo ProductosVendidos.xml
    @RequestMapping(value = "/mostrar", method = RequestMethod.GET)
    public List<Producto> getMostrarProductosVendidos(){
        return productoService.mostrarProductosVendidos();
    }
    //agrega un producto al archivo ProductosVendidos.xml segun id
    @RequestMapping(value = "/producto/{id}", method = RequestMethod.GET)
    public Producto setVenderProducto(@PathVariable Integer id){
        return productoService.venderProducto(id);
    }


}
