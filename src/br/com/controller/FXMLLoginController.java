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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author matheusm
 */
public class FXMLLoginController implements Initializable {

    private List<Conexao> conList = new ArrayList<Conexao>();
    private ObservableList<Conexao> obConexoes;
    private Conexao conexao;
    
    @FXML
    private ComboBox<Conexao> combo_config;

    @FXML
    private TextField txt_porta;

    @FXML
    private TextField txt_usuario;

    @FXML
    private TextField txt_host;

    @FXML
    private PasswordField txt_senha;

    @FXML
    private Button btn_conectar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.carregaConexoes();
        
        combo_config.setOnAction(e -> {
            int index = combo_config.getSelectionModel().getSelectedIndex();
            conexao = conList.get(index);
            txt_host.setText(conexao.getHost());
            txt_porta.setText(Integer.toString(conexao.getPorta()));
            txt_usuario.setText(conexao.getUsuario());
            txt_senha.setText(conexao.getSenha());
        });
    }    
        
    public void carregaConexoes(){
        ConexaoController con = new ConexaoController();
        conList = con.leConexoes();
        obConexoes = FXCollections.observableArrayList(conList);
        combo_config.setItems(obConexoes);
    }
}
