/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 *
 * @author matheusm
 */
public class DownloadServiceThread extends Service<String>{

    private String arquivo;
    private String dirLocal;
    private ChannelSftp sftpChannel;
    private SftpProgressMonitor monitor;

    @FXML
    private ProgressBar progress_download;
    
    @FXML
    private Text txt_progress;


    public DownloadServiceThread(String arquivo, String dirLocal, ChannelSftp sftpChannel, ProgressBar progress_download, Text txt_progress) {
        this.arquivo = arquivo;
        this.dirLocal = dirLocal;
        this.sftpChannel = sftpChannel;
        this.progress_download = progress_download;
        this.txt_progress = txt_progress;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                monitor = new DownloadProgresso(progress_download, txt_progress);
                try{
                    sftpChannel.get(arquivo, dirLocal, monitor);
                } catch(SftpException se){
                    System.out.println("ARQUIVO NÃO ENCONTRADO");    
                }
                
                return "PRONTO";
            }
        };
    }
    
}
