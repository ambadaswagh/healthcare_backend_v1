
package com.healthcare.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Created by Johnny on 2/23/17.
*/
public class MD5hash {
    public static  String MD5Encrypt(String value) throws NoSuchAlgorithmException{
       
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return  sb.toString();
    }
    
    
}
