package org.sayar.net.Dao;

import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentDaoImpl implements DocumentDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<DocumentFile> getAllByExtraId(String extraId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("extraId").is(extraId));
        query.fields().exclude("fileByte");
        return mongoOperations.find(query, DocumentFile.class);
    }

    @Override
    public List<DocumentFile> getAllByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        query.fields().exclude("fileByte");
        return mongoOperations.find(query, DocumentFile.class);
    }

    @Override
    public List<DocumentFile> getAllDocumentsOfWorkOrderByExtraId(List<String> documentsList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(documentsList));
        query.fields().exclude("fileByte");
        return mongoOperations.find(query, DocumentFile.class);
    }

    @Override
    public List<DocumentFile> getDocumentsOfRepository(List<String> documentsList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(documentsList));
        query.fields().exclude("fileByte");
        return mongoOperations.find(query, DocumentFile.class);
    }
}
