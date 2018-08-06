/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.model;

/**
 *
 * @author matheusm
 */
public class Conexao {
    private String host;
    private int porta;
    private String usuario;
    private String senha;
    private String dirLocal;
    private String dirRemoto;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDirLocal() {
        return dirLocal;
    }

    public void setDirLocal(String dirLocal) {
        this.dirLocal = dirLocal;
    }

    public String getDirRemoto() {
        return dirRemoto;
    }

    public void setDirRemoto(String dirRemoto) {
        this.dirRemoto = dirRemoto;
    }

    @Override
    public String toString() {
        return "" + host + " > " + usuario;
    }
    
    
}
