package org.sayar.net.Model.Mongo.poll.enums;

/**
 * Created by sina on 2/9/17.
 */
public enum Gender {
    MALE,
    FEMALE;



    @Override
    public String toString() {
        switch(this){
            case MALE :
                return "MALE";
            case FEMALE :
                return "FEMALE";
        }
        return null;
    }
}
