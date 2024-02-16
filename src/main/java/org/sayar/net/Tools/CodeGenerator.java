package org.sayar.net.Tools;

import java.util.Random;

public class CodeGenerator {

    public static String generate(){

        String code="";
        Random rnd = new Random();
        code += String.valueOf(rnd.nextInt(9)+1);


        for (int i = 0; i <4 ; i++) {
            code += String.valueOf(rnd.nextInt(10));
        }

        return code;
    }
}
