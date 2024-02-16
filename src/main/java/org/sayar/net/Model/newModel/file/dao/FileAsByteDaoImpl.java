package org.sayar.net.Model.newModel.file.dao;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.file.domain.FileAsByte;
import org.sayar.net.Model.newModel.file.dto.IdSetterDTO;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileAsByteDaoImpl extends GeneralDaoImpl<FileAsByte> implements FileAsByteDao {
    public FileAsByteDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }



    @Override
    public FileAsByte create(FileAsByte fileAsByte) {
        this.save(fileAsByte);
        return fileAsByte;
    }
    @Override
    public boolean deleteOne(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        this.remove(query, FileAsByte.class);
        return true;
    }



    public FileAsByte getOne(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        return this.findOne(query, FileAsByte.class);
    }


    public List<FileAsByte> getAllUser(String uploaderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uploaderId").is(uploaderId));

        return this.find(query, FileAsByte.class);
    }

    @Override
    public boolean categoryIdSetter(IdSetterDTO dto) {

        Query query =new Query();
        query.addCriteria(Criteria.where("id").in(dto.getImageIdList()));
        Update update =new Update();
        update.set(IdSetterDTO.FN.categoryId.toString(),dto.getCategoryId());
        this.updateMulti(query,update,FileAsByte.class);
        return false;
    }
}
