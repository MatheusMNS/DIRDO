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

import com.jcraft.jsch.SftpProgressMonitor;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 *
 * @author matheusm
 */
public class DownloadProgresso implements SftpProgressMonitor{

    private long percent = -1;
    private ProgressBar progress_download;
    private Text txt_progress;
    long count=0;
    long max=0;
    
    public DownloadProgresso(ProgressBar progress_download, Text txt_progress){
        this.progress_download = progress_download;
        this.txt_progress = txt_progress;
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
        txt_progress.setText(""+percent+"%");
     
      return true;
    }
    
    public void end(){
      
    }
    
}
