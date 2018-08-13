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

import br.com.controller.DownloadServiceThread;
import br.com.model.Conexao;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author mathe
 */
public class FXMLDownloadController implements Initializable {

    private static FXMLDownloadController controller;
    private Conexao con;
    private ChannelSftp sftpChannel;
    private Alert alert;
    
    @FXML
    private GridPane gridPaneDownloads;
    
    @FXML
    private Button btn_download;
    
    @FXML
    private TextArea txt_arquivos;

    @FXML
    private TextField txt_local = new TextField();

    @FXML
    private TextField txt_remoto = new TextField();
    
    @FXML
    private ProgressBar progress_download;
    
    @FXML
    private Text txt_progress;
    
    @FXML
    private Label lbl_download;
    
    @FXML
    private Label lbl_restantes;
    
    @FXML
    void handleDownloadAction(ActionEvent event) {   
        try {
            this.cdDiretorio();
            
            final DownloadServiceThread dst;
            dst = new DownloadServiceThread(btn_download, txt_local.getText(), sftpChannel, progress_download, txt_progress, txt_arquivos, lbl_download, lbl_restantes);
            
            //lbl_download.textProperty().bind(dst.messageProperty());
            //Here you tell your progress indicator is visible only when the service is runing
            //progress_download.progressProperty().bind(dst.progressProperty());
            dst.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String textoNaoEncontrados = dst.getValue();
                    txt_arquivos.setText("LOG de arquivos não encontrados \n" + 
                                         "------------------------------------------- \n" + 
                                            textoNaoEncontrados);
                    System.out.println("Execução finalizada com sucesso.");
                }
            });

            dst.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Execução finalizada com erros.");
                }
            });
            dst.start(); //here you start your service

        } catch (SftpException ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar abrir o diretório remoto.");
            alert.showAndWait();
        }        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
       
    public void start(Stage stage) throws Exception {        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDownload.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDownload.fxml"));
        Parent root = loader.load();
        FXMLDownloadController ctrl = (FXMLDownloadController)loader.getController();
        controller = ctrl;
        
        Scene scene =  new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();
        //stage.setTitle(con.toString());

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("\nFechando conexão com o servidor...");
                getController().sftpChannel.exit();
                getController().sftpChannel.disconnect();
                System.out.println("\nConexão fechada.");
                System.out.println("\nBye bye!");
                System.exit(0);
            }
        });

    }
    
    public static FXMLDownloadController getController(){
        return FXMLDownloadController.controller;
    }
    
    public void inicializaDados(Conexao con, ChannelSftp sftpChannel) {
        this.con = con;
        this.sftpChannel = sftpChannel;
        txt_local.setText(con.getDirLocal());
        txt_remoto.setText(con.getDirRemoto());
        Stage stage = (Stage) gridPaneDownloads.getScene().getWindow();
        stage.setTitle(con.toString());
    }

    public void cdDiretorio() throws SftpException{
        String dirRemoto = txt_remoto.getText();
        System.out.println("\nAbrindo diretório '" +dirRemoto+ "'");
        sftpChannel.cd(dirRemoto);
        System.out.println("\nDiretório remoto aberto.");
    }
}
