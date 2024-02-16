package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.CompanyDTO;
import org.sayar.net.Model.DTO.CompanyGetOneDTO;
import org.sayar.net.Model.DTO.CompanySearchDTO;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository("companyDaoImpl")
@Transactional
public class CompanyDaoImpl extends GeneralDaoImpl<Company> implements CompanyDao {
    @Autowired
    private TokenPrinciple tokenPrinciple;
    @Autowired
    private MongoOperations mongoOperations;

    public CompanyDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<Node> getCompanyTreeNode() {
        return null;
    }

    @Override
    public List<CompanyDTO> getCompressAll() {
        ProjectionOperation projection = project()
                .andExpression("id").as("id")
                .andExpression("name").as("name")
                .andExpression("phoneNumber").as("phoneNumber")
                .andExpression("email").as("email")
                .andExpression("type").as("type")
                .andExpression("Currency.title").as("currencyTitle");
        Aggregation aggregation = Aggregation.newAggregation(
                projection
        );
        AggregationResults<CompanyDTO> aggregationResults = this.aggregate(
                aggregation, "company", CompanyDTO.class
        );
        return aggregationResults.getMappedResults();
    }


    public List<Node> getCompanyTreeNodeSearch(String searchTerm) {
        return null;
    }

    @Override
    public List<Company> getSupplier(String organId) {

//        return getCurrentSession().createCriteria(Company.class,"business")
//                .add(Restrictions.eq("business.organId",organId))
//                .add(Restrictions.eq("business.type", BusinessType.supplier))
//                .list();
        return null;
    }

    @Override
    public List<CompanyDTO> getByIdList(List<String> companyIdList) {
        ProjectionOperation projection = project()
                .andExpression("id").as("id")
                .andExpression("name").as("name")
                .andExpression("phoneNumber").as("phoneNumber")
                .andExpression("email").as("email")
                .andExpression("type").as("type");
        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("id").in(companyIdList)),
                projection
        );
        AggregationResults<CompanyDTO> aggregationResults = this.aggregate(
                aggregation, "company", CompanyDTO.class
        );
        return aggregationResults.getMappedResults();
    }

    @Override
    public Company postOne(Company company) throws ApiRepetitiveException {
        return mongoOperations.save(company);
    }

    @Override
    public Company updateCompany(Company company) {
        if (company.getCurrencyId().equals("")) {
            company.setCurrencyId(null);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(company.getId()));
        Update update = new Update();
        update.set("name", company.getName());
        update.set("code", company.getCode());
        update.set("phoneNumber", company.getPhoneNumber());
        update.set("email", company.getEmail());
        update.set("webSite", company.getWebSite());
        update.set("fax", company.getFax());
        update.set("description", company.getDescription());
        update.set("type", company.getType());
        update.set("address", company.getAddress());
        update.set("currencyId", company.getCurrencyId());
        update.set("documents", company.getDocuments());
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.returnNew(true);
        return mongoOperations.findAndModify(query, update, options, Company.class);
    }

    @Override
    public boolean ifProvinceExistsInCompany(String provinceId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("address.province.id").is(provinceId),
                        Criteria.where("deleted").ne(true)
                )
        );
        return mongoOperations.exists(query, Company.class);
    }

    @Override
    public boolean ifCityExistsInCompany(String cityId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("address.city._id").is(cityId),
                        Criteria.where("deleted").ne("true"))
        );
        return mongoOperations.exists(query, Company.class);
    }

    @Override
    public List<CompanyGetOneDTO> getAllByTermAndPagination(CompanySearchDTO companySearchDTO, Pageable pageable) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (companySearchDTO.getCompanyName() != null && !companySearchDTO.getCompanyName().equals("")) {
            criteria.and("name").regex(companySearchDTO.getCompanyName());
        }
        if (companySearchDTO.getCompanyCode() != null && !companySearchDTO.getCompanyCode().equals("")) {
            criteria.and("code").regex(companySearchDTO.getCompanyCode());
        }
        if (companySearchDTO.getProvinceId() != null && !companySearchDTO.getProvinceId().equals("")) {
            criteria.and("address.provinceId").is(companySearchDTO.getProvinceId());
        }
        if (companySearchDTO.getCityId() != null && !companySearchDTO.getCityId().equals("")) {
            criteria.and("address.cityId").is(companySearchDTO.getCityId());
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                skipOperation,
                limitOperation,
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("phoneNumber").as("phoneNumber")
                        .and("email").as("email")
                        .and("webSite").as("webSite")
                        .and("fax").as("fax")
                        .and("description").as("description")
                        .and("type").as("type")
                        .and("documents").as("documents")
                        .and("address.id").as("address.id")
                        .and("address.description").as("address.description")
                        .and("address.location").as("address.location")
                        .and(ConvertOperators.ToObjectId.toObjectId("$currencyId")).as("currencyId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.cityId")).as("address.cityId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.provinceId")).as("address.provinceId"),
                lookup("currency", "currencyId", "_id", "currency"),
                lookup("city", "address.cityId", "_id", "city"),
                lookup("province", "address.provinceId", "_id", "province"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("phoneNumber").as("phoneNumber")
                        .and("email").as("email")
                        .and("webSite").as("webSite")
                        .and("fax").as("fax")
                        .and("description").as("description")
                        .and("type").as("type")
                        .and("documents").as("documents")
                        .and("currency._id").as("currencyId")
                        .and("currency.title").as("currencyName")
                        .and("address.id").as("address.id")
                        .and("address.description").as("address.description")
                        .and("address.location").as("address.location")
                        .and("city._id").as("address.cityId")
                        .and("city.name").as("address.cityName")
                        .and("province._id").as("address.provinceId")
                        .and("province.name").as("address.provinceName")
        );
        return this.aggregate(aggregation, Company.class, CompanyGetOneDTO.class).getMappedResults();
    }

    @Override
    public List<Company> getCompanyByIdList(List<String> companyIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(companyIdList));
        return mongoOperations.find(query, Company.class);
    }

    @Override
    public List<Company> getAllCompany() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("name")
                .include("code")
                .include("id");
        return mongoOperations.find(query, Company.class);
    }

    @Override
    public boolean chickIfCurrencyExistsInCompany(String currencyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("currency.id").in(currencyId));
        return mongoOperations.exists(query, Company.class);
    }

    @Override
    public long numberOfCompanies(CompanySearchDTO companySearchDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (companySearchDTO.getCompanyName() != null && !companySearchDTO.getCompanyName().equals("")) {
            criteria.and("name").regex(companySearchDTO.getCompanyName());
        }
        if (companySearchDTO.getCompanyCode() != null && !companySearchDTO.getCompanyCode().equals("")) {
            criteria.and("code").regex(companySearchDTO.getCompanyCode());
        }
        if (companySearchDTO.getProvinceId() != null && !companySearchDTO.getProvinceId().equals("")) {
            criteria.and("address.provinceId").is(companySearchDTO.getProvinceId());
        }
        if (companySearchDTO.getCityId() != null && !companySearchDTO.getCityId().equals("")) {
            criteria.and("address.cityId").is(companySearchDTO.getCityId());
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return this.aggregate(aggregation, Company.class, Company.class).getMappedResults().size();
    }

    @Override
    public boolean checkCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, Company.class);
    }

    @Override
    public CompanyGetOneDTO getOne(String companyId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(companyId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("phoneNumber").as("phoneNumber")
                        .and("email").as("email")
                        .and("webSite").as("webSite")
                        .and("fax").as("fax")
                        .and("description").as("description")
                        .and("type").as("type")
                        .and("documents").as("documents")
                        .and("address.id").as("address.id")
                        .and("address.description").as("address.description")
                        .and("address.location").as("address.location")
                        .and(ConvertOperators.ToObjectId.toObjectId("$currencyId")).as("currencyId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.cityId")).as("address.cityId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.provinceId")).as("address.provinceId"),
                lookup("currency", "currencyId", "_id", "currency"),
                lookup("city", "address.cityId", "_id", "city"),
                lookup("province", "address.provinceId", "_id", "province"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("phoneNumber").as("phoneNumber")
                        .and("email").as("email")
                        .and("webSite").as("webSite")
                        .and("fax").as("fax")
                        .and("description").as("description")
                        .and("type").as("type")
                        .and("documents").as("documents")
                        .and("currency._id").as("currencyId")
                        .and("currency.title").as("currencyName")
                        .and("address.id").as("address.id")
                        .and("address.description").as("address.description")
                        .and("address.location").as("address.location")
                        .and("city._id").as("address.cityId")
                        .and("city.name").as("address.cityName")
                        .and("province._id").as("address.provinceId")
                        .and("province.name").as("address.provinceName")
        );
        return mongoOperations.aggregate(aggregation, Company.class, CompanyGetOneDTO.class).getUniqueMappedResult();
    }

    @Override
    public Company getCompanyName(String companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(companyId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, Company.class);
    }
}


