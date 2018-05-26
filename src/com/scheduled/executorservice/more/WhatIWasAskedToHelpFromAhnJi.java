package com.scheduled.executorservice.more;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WhatIWasAskedToHelpFromAhnJi {
   public static void main(String[] args) {
      WhatIWasAskedToHelpFromAhnJi exec = new WhatIWasAskedToHelpFromAhnJi();
      exec.exec();
   }
   
   public void exec() {
      
      BufferedReader br = null;
      Process p = null;
      String line = null;
      
      try {
			// 지은이코드
			// String[] cmdArr = { "/bin/sh", "-c", "touch /home/proobject3/proobject/pfm/deploy_application/*/META-INF/proframe-application.xml"};
			// String[] cmdArr2 = { "/bin/sh", "-c", "ls -lrt /home/proobject3/proobject/pfm/deploy_application/*/META-INF/proframe-application.xml"};
			// p = Runtime.getRuntime().exec(cmdArr);
			// p.waitFor();
			// p = Runtime.getRuntime().exec(cmdArr2);
			// p.waitFor();
			// /지은이 코드
    	  
         //3번째 배열 데이터에 proframe-application.xml 위치를 지정해줘야 한다.
         //WAS/PSGSCM01/APPL/proframe/pfm/deploy_application
         String[] cmdArr ={
                         "/bin/sh"
                          ,"-c"   
                          ,"touch /WAS/PSGSCM01/APPL/proframe/pfm/deploy_application/*/META-INF/proframe-application.xml"
                          };
         
         p = Runtime.getRuntime().exec(cmdArr);
         p.waitFor();
         
         //3번째 배열 데이터에 proframe-application.xml 위치를 지정해줘야 한다.
         String[] cmdArr2= {
               "/bin/sh"
                 ,"-c"   
                 ,"ls -lrt /WAS/PSGSCM01/APPL/proframe/pfm/deploy_application/*/META-INF/proframe-application.xml"
         };
         
         p = Runtime.getRuntime().exec(cmdArr2);
         p.waitFor();
         
         br = new BufferedReader(new InputStreamReader(p.getInputStream()));
         while((line = br.readLine()) != null){
              System.out.println(line);
          }
          
      } catch (Exception e) {
         // TODO: handle exception
         e.printStackTrace();
      }finally {
         try {
            
            if(br != null) {
               br.close();
            }
            p.destroy();
         } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
         }
         
         
      }
   
   }
}