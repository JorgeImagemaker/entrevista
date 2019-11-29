package com.example.SegurosFalabella.repository;

import com.example.SegurosFalabella.dto.Producto;
import com.example.SegurosFalabella.repository.interfaces.IProductoRepository;

import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepository implements IProductoRepository {

    //lista los Productos vendidos
    @Override
    public List<Producto> listarProductosVendidos() {
        File file = new File("./src/main/resources/xml/ProductosVendidos.xml");
        List<Producto> listProductos = new ArrayList<Producto>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nList = doc.getElementsByTagName("producto");
            for(int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Producto producto = new Producto(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()),Integer.parseInt(eElement.getElementsByTagName("sellIn").item(0).getTextContent()),eElement.getElementsByTagName("nombre").item(0).getTextContent(),Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent()));
                    listProductos.add(producto);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return listProductos;
    }

    //lista los productos que se tienen a la venta
    @Override
    public List<Producto> listarProductos() {
        File file = new File("./src/main/resources/xml/Productos.xml");
        List<Producto> listProductos = new ArrayList<Producto>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList nList = doc.getElementsByTagName("producto");
            for(int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Producto producto = new Producto(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()),Integer.parseInt(eElement.getElementsByTagName("sellIn").item(0).getTextContent()),eElement.getElementsByTagName("nombre").item(0).getTextContent(),Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent()));
                    listProductos.add(producto);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return listProductos;
    }

    //esta funcion nos permite guardar los productos vendidos en un archivo xml
    @Override
    public void addProductoVendido (Producto vender) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse("./src/main/resources/xml/ProductosVendidos.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NodeList list = document.getElementsByTagName("producto");

        Element root = document.getDocumentElement();
        Element rootElement = document.getDocumentElement();

        List<Producto> prod = new ArrayList<Producto>();
        prod.add(new Producto(vender.getid(),vender.getSellIn(), vender.getNombreProducto(), vender.getPrice()));

        for (Producto i : prod) {
            Element producto = document.createElement("producto");
            rootElement.appendChild(producto);

            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(Integer.toString(i.getid())));
            producto.appendChild(id);

            Element nombre = document.createElement("nombre");
            nombre.appendChild(document.createTextNode(i.getNombreProducto()));
            producto.appendChild(nombre);

            Element sellIn = document.createElement("sellIn");
            sellIn.appendChild(document.createTextNode(Integer.toString(i.getSellIn())));
            producto.appendChild(sellIn);

            Element price = document.createElement("price");
            price.appendChild(document.createTextNode(Integer.toString(i.getPrice())));
            producto.appendChild(price);

            root.appendChild(producto);
        }

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        StreamResult result = new StreamResult("./src/main/resources/xml/ProductosVendidos.xml");
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();

        }
    }

}
