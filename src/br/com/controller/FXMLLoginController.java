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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import com.jcraft.jsch.ChannelSftp;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * FXML Controller class
 *
 * @author matheusm
 */
public class FXMLLoginController implements Initializable {

    private List<Conexao> conList = new ArrayList<Conexao>();
    private ObservableList<Conexao> obConexoes;
    private Conexao conexao;
    
    private ChannelSftp sftpChannel;
    
    private Alert alert;
    
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
    
    @FXML
    private ProgressIndicator progress_conectar;
    
    @FXML
    private Text txt_conectar;
    
    @FXML
    void handleComboAction(ActionEvent event) {
        int index = combo_config.getSelectionModel().getSelectedIndex();
        
        //progress_conectar.setVisible(false);
        
        conexao = conList.get(index);
        txt_host.setText(conexao.getHost());
        txt_porta.setText(Integer.toString(conexao.getPorta()));
        txt_usuario.setText(conexao.getUsuario());
        txt_senha.setText(conexao.getSenha());
    }
    
    @FXML
    void handleConectarAction(ActionEvent event) {
        //boolean conectar; 
        progress_conectar.setVisible(true);
        progress_conectar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        //conectar = conectaServidor();
        btn_conectar.setDisable(true);
        txt_conectar.setText("Aguarde...");
        final LoginServiceThread lst;
        lst = new LoginServiceThread(txt_host.getText(), Integer.parseInt(txt_porta.getText()), txt_usuario.getText(), txt_senha.getText());

        //Here you tell your progress indicator is visible only when the service is runing
        //progress_conectar.visibleProperty().bind(lst.runningProperty());
        lst.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                progress_conectar.setVisible(false);
                txt_conectar.setText("");
                sftpChannel = lst.getValue();   //here you get the return value of your service
                System.out.println(sftpChannel.toString());
            }
        });

        lst.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                progress_conectar.setVisible(false);
                txt_conectar.setText("");
                
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao tentar ao conectar com o servidor.");
                alert.setContentText("Por favor, verifique se as informações de conexão estão corretas e tente novamente.");
                alert.showAndWait();
                btn_conectar.setDisable(false);
                txt_conectar.setText("");
            }
        });
        lst.start(); //here you start your service
    }

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
