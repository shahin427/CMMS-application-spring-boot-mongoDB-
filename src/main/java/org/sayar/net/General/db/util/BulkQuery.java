package org.sayar.net.General.db.util;


import org.sayar.net.General.db.configuration.CustomMongoTemplate;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;

public class BulkQuery<T> extends CustomMongoTemplate<T> {
    private Class<T> entityClass;
    private BulkOperations bulkOperations;

    public BulkQuery(MongoDatabaseFactory mongoDbFactory, Class<T> entityClass, BulkMode bulkMode) {
        super(mongoDbFactory);
        this.entityClass = entityClass;
        bulkOperations = this.bulkOps(bulkMode, entityClass);
    }

    public BulkQuery<T> updateOne(CommonQuery<T>... commonQueryList) {
        for (int i = 0; i < commonQueryList.length; i++) {
            bulkOperations.updateOne(commonQueryList[i].getQuery(), commonQueryList[i].getUpdate());
        }
        return this;
    }

    public BulkQuery<T> updateOne(CommonQuery<T> commonQuery) {
        bulkOperations.updateOne(commonQuery.getQuery(), commonQuery.getUpdate());
        return this;
    }

//    public BulkWriteResult execute() {
//        return this.bulkOperations.execute();
//    }
}