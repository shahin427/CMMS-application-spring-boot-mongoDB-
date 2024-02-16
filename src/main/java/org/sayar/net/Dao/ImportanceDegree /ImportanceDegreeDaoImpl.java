package org.sayar.net.Dao.ImportanceDegree;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.ImportanceDegree;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;


@Repository("ImportanceDegreeDaoImpl")
public class ImportanceDegreeDaoImpl extends GeneralDaoImpl<ImportanceDegree> implements org.sayar.net.Dao.ImportanceDegree.ImportanceDegreeDao {

    @Autowired
    private MongoOperations mongoOperations;

    public ImportanceDegreeDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }



    @Override
    public ImportanceDegree createImportanceDegree(ImportanceDegree entity) {
        return mongoOperations.save(entity);
    }

    @Override
    public ImportanceDegree getOneImportanceDegree(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoOperations.findOne(query, ImportanceDegree.class);
    }

    @Override
    public Boolean updateImportanceDegree(ImportanceDegree userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userType.getId()));
        Update update = new Update();
        update.set("name", userType.getName());
        UpdateResult res= mongoOperations.updateFirst(query, update, ImportanceDegree.class);
        if(res!=null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Boolean deleteImportanceDegree(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        DeleteResult res= mongoOperations.remove(query, ImportanceDegree.class);
        if(res!=null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Boolean checkedUsed(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("importanceDegreeId").is(id));
       return mongoOperations.exists(query, WorkOrderSchedule.class);
    }

    @Override
    public List<ImportanceDegree> getAllType() {
        Query query = new Query();
        return mongoOperations.find(query, ImportanceDegree.class);
    }

    @Override
    public Page<ImportanceDegree> getPage(String term, Pageable pageable, Integer total) {
        Criteria criteria = new Criteria();
        if (term !=null &&!term.isEmpty()) {
            criteria.and("name").regex(term);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skip(pageable.getPageNumber() * pageable.getPageSize())
                , limit(pageable.getPageSize())
        );
        AggregationResults<ImportanceDegree> groupResults
                = super.aggregate(agg, ImportanceDegree.class, ImportanceDegree.class);
        List<ImportanceDegree> result = groupResults.getMappedResults();
        if (total <= 0) {
            Aggregation aggCount = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<ImportanceDegree> groupResultsCount
                    = super.aggregate(aggCount, ImportanceDegree.class, ImportanceDegree.class);
            List<ImportanceDegree> resultcount = groupResultsCount.getMappedResults();
            total = resultcount.size();
        }
        Page<ImportanceDegree> res = new PageImpl<ImportanceDegree>(result, pageable, total);
        return res;
    }


}
