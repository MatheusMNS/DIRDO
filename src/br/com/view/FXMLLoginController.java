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
package br.com.view;

import br.com.controller.ConexaoController;
import br.com.controller.LoginServiceThread;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import com.jcraft.jsch.ChannelSftp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author matheusm
 */
public class FXMLLoginController implements Initializable {

    private List<Conexao> conList = new ArrayList<Conexao>();
    private ObservableList<Conexao> obConexoes;
    private Conexao conexao = null;
    
    private FXMLDownloadController downloadScene;
    
    private ChannelSftp sftpChannel;
    
    private Alert alert;
    
    @FXML
    private ComboBox<Conexao> combo_config;

    @FXML
    private TextField txt_host;
    
    @FXML
    private TextField txt_porta;

    @FXML
    private TextField txt_usuario;

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
        
        conexao = conList.get(index);
        txt_host.setText(conexao.getHost());
        txt_porta.setText(Integer.toString(conexao.getPorta()));
        txt_usuario.setText(conexao.getUsuario());
        txt_senha.setText(conexao.getSenha());
        
        this.preencheConexao();
    }
    
    @FXML
    void handleConectarAction(ActionEvent event) {
        if(txt_host.getText().equals("") || txt_porta.getText().equals("") || txt_usuario.getText().equals("") || txt_senha.getText().equals("") ||
            txt_host.getText().equals(" ") || txt_porta.getText().equals(" ") || txt_usuario.getText().equals(" ") || txt_senha.getText().equals(" ")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Um ou mais campos estão preenchidos incorretamente.");
            alert.setContentText("Por favor, verifique se as informações de conexão estão corretas e tente novamente.");
            alert.showAndWait();
        }
        else{
            progress_conectar.setVisible(true);
            progress_conectar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            btn_conectar.setDisable(true);
            txt_conectar.setText("Aguarde...");
            this.preencheConexao(); // Verifica se o objeto Conexao possui valores, caso não, insere novos valores
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

                    // Abrindo a janela de downloads com o sucesso da conexão
                    downloadScene = new FXMLDownloadController();

                    Stage stage = (Stage) btn_conectar.getScene().getWindow();
                    try {
                        downloadScene.start(stage);
                        downloadScene.getController().inicializaDados(conexao, sftpChannel);
                    } catch (Exception ex) {
                        Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
    
    public void preencheConexao(){
        if (conexao == null){
            conexao.setDirLocal("");
            conexao.setDirRemoto("");
            conexao.setHost(txt_host.getText());
            conexao.setPorta(Integer.parseInt(txt_porta.getText()));
            conexao.setSenha(txt_senha.getText());
            conexao.setUsuario(txt_usuario.getText());
        }
        else{
            int index = combo_config.getSelectionModel().getSelectedIndex();
            conexao = conList.get(index);
        }
    }
}
