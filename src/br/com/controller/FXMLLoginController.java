/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.model.Conexao;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author matheusm
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private ComboBox<Conexao> combo_config;
    
    private List<Conexao> conList = new ArrayList<Conexao>();
    private ObservableList<Conexao> obConexoes;
    
    @FXML
    private TextField txt_porta;

    @FXML
    private TextField txt_usuario;

    @FXML
    private TextField txt_host;

    @FXML
    private TextField txt_senha;

    @FXML
    private Button btn_conectar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.carregaConexoes();
    }    
    
    public void carregaConexoes(){
        ConexaoController con = new ConexaoController();
        conList = con.leConexoes();
        obConexoes = FXCollections.observableArrayList(conList);
        combo_config.setItems(obConexoes);
    }
}
