package org.sayar.net.Model.newModel.purchase.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.sayar.net.Model.newModel.Purchasing.PurchaseShipping;
import org.sayar.net.Model.newModel.purchase.DTO.PurchaseDTO;
import org.sayar.net.Model.newModel.purchase.DTO.PurchaseShippingDTO;
import org.sayar.net.Model.newModel.purchase.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@Transactional
public class PurchaseDaoImpl extends GeneralDaoImpl<Purchase> implements PurchaseDao {
    public PurchaseDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<PurchaseDTO> getByIdLIst(List<String> idList) {
        ProjectionOperation pOp = project()
                .andExpression("id").as("id")
                .andExpression("price").as("price")
                .andExpression("deliverDate").as("deliverDate")
                .andExpression("purchaseDate").as("purchaseDate")
                .andExpression("currency").as("currency")
                .andExpression("organId").as("organId");

        Aggregation aggregation = newAggregation(
                Aggregation.match(Criteria.where("id").in(idList)),
                pOp
        );
        AggregationResults<PurchaseDTO> results =
                this.aggregate(aggregation, "purchase", PurchaseDTO.class);
        return results.getMappedResults();

    }

    @Override
    public Object getOnePurchase(String id) {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(Purchase.FN.id.toString()).is(id)),
                Aggregation.lookup("company", "shipping.companyId", "_id", "company"),
                Aggregation.lookup("asset", "shipping.assetId", "_id", "asset")
                ,unwind("company")
                ,unwind("asset")
                ,Aggregation.project()
                        .and("company._id").as("companyId")
                        .and("company.name").as("companyName")
                        .and("company.phoneNumber").as("companyPhoneNumber")
                        .and("company.address").as("companyAddress")
                        .and("asset._id").as("assetId")
                        .and("asset.title").as("assetTitle")
                        .and("asset.code").as("assetCode")
        );

//               Print.print(mongoOperations.aggregate(agg, Purchase.class, Object.class).getUniqueMappedResult());

        return mongoOperations.aggregate(agg, Purchase.class, PurchaseShippingDTO.class).getMappedResults();
    }

    @Override
    public boolean postOnePurchase(Purchase purchase) {
        mongoOperations.save(purchase);
        return true;
    }

    @Override
    public boolean postOnePurchaseShipping(String purchaseId, PurchaseShipping purchaseShipping) {

        Query query = new Query();
        query.addCriteria(Criteria.where(Purchase.FN.id.toString()).is(purchaseId));
        Update update = new Update();
        update.set("shipping", purchaseShipping);
        mongoOperations.updateFirst(query, update, Purchase.class);
        return true;
    }

    @Override
    public UpdateResult editOnePurchase(String purchaseId, Purchase purchase) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Purchase.FN.id.toString()).is(purchaseId));
        mongoOperations.save(purchase);
        return null;
    }

    @Override
    public List<Purchase> getAllPurchasePlanning(Purchase purchase) {
        Query query = new Query();
        query.fields().include("purchaseAssetDTO").include("requester").include("requiredQuantity")
                .include("unitPrice").include("totalCost").include("supplier").include("status")
                .include("deleted");
        query.addCriteria(Criteria.where("deleted").ne(true));
        List<Purchase> purchasePlanning = mongoOperations.find(query, Purchase.class);
        return purchasePlanning;
    }

    @Override
    public boolean addAdditionalCost(String purchaseId, AdditionalCost additionalCost) {

        Query query = new Query();
        query.addCriteria(Criteria.where(Purchase.FN.id.toString()).is(purchaseId));
        Update update = new Update();
        update.push("additionalCost", additionalCost);
        if (mongoOperations.updateFirst(query, update, Purchase.class) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UpdateResult editAdditionalCost(String purchaseId, String addCostId, AdditionalCost additionalCost) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Purchase.FN.id.toString()).is(purchaseId));
        query.addCriteria(Criteria.where(Purchase.FN.additionalCost.toString()).elemMatch(
                Criteria.where(AdditionalCost.FN.ID.toString()).is(addCostId)
                )
        );
        Update update1 = new Update();
        update1.pull(Purchase.FN.additionalCost.toString(), new BasicDBObject("id", addCostId));

        Query query1 = new Query();
        query1.addCriteria(Criteria.where(Purchase.FN.id.toString()).is(purchaseId));
        query1.addCriteria(Criteria.where(Purchase.FN.additionalCost.toString()).is(addCostId));
        Update update2 = new Update();
        update2.push(Purchase.FN.additionalCost.toString(), additionalCost);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update2, Purchase.class);
        return updateResult;

    }

}
