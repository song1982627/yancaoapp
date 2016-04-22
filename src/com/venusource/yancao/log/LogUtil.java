package com.venusource.yancao.log;

import java.io.File;
import java.io.FileOutputStream;

import android.util.Log;
public class LogUtil {
    private final static  String fileName = "/sdcard/shoujilog.txt";  
      
    public static void PrintTextLog(String log) {  
    	
    	try {   
      
            File file = new File(fileName);   
            if (!file.exists()) {
            	file.createNewFile();
            }
           
            FileOutputStream stream = new FileOutputStream(file,true);   
          
            byte[] buf = (log+"\r\n").getBytes();   
            stream.write(buf);            
           
            stream.close();   
               
        } catch(Exception e) {   
            e.printStackTrace();   
        }   
    }
}  

