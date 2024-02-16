package org.sayar.net.Tools;

public class MessageTemplate {

    private String templat="";
    public String build(){
        return templat;
    }

    public MessageTemplate add(String field,TagType tagType){
            this.templat += "<"+tagType+">"+field+"</"+tagType+"> \n";

        return this;
    }

    public MessageTemplate add(String field,String url){
        this.templat += "<"+ TagType.A+" href="+url+">"+field+"</"+ TagType.A+"> \n";
        return this;
    }


    public enum TagType{
        A,P,HTML;


        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
