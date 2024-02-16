package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.PartACR;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartACRDaoImp implements PartACRDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public PartACR savePartACR(PartACR partACR) {
        return mongoOperations.save(partACR);
    }

    @Override
    public List<PartACR> getAllPartACR(String partId, Pageable pageable, Integer totalElement) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partId));
        query.with(pageable);
        return mongoOperations.find(query, PartACR.class);
    }

    @Override
    public PartACR getOnePartACR(String ACRID) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(ACRID));
        return mongoOperations.findOne(query, PartACR.class);
    }

    @Override
    public UpdateResult updatePartACR(PartACR partACR) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partACR.getId()));
        Update update = new Update();
        update.set("id", partACR.getId());
        update.set("assetName", partACR.getAssetName());
        update.set("assetCode", partACR.getAssetCode());
        update.set("quantity", partACR.getQuantity());
        update.set("partId", partACR.getPartId());
        return mongoOperations.updateFirst(query, update, partACR.getClass());
    }

    @Override
    public boolean checkIfPartACRRegistered(PartACR partACR) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partACR.getPartId()));
        query.addCriteria(Criteria.where("assetCode").is(partACR.getAssetCode()));
        return mongoOperations.exists(query, PartACR.class);
    }

    @Override
    public long countPartACRs(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partId));
        return mongoOperations.count(query, PartACR.class);
    }
}
