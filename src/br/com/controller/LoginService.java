/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author matheusm
 */
public class LoginService extends Service<String>{
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                //DO YOU HARD STUFF HERE
                String res = "toto";
                Thread.sleep(5000);
                return res;
            }
        };
    }
}
