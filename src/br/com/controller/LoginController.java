/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.model.Conexao;
import java.net.URL;
import java.util.ResourceBundle;
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
public class LoginController implements Initializable {

    @FXML
    private ComboBox<Conexao> combo_config;
    
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
        // TODO
    }    
    
}
