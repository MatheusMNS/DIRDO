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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
        btn_download.setDisable(true);
        String[] arquivos = txt_arquivos.getText().split("\\n");
        try {
            this.cdDiretorio();
            
            for(int i = 0 ; i < arquivos.length; i++){
                lbl_restantes.setText(""+i+"/"+arquivos.length);
                lbl_download.setText(arquivos[i]);
                this.fazDownload(arquivos[i], i, arquivos.length);
                txt_progress.setText("0%");
                progress_download.setProgress(0.0F);
            }

        } catch (SftpException ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar abrir o diretório remoto.");
            alert.showAndWait();
        }
        
        lbl_download.setText("NENHUM");
        lbl_restantes.setText("0/0");
        btn_download.setDisable(false);
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
        sftpChannel.cd(dirRemoto);
        System.out.println("\nDiretório remoto aberto.");
    }
    
    public void fazDownload(String arquivo, int restantes, int total){
        final DownloadServiceThread dst;
        dst = new DownloadServiceThread(arquivo, txt_local.getText(), sftpChannel, progress_download, txt_progress);

        //Here you tell your progress indicator is visible only when the service is runing
        //progress_download.progressProperty().bind(dst.progressProperty());
        dst.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("Status: OK");
            }
        });

        dst.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("Status: ERRO");
            }
        });
        dst.start(); //here you start your service

    }
}
