package org.sayar.net.Tools;


import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class Base64Converter {


    public static String createImageFile(String imageData, String imageId, String extension, String organId) {

        String url = DirectoryManager.imageFileDirectoryPath(organId) + "/" + imageId + extension;

//        try {
        fileCreature(imageData, url);
//        }catch (Exception e){

//        }
        return url;
    }

    public static String createUserImageFileFromBase64(String base64, String organid, String userName, String extention) {

        String url = DirectoryManager.userFileDirectoryPath(organid) + "/" + userName + extention;
        try {
            fileCreature(base64, url);
        } catch (Exception e) {
        }

        return url;

    }

//    public static String createItemImageFileFromBase64( Item item){
//
//        String url = DirectoryManager.itemFileDirectoryPath(
//                item.getOrganization().getId())
//                +"/"+item.getTitle()+"("+item.getId()+")" +item.getImage().getExtension();
//        try {
//
//
//            fileCreature(
//                    item.getImage().getImageData(),
//                    url
//            );
//
//        }catch (Exception e){ }
//
//         return url;
//    }

    private static boolean fileCreature(String base64, String path) {

        if (base64 == null) {
            return false;
        }
        try (FileOutputStream imageOutFile = new FileOutputStream(path)) {

            byte[] imageByte = Base64.getDecoder().decode(new String(base64).getBytes("UTF-8"));
            imageOutFile.write(imageByte);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException ioe) {
            return false;
        }

    }


    public static String encodeImage(String filePath) throws IOException {
        File file = new File(filePath);
        Path path = Paths.get(file.getAbsolutePath());
        byte[] image = Files.readAllBytes(path);
        return new String(Base64.getEncoder().encode(image), "UTF-8");
    }


    public static boolean base64Validation(String base64) {

        System.out.println(base64);

        System.out.println(!base64.contains("download/file/") && !base64.contains("d/file/"));

        String base64Regex="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$";

//        return !base64.contains("download/file/") && !base64.contains("d/file/");
        return base64.matches(base64Regex);
    }

}
