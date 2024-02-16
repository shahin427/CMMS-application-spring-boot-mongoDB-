package org.sayar.net.Dao.NewDao;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.DTO.BudgetDTO;
import org.sayar.net.Model.DTO.BudgetWithCurrencyDTO;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BudgetDaoImpl extends GeneralDaoImpl<Budget> implements BudgetDao {
    public BudgetDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    public Budget saeBudget(Budget budget) {
        return mongoOperations.save(budget);
    }

    @Override
    public Budget updateBudget(Budget budget) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("id").is(budget.getId()),
                        Criteria.where("deleted").ne(true)
                )
        );
        Update update = new Update();
        update.set("title", budget.getTitle());
        update.set("description", budget.getDescription());
        update.set("code", budget.getCode());
        update.set("budgetAmount", budget.getBudgetAmount());
        update.set("currencyId", budget.getCurrencyId());
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.returnNew(true);
        return mongoOperations.findAndModify(query, update, options, Budget.class);
    }

    @Override
    public boolean checkIfCodeExist(Budget budget) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("code").is(budget.getCode()),
                        Criteria.where("deleted").ne(true),
                        Criteria.where("id").ne(budget.getId())
                )
        );
        Budget budget1 = mongoOperations.findOne(query, Budget.class);
        if (budget1 == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<BudgetWithCurrencyDTO> findAllBudget(BudgetDTO budgetDTO, Pageable pageable, Integer totalElement) {
        long skip = pageable.getPageNumber() * pageable.getPageSize();

        Criteria criteria1 = new Criteria();
        criteria1.and("deleted").ne(true);
        criteria1.and("title").ne(null);

        Criteria criteria = new Criteria();
        if (budgetDTO.getFinalBudgetAmount() != null && budgetDTO.getPrimaryBudgetAmount() == null) {
            criteria = Criteria.where("budgetAmount").lte(budgetDTO.getFinalBudgetAmount());
        }
        if (budgetDTO.getPrimaryBudgetAmount() != null && budgetDTO.getFinalBudgetAmount() == null) {
            criteria = Criteria.where("budgetAmount").gte(budgetDTO.getPrimaryBudgetAmount());
        }
        if (budgetDTO.getPrimaryBudgetAmount() != null && budgetDTO.getFinalBudgetAmount() != null) {
            criteria = new Criteria().andOperator(Criteria.where("budgetAmount").lte(budgetDTO.getFinalBudgetAmount()),
                    Criteria.where("budgetAmount").gte(budgetDTO.getPrimaryBudgetAmount()));
        }
        if (budgetDTO.getTitle() != null && !budgetDTO.getTitle().equals("")) {
            criteria.and("title").regex(budgetDTO.getTitle());
        }
        if (budgetDTO.getCode() != null && !budgetDTO.getCode().equals("")) {
            criteria.and("code").regex(budgetDTO.getCode());
        }
        if (budgetDTO.getCurrency() != null && !budgetDTO.getCurrency().equals("")) {
            criteria.and("currencyId").is(budgetDTO.getCurrency());
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria1),
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("code").as("code")
                        .and("budgetAmount").as("budgetAmount")
                        .and(ConvertOperators.ToObjectId.toObjectId("$currencyId")).as("currencyId"),
                Aggregation.lookup("currency", "currencyId", "_id", "currency"),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("code").as("code")
                        .and("budgetAmount").as("budgetAmount")
                        .and("currency._id").as("currencyId")
                        .and("currency.title").as("currencyName"),
                Aggregation.skip(skip),
                Aggregation.limit(pageable.getPageSize())
        );
        System.out.println("agg.toString() = " + agg.toString());
        return mongoOperations.aggregate(agg, Budget.class, BudgetWithCurrencyDTO.class).getMappedResults();
    }

    @Override
    public long     getAllCount(BudgetDTO budgetDTO) {

        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();
        criteria1.and("deleted").ne(true);
        criteria1.and("title").ne(null);

        if (budgetDTO.getFinalBudgetAmount() != null && budgetDTO.getPrimaryBudgetAmount() == null) {
            criteria = Criteria.where("budgetAmount").lte(budgetDTO.getFinalBudgetAmount());
        }
        if (budgetDTO.getPrimaryBudgetAmount() != null && budgetDTO.getFinalBudgetAmount() == null) {
            criteria = Criteria.where("budgetAmount").gte(budgetDTO.getPrimaryBudgetAmount());
        }
        if (budgetDTO.getPrimaryBudgetAmount() != null && budgetDTO.getFinalBudgetAmount() != null) {
            criteria = new Criteria().andOperator(Criteria.where("budgetAmount").lte(budgetDTO.getFinalBudgetAmount()),
                    Criteria.where("budgetAmount").gte(budgetDTO.getPrimaryBudgetAmount()));
        }
        if (budgetDTO.getTitle() != null && !budgetDTO.getTitle().equals("")) {
            criteria.and("title").regex(budgetDTO.getTitle());
        }

        if (budgetDTO.getCode() != null && !budgetDTO.getCode().equals("")) {
            criteria.and("code").regex(budgetDTO.getCode());
        }
        if (budgetDTO.getCurrency() != null && !budgetDTO.getCurrency().equals("")) {
            criteria.and("currencyId").is(budgetDTO.getCurrency());
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria1),
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(agg, Budget.class, Budget.class).getMappedResults().size();
    }

    @Override
    public List<Budget> findAllBudgetByFilterAndPagination(String name, Pageable pageable) {
        return null;
    }

    @Override
    public boolean deleteBudget(String budgetId) {
        Query query = new Query();
        Budget budget = mongoOperations.findOne(query, Budget.class);
        if (budget != null) {
            budget.setDeleted(true);
            mongoOperations.save(budget);
            return true;
        } else {
            System.out.println("Not_deleted");
            return false;
        }

    }

    @Override
    public List<Budget> getAllBudget() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").ne(null));
        query.fields()
                .include("title");
        return mongoOperations.find(query, Budget.class);
    }

    @Override
    public boolean ifCurrencyExistsInBudget(String currencyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("currency.id").is(currencyId));
        return mongoOperations.exists(query, Budget.class);
    }

    @Override
    public boolean checkIfBudgetCodeIsUnique(String code) {
        Print.print("codeeeee", code);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, Budget.class);
    }

    @Override
    public Budget getBudgetTitle(String budgetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(budgetId));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.findOne(query, Budget.class);
    }

    @Override
    public BudgetWithCurrencyDTO findOneBudget(String id) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(id);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("code").as("code")
                        .and("budgetAmount").as("budgetAmount")
                        .and(ConvertOperators.ToObjectId.toObjectId("$currencyId")).as("currencyId"),
                Aggregation.lookup("currency", "currencyId", "_id", "currency"),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("code").as("code")
                        .and("budgetAmount").as("budgetAmount")
                        .and("currency._id").as("currencyId")
                        .and("currency.name").as("currencyName")
        );
        return mongoOperations.aggregate(aggregation, Budget.class, BudgetWithCurrencyDTO.class).getUniqueMappedResult();
    }
}
