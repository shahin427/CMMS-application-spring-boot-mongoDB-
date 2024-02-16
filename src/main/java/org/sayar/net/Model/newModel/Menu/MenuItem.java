package org.sayar.net.Model.newModel.Menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//import javax.persistence.*;

@Data
@NoArgsConstructor
//@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"id"})
public class MenuItem {

//    @Column(columnDefinition = "VARCHAR(256) COLLATE utf8_persian_ci")
    private String title;
    private String link;
    private String icon;
    private List<SubItem> children;

    public MenuItem(String title, String link, String icon) {
        this.title = title;
        this.link = link;
        this.icon = icon;
    }

    public MenuItem(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public static MenuItem createMenuItem(String simbule){
        MenuItem menuItem;

        switch (simbule){
            case "a":{

                menuItem = new MenuItem("صفحه اصلی","/pages/dashboard","nb-home");
                break;
            }case "j":{

                menuItem = new MenuItem("کارتابل کاربری","/inbox","nb-person");
                break;
            }

            case "b":{

                menuItem = new MenuItem("مدیریت کاربری","nb-person");

                break;
            }

            case "c":{

                menuItem = new MenuItem(" تعمیرات و نگهداری","ion-social-dropbox-outline");

                break;
            }

            case "d":{
                menuItem = new MenuItem(" اطلاعات پایه","ion-ios-information-outline");

                break;
            }

            case "e":{
                menuItem = new MenuItem("لیست دارایی ها","/item/listAsset/0","ion-social-dropbox-outline");

                break;
            }

            case "f":{
                menuItem = new MenuItem(" قطعات","ion-social-dropbox-outline");

                break;
            }

            case "g":{
                menuItem = new MenuItem("پیام رسانی","/message/listMessage","ion-ios-people-outline");

                break;
            }
            case "x":{

                menuItem = new MenuItem("فرم ساز","nb-home");
                break;
            }
            case "y":{

                menuItem = new MenuItem("فرایند ساز","nb-home");
                break;
            }

            case "h":{

                menuItem = new MenuItem("درخواست خرید","/purchasing/listPurchasingPlanning","ion-ios-people-outline");
                break;
            }case "k":{

                menuItem = new MenuItem("وظایف من","/userTask/list","ion-ios-people-outline");
                break;
            }
//            case "i":{
//
//                menuItem = new MenuItem("فرم ساز","/formBuilder","ion-ios-people-outline");
//                break;
//            }
            default:{
                menuItem =new MenuItem();
            }
        }
        return menuItem;
    }
}
