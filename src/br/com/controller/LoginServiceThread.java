/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

/**
 *
 * @author matheusm
 */
public class LoginServiceThread extends Service<ChannelSftp>{
    
    private JSch jsch;
    private Session session;
    private ChannelSftp sftpChannel;
    
    private String host;
    private int porta;
    private String usuario;
    private String senha;
    
    private Alert alert;
    
    public LoginServiceThread(String host, int porta, String usuario, String senha) {
        this.host = host;
        this.porta = porta;
        this.usuario = usuario;
        this.senha = senha;
    }
    
   @Override
    protected Task<ChannelSftp> createTask() {
        return new Task<ChannelSftp>() {
            @Override
            protected ChannelSftp call() throws Exception {
                
                try {
                    jsch = new JSch();
                    System.out.println("Estabelecendo a Conexão...");
                    session = jsch.getSession(usuario, host, porta);
                    session.setPassword(senha);
                    session.setConfig("StrictHostKeyChecking", "no");

                    session.connect();
                    System.out.println("Conexão estabelecia.");
                    System.out.println("Criando Canal SFTP...");
                    sftpChannel = (ChannelSftp) session.openChannel("sftp");
                    sftpChannel.connect();
                    System.out.println("Canal SFTP criado.");
                    
                    return sftpChannel;
                }
                catch(Exception e){
                    System.err.print(e);
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Erro ao tentar ao conectar com o servidor.");
                    alert.setContentText("Por favor, verifique se as informações de conexão estão corretas e tente novamente.");
                    alert.showAndWait();
                    
                    return null;
                }
                
            }
        };
    } 
}
