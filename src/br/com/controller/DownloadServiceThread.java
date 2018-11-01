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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 *
 * @author matheusm
 */
public class DownloadServiceThread extends Service<String>{

    private String[] arquivos;
    private String dirLocal;
    private ChannelSftp sftpChannel;
    private SftpProgressMonitor monitor; // Não recebe no construtor

    @FXML
    private ProgressBar progress_download;
    
    @FXML
    private Text txt_progress;

    @FXML
    private final Label lbl_download;
    
    @FXML
    private final Label lbl_restantes;
    
    @FXML
    private Button btn_download;

    public DownloadServiceThread(Button btn_download, String dirLocal, ChannelSftp sftpChannel, ProgressBar progress_download, Text txt_progress, TextArea txt_arquivos, Label lbl_download, Label lbl_restantes) {
        this.btn_download = btn_download;
        this.dirLocal = dirLocal;
        this.sftpChannel = sftpChannel;
        this.progress_download = progress_download;
        this.txt_progress = txt_progress;
        this.arquivos = txt_arquivos.getText().split("\\n");
        this.lbl_download = lbl_download;
        this.lbl_restantes = lbl_restantes;
    }
 
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                String naoEncontrados = "";
                btn_download.setDisable(true);
                monitor = new DownloadProgresso(progress_download, txt_progress);
                for(int i = 0 ; i < arquivos.length; i++){

                    updateLabel(lbl_download, arquivos[i]);
                    updateLabel(lbl_restantes, ""+(i+1)+"/"+arquivos.length);
                    
                    try{
                        sftpChannel.get(arquivos[i], dirLocal, monitor);
                    } catch(SftpException se){
                        naoEncontrados = naoEncontrados + arquivos[i] + "\n";
                    }
                    
                    txt_progress.setText("0%");
                    progress_download.setProgress(0.0F);
                    updateLabel(lbl_download, "NENHUM");
                    updateLabel(lbl_restantes, "0/0");
                }
                btn_download.setDisable(false);
                return naoEncontrados;
            }
        };
    }
    
    void updateLabel(final Label label, final String text) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                label.setText(text);
            }
        });
    }
}
