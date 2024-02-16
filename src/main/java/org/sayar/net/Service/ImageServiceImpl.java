package org.sayar.net.Service;

import org.sayar.net.Dao.ImageDao;
import org.sayar.net.Model.newModel.Image;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;
    @Override
    public Image save(Image image, String organId) {
        return imageDao.save(image,organId);
    }



    @Override
    public Image findOne(String id) {
        return imageDao.findOne(id);
    }

    @Override
    public boolean update(Image image) {
//        return imageDao.update(file);
        return false;
    }
}
