package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.CertificationDao;
import org.sayar.net.Model.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CertificationDaoImp implements CertificationDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Certification createUserCertification(Certification certification) {
        return mongoOperations.save(certification);
    }

    @Override
    public List<Certification> getAllUserCertification() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Certification.class);
    }

    @Override
    public List<Certification> getOneCertification(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoOperations.find(query, Certification.class);
    }

    @Override
    public Certification getAnyCertification(String certificationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(certificationId));
        return mongoOperations.findOne(query, Certification.class);
    }

    @Override
    public UpdateResult updateCertification(Certification certification) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(certification.getId()));
        Update update = new Update();
        update.set("name", certification.getName());
        update.set("description", certification.getDescription());
        update.set("validFrom", certification.getValidFrom());
        update.set("validFor", certification.getValidFor());
        update.set("certificateDocumentList", certification.getCertificateDocumentList());
        return mongoOperations.updateFirst(query, update, Certification.class);
    }

    @Override
    public List<Certification> getUsersCertification(String userId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        query.fields().include("name");
        return mongoOperations.find(query,Certification.class);
    }
}
