package org.sayar.net.Model.newModel.Enum;

public enum ShippingType {
    g(1),s(2),h(3);

    ShippingType(int n) {

    }

    public static ShippingType create(int n){
        switch (n){
            case 1:{
                return g;

            }
            case 2:{
                return s;

            }
            case 3:{
                return h;

            }
            default:{
                return null;
            }
        }
    }
}
