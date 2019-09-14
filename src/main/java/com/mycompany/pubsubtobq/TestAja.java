/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author kenny
 */
public class TestAja {
     public static void main(String[] args) {
         byte[] data = {123,34,69,118,101,110 ,116 ,83 ,101 ,114 ,105 ,97 ,108 ,34 ,58 ,34 ,49 ,50 ,51 ,34 ,44 ,34 ,83 ,101 ,103 ,109 ,101 ,110 ,116 ,83 ,101 ,114 ,105 ,97 ,108 ,34 ,58 ,34 ,83 ,69 ,45 ,49 ,50 ,51 ,34 ,125};
         String payload = new String(data, StandardCharsets.UTF_8);
         Gson gson = new Gson();
         try{
             UserScore s = gson.fromJson(payload, UserScore.class);
             System.out.println(s);
         }catch(JsonSyntaxException e) {
             System.out.println("error "+e.getMessage());
         }
     }
}
