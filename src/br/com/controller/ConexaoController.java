/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.model.Conexao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author matheusm
 */
public class ConexaoController {
    
    public List<Conexao> leConexoes(){
        String filePath = "config.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("conexao");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Conexao> conList = new ArrayList<Conexao>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                conList.add(getConexao(nodeList.item(i)));
            }

            return conList;
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
        
        return null;
    }
    
     private static Conexao getConexao(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Conexao con = new Conexao();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            con.setHost(getTagValue("host", element));
            con.setPorta(Integer.parseInt(getTagValue("porta", element)));
            con.setUsuario(getTagValue("usuario", element));
            con.setSenha(getTagValue("senha", element));
            con.setDirLocal(getTagValue("dirLocal", element));
            con.setDirRemoto(getTagValue("dirRemoto", element));
        }

        return con;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
