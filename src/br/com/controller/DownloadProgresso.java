/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import com.jcraft.jsch.SftpProgressMonitor;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author matheusm
 */
public class DownloadProgresso implements SftpProgressMonitor{

    private long percent = -1;
    private ProgressBar progress_download;
    long count=0;
    long max=0;
    
    public DownloadProgresso(ProgressBar progress_download){
        this.progress_download = progress_download;
    }
    
    public void init(int op, String src, String dest, long max){
      this.max = max;
      count = 0;
      percent = -1;
      this.count(count);
    }
    
    public boolean count(long count){
      this.count += count;

      if(percent >= this.count*100/max){ 
          return true; 
      }

      percent = this.count*100/max;

      //monitor.setNote("Completed "+this.count+"("+percent+"%) out of "+max+".");     
      //monitor.setProgress((int)this.count);
      // este -> aux = lines[lines.length] + "" + percent + "% --- " + df.format((float)this.count/1000000) + " Mb de " + df.format((float)max/1000000) + "Mb";
      // este -> lines[lines.length] = aux;
      //System.out.println(percent + "% --- " + df.format((float)this.count/1000000) + " Mb de " + df.format((float)max/1000000) + "Mb");

     // este ->  _txt_status.setText(lines.toString());

     //_lbl_progresso.setText(percent + "% --- " + df.format((float)this.count/1000000) + " Mb de " + df.format((float)max/1000000) + "Mb");
     progress_download.setProgress((double)percent/100);
        System.out.println("porcentagem: "+percent);
     
      return true;
    }
    
    public void end(){
      
    }
    
}
