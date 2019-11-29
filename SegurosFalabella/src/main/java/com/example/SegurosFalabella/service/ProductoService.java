package com.example.SegurosFalabella.service;

import com.example.SegurosFalabella.dto.Producto;
import com.example.SegurosFalabella.repository.interfaces.IProductoRepository;
import com.example.SegurosFalabella.service.interfaces.IProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository iProductoRepository;



    @Override
    public String evaluarProductos(Integer day) {
        List<Producto> listProductos = new ArrayList<Producto>();
        listProductos = AplicarReglas(iProductoRepository.listarProductosVendidos(), day);
        String listSalida=formatearSalida(listProductos, day);
        return listSalida;
    }

    @Override
    public List<Producto> mostrarProductosVendidos() {
        List<Producto> listProductos = new ArrayList<Producto>();
        listProductos = iProductoRepository.listarProductosVendidos();
        return listProductos;
    }

    @Override
    public Producto venderProducto(Integer id) {
        List<Producto> listProductos = new ArrayList<Producto>();
        listProductos = iProductoRepository.listarProductos();
        Producto producto  = buscarProducto(listProductos, id);
        iProductoRepository.addProductoVendido(producto);
        return producto;
    }

    public String formatearSalida (List<Producto> listaProductos, Integer day){

        String cadena="";
        int vendidosPorDia = listaProductos.size()/day;

        int j=0;
        for(int i=0; i<day;i++){
            cadena = cadena+"-------- dia "+i +" --------\n";
            cadena = cadena + "nombre, sellIn, price\n";

            for(j=vendidosPorDia * i;j<(vendidosPorDia * (i+1)); j++){
                cadena= cadena + listaProductos.get(j).getNombreProducto()+", "+listaProductos.get(j).getSellIn()+", "+listaProductos.get(j).getPrice()+"\n";
            }
        }
        return cadena;
    }

    public Producto buscarProducto (List<Producto> listaProductos, Integer id){
        Producto productoTemp = null;
        for (Producto producto : listaProductos) {
            if(producto.getid()==id){
                productoTemp = new Producto(producto.getid(),producto.getSellIn(),producto.getNombreProducto(),producto.getPrice());
            }
        }
        return productoTemp;
    }

    public List<Producto> AplicarReglas (List<Producto> listaProductos, int day) {
        List<Producto> listaTemporal = new ArrayList<Producto>();
        for(int i=0;i<day;i++) {


            if(i==0){
                //si es el dia cero
                for (Producto producto : listaProductos) {
                    Producto productoTemp = new Producto(producto.getid(),producto.getSellIn(),producto.getNombreProducto(),producto.getPrice());
                    listaTemporal.add(productoTemp);
                }
            }else{
                //si es mayor a 1 dia
                int j=0;
                int degradarProducto=1;
                for (Producto producto : listaProductos) {
                    Producto productoTemp = new Producto(producto.getid(),producto.getSellIn(),producto.getNombreProducto(),producto.getPrice());

                    //disminuye el sellin y el precio en 1 por dia
                    productoTemp.setSellIn(productoTemp.getSellIn()-1);
                    //sellIn < 0 , los precios de cada producto, se degradan el doble de rapido.
                    if(productoTemp.getSellIn()<0){
                        degradarProducto = 2;
                    }
                    if(!productoTemp.getNombreProducto().equals("Super avance")){
                        if((productoTemp.getPrice()-1*degradarProducto)>=0){
                            productoTemp.setPrice(productoTemp.getPrice()-1*degradarProducto);
                        }
                    }else{
                        //El producto "Super avance" dismuniye su precio el doble de rapido que un producto normal.
                        if((productoTemp.getPrice()-2*degradarProducto)<=100){
                            productoTemp.setPrice(productoTemp.getPrice()-2*degradarProducto);
                        }
                    }

                    //el producto "Full cobertura" incrementa su precio a medida que pasa el tiempo.
                    if (productoTemp.getNombreProducto().equals("Full cobertura")) {
                        if(productoTemp.getPrice()+1<=100){
                            productoTemp.setPrice(productoTemp.getPrice()+1);
                        }
                    }

                    //el producto "Mega cobertura", nunca vence para vender y nunca disminuye su precio.
                    if (productoTemp.getNombreProducto().equals("Mega cobertura")) {
                        productoTemp.setPrice(producto.getPrice());
                    }

                    // el producto "Full cobertura Super duper", tal como el
                    // "Full Cobertura- El precio se incrementa en 2 cuando quedan 10 dias o menos y se incrementa en 3,
                    // cuando quedan 5 dias o menos.
                    if (productoTemp.getNombreProducto().equals("Full cobertura") || productoTemp.getNombreProducto().equals("Full cobertura Super duper")) {
                        if(productoTemp.getSellIn()<=10 && productoTemp.getSellIn()>5){
                            if((productoTemp.getPrice()+2)<100){
                                productoTemp.setPrice(productoTemp.getPrice()+2);
                            }else {
                                productoTemp.setPrice(100);
                            }
                        }
                        if(productoTemp.getSellIn()<=5 && productoTemp.getSellIn()>=0){
                            if((productoTemp.getPrice()+3)<100){
                                productoTemp.setPrice(productoTemp.getPrice()+3);
                            }else {
                                productoTemp.setPrice(100);
                            }
                        }
                        //el precio disminuye a 0 cuando se vence el tiempo de venta.
                        if(producto.getSellIn()<=0){
                            productoTemp.setPrice(0);
                        }
                    }
                    listaProductos.set(j, productoTemp);
                    listaTemporal.add(productoTemp);
                    j++;
                }
            }
        }
        return listaTemporal;
    }


}

