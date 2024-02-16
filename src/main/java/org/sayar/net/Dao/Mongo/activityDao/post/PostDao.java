package org.sayar.net.Dao.Mongo.activityDao.post;

import org.sayar.net.Model.Mongo.activityModel.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("postDao")
public class PostDao {
    private final MongoOperations mongoOperations;

    @Autowired
    public PostDao(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Post create(Post post) {
        mongoOperations.save(post);
        return post;
    }

    public List<Post> getAll(String organizationId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("organizationId").is(organizationId));
        return mongoOperations.find(query, Post.class);
    }

    public boolean setPostToPerson(String postId, String personId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(personId));
        Post post = new Post();
        post.setId(postId);
        Update update = new Update();
        update.set("post", post);
//        if (mongoOperations.exists(query, Person.class)) {
//            mongoOperations.updateFirst(query, update, Person.class);
//            return true;
//        }
        return false;
    }

    public Post update(Post post) {
        mongoOperations.save(post);
        return post;
    }

    public Post getOne(String id) {
        return mongoOperations.findOne(
                new Query().addCriteria(Criteria.where("id").is(id)),
                Post.class
        );
    }

    public boolean delete(String id) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("post").is(id));
//            if (mongoOperations.exists(query, Person.class))
//                return false;
//            else {
//                mongoOperations.remove(new Query().addCriteria(Criteria.where("id").is(id)), Post.class);
//                return true;
//            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<Post> getAllByCompanyId(String id) {
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("companyId").is(id));
//            query.fields().include("id");
//            List<Organization> organizationList = mongoOperations.find(query,Organization.class);
//            List<String> idList = new ArrayList<>();
//            for (Organization o:organizationList)
//                idList.add(o.getId());
//
//            query= new Query();
//            query.addCriteria(Criteria.where("organizationId").in(idList));
//
//
//            return mongoOperations.find(query, Post.class);
//
//        } catch (Exception e) {
//            e.printStackTrace();
            return null;
//        }

    }


}
