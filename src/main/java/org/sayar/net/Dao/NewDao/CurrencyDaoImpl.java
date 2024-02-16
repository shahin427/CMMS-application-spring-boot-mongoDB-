package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.CurrencyDTO;
import org.sayar.net.Model.newModel.Currency;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class CurrencyDaoImpl extends GeneralDaoImpl<Currency> implements CurrencyDao {
    @Autowired
    private MongoOperations mongoOperations;

    public CurrencyDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    public Currency saveCurrency(Currency currency) throws ApiRepetitiveException {
        return mongoOperations.save(currency);
    }

    public Currency updateCurrency(Currency currency) throws ApiRepetitiveException {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(currency.getId()));
        Update update = new Update();
        update.set("title", currency.getTitle());
        update.set("isoCode", currency.getIsoCode());
        update.set("isoCode", currency.getIsoCode());
        mongoOperations.updateFirst(query, update, Currency.class);
        return currency;
    }

    @Override
    public boolean checkIfCurrencyIsUnique(String isoCode, String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(title));
        query.addCriteria(Criteria.where("isoCode").is(isoCode));
        return mongoOperations.exists(query, Currency.class);
    }

    @Override
    public List<Currency> getAllCurrency() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").ne(null));
        return mongoOperations.find(query, Currency.class);
    }

    @Override
    public List<Currency> getAllByPagination(CurrencyDTO currencyDTO, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("title").ne(null);

        if (currencyDTO.getIsoCode() != null && !currencyDTO.getIsoCode().equals("")) {
            criteria.and("isoCode").regex(currencyDTO.getIsoCode());
        }

        if (currencyDTO.getTitle() != null && !currencyDTO.getTitle().equals("")) {
            criteria.and("title").regex(currencyDTO.getTitle());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );

        return mongoOperations.aggregate(aggregation, Currency.class, Currency.class).getMappedResults();
    }

    @Override
    public long getAllCount(CurrencyDTO currencyDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("title").ne(null);

        if (currencyDTO.getIsoCode() != null && !currencyDTO.getIsoCode().equals("")) {
            criteria.and("isoCode").regex(currencyDTO.getIsoCode());
        }

        if (currencyDTO.getTitle() != null && !currencyDTO.getTitle().equals("")) {
            criteria.and("title").regex(currencyDTO.getTitle());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );

        return mongoOperations.aggregate(aggregation, Currency.class, Currency.class).getMappedResults().size();
    }

    @Override
    public boolean checkIfTitleAndIsoCodeExist(CurrencyDTO currencyDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(currencyDTO.getTitle()));
        query.addCriteria(Criteria.where("isoCode").is(currencyDTO.getIsoCode()));
        return mongoOperations.exists(query, Currency.class);
    }
}

