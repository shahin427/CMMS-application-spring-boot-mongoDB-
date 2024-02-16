package org.sayar.net.Tools;

import java.io.File;

public class DirectoryManager {

//    private  static final String PROJECT_DIR =System.getProperty("user.dir");
    private  static final String PROJECT_DIR ="/root";
    private static final String MAIN_DIR = PROJECT_DIR+"/UPLOAD_DIR2";
    private static final String ORGAN_DIR = MAIN_DIR + "/ORGANIZATION_DIR";


    public static  String getStaticDir(){
        return PROJECT_DIR+"/src/main/resources/static/DefaultCategoryImg";
    }
    public static String getMainDir(){
        return MAIN_DIR;
    }

    public static boolean createMainDir(){
        return createDirectory(MAIN_DIR) &&
                createDirectory(ORGAN_DIR);
    }



    public static boolean createOrganDirectory(long organId){
        return createDirectory(ORGAN_DIR+"/"+organId)&&
//                createDirectory(ORGAN_DIR+"/"+organId+"/ITEM_FILE")&&
//                createDirectory(ORGAN_DIR+"/"+organId+"/USER_FILE")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/IMAGE_FILE")&& //use for file file only

                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE/PDF")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE/ZIP")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE/MP3")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE/MP4")&&
                createDirectory(ORGAN_DIR+"/"+organId+"/OTHER_FILE/OTHER_EXTENSION");
    }

    public static String organDirectoryPath(String organId){

        return ORGAN_DIR+"/"+organId;
    }

    public static String userFileDirectoryPath(String organId){

        return ORGAN_DIR+"/"+organId+"/IMAGE_FILE";
    }

    public static String itemFileDirectoryPath(String organId){

        return ORGAN_DIR+"/"+organId+"/IMAGE_FILE";
    }

    public static String imageFileDirectoryPath(String organId){

        return ORGAN_DIR+"/"+organId+"/IMAGE_FILE";

    }

    public static String otherFileDirectoryPath(String organId){

        return ORGAN_DIR+"/"+organId+"/OTHER_FILE";
    }

    private static boolean createDirectory(String path){

        File file = new File(path);

        if (file.exists()){
            return false;
        }else {
            file.mkdir();
            return true;
        }
    }

    public static String getFilePathWhitContentType(String contentType,String organId,String fileName){
        switch (contentType){
            case "application/pdf":{
                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/PDF/"+fileName;
            }
            case "audio/mp3":{
                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/MP3/"+fileName;

            }
            case "video/mp4":{
                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/MP4/"+fileName;

            }
            case "application/zip":{
                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/ZIP/"+fileName;
            }
            case "application/x-rar-compressed":{

                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/ZIP/"+fileName;
            }
            default:{
                return DirectoryManager.organDirectoryPath(organId)+"/OTHER_FILE/OTHER_EXTENSION/"+fileName;
            }
        }
    }

}
