package org.sayar.net.Tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Print {
    public static void print(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            System.out.println(
                            "\n\n\n\n\n\n\n*****************< " + object.getClass().getName() + " >*****************\n" +
                            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object) +
                            "\n*****************</ " + object.getClass().getName() + " >*****************\n\n\n\n\n\n"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void print(String label, Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(label + " : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}