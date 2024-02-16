package org.sayar.net.Model.Mongo.poll.enums;

public enum PrivatePublish {
    INVALID,
    VALID,
    ALL;


    @Override
    public String toString() {
        switch(this){
            case INVALID :
                return "INVALID";
            case VALID :
                return "VALID";
            case ALL :
                return "ALL";
        }
        return null;
    }
}
