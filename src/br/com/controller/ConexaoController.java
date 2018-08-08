/*
 *    BSD 2-Clause License
 *    
 *    Copyright (c) 2018, MatheusMNS
 *    All rights reserved.
 *
 *    Redistribution and use in source and binary forms, with or without
 *    modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *
 *    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
