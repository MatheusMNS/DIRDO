/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField txt_local = new TextField();

    @FXML
    private TextField txt_remoto = new TextField();
    
    @FXML
    private ProgressBar progress_download;
    
    @FXML
    void handleDownloadAction(ActionEvent event) {
        try {
            this.cdDiretorio();
            System.out.println("botao Download: "+sftpChannel);
            this.fazDownload();
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
        stage.show();
//        stage.setTitle(con.toString());

    }
    
    public static FXMLDownloadController getController(){
        return FXMLDownloadController.controller;
    }
    
    public void inicializaDados(Conexao con, ChannelSftp sftpChannel) {
        this.con = con;
        this.sftpChannel = sftpChannel;
        txt_local.setText(con.getDirLocal());
        txt_remoto.setText(con.getDirRemoto());
    }

    public void cdDiretorio() throws SftpException{
        String dirRemoto = txt_remoto.getText();
        System.out.println("\nAbrindo diretório '" +dirRemoto+ "'");
        System.out.println("CD diretorio: "+sftpChannel);
        sftpChannel.cd(dirRemoto);
        System.out.println("\nDiretório remoto aberto.");
    }
    
    public void fazDownload(){
        final DownloadServiceThread dst;
        dst = new DownloadServiceThread("miub24-180805-033539-04-63307862.BATCH.004", con.getDirLocal(), sftpChannel, progress_download);

        //Here you tell your progress indicator is visible only when the service is runing
        //progress_download.progressProperty().bind(dst.progressProperty());
        dst.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("DEU CERTO");
            }
        });

        dst.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("DEU ERRADO");
            }
        });
        dst.start(); //here you start your service

    }
}
