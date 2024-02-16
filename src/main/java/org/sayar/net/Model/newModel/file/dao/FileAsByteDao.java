package org.sayar.net.Model.newModel.file.dao;

import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.file.domain.FileAsByte;
import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;

import java.util.List;

public interface FileAsByteDao extends GeneralDao<FileAsByte> {
     boolean deleteOne(String id);
     FileAsByte create(FileAsByte fileAsByte);
     FileAsByte getOne(String id);
    List<FileAsByte> getAllUser(String uploaderId);

    boolean categoryIdSetter(IdSetterDTO dto) throws Exception;
}
