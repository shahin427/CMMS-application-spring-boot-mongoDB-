package org.sayar.net.Dao;

import org.sayar.net.Model.newModel.Image;

public interface ImageDao {

    public Image save(Image image, String organId);
    public boolean delete(Image image, String organId);
    public Image findOne(String id);
    public Image update(Image image, String organId);


}
