package org.sayar.net.Dao;

import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Tools.Base64Converter;
import org.sayar.net.Tools.DirectoryManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Repository
@Transactional
public class ImageDaoImpl implements ImageDao {

//    @Autowired
//    private SessionFactory sessionFactory;


//    @Autowired
//    private Base64Converter base64Converter;

    @Override
    public Image save(Image image, String organId) {
//        try {

        if (image == null) return null;
        else if (image.getImageData() == null) return null;
        else {

            String imageData = image.getImageData();
            image.setImageData("");
//                getCurrentSession().save(file);

            Base64Converter.createImageFile(imageData, image.getId(), image.getExtension(), organId);
            String imageUrl = "download/file/" + organId + "/" + image.getId();
            image.setImageData(imageUrl);
            return image;
        }
//        } catch (Exception e) {
//            return null;
//        }
    }

    @Override
    public boolean delete(Image image, String organId) {


        String path = DirectoryManager.imageFileDirectoryPath(organId) + "/" + image.getId() + image.getExtension();
        File imageFile = new File(path);
        imageFile.delete();

//        getCurrentSession().delete(
//                getCurrentSession().merge(file)
//        );
        return true;
    }


    @Override
    public Image findOne(String id) {
        return null;
//        return (Image) getCurrentSession().get(Image.class, id);
    }

    @Override
    public Image update(Image image, String organId) {

        if (image == null) return null;
        else if (image.getImageData() == null) return null;
        else {

            String imageData = image.getImageData();
            image.setImageData("");
//            getCurrentSession().update(file);

            Base64Converter.createImageFile(imageData, image.getId(), image.getExtension(), organId);
            String imageUrl = "download/file/" + image.getId();// set file data => file url because client can download file !!!
            image.setImageData(imageUrl);
            return image;
        }
    }


//    private Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }


}
