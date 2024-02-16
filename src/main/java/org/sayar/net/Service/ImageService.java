package org.sayar.net.Service;

import org.sayar.net.Model.newModel.Image;

public interface ImageService {
    public Image save(Image image, String organId);
    public Image findOne(String id);
    public boolean update(Image image);
}
