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

import br.com.model.Conexao;
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
