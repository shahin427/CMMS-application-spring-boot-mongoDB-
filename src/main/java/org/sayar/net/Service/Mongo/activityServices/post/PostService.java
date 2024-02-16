package org.sayar.net.Service.Mongo.activityServices.post;



import org.sayar.net.Dao.Mongo.activityDao.post.PostDao;
import org.sayar.net.Model.Mongo.activityModel.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("postService")
public class PostService {

    private final PostDao postDao;
    @Autowired
    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public Post create(Post post) {
        return postDao.create(post);
    }

    public List<Post> getAll(String organizationId) {
        return postDao.getAll(organizationId);
    }

    public boolean setPostToPerson(String postId, String personId) {
        return postDao.setPostToPerson( postId, personId);
    }

    public Post update(Post post) {
        return postDao.update(post);
    }

    public Post getOne(String id) {
        return postDao.getOne(id);
    }

    public boolean delete(String id) {
        return postDao.delete(id);
    }

    public List<Post> getAllByCompanyId(String id) {
        return postDao.getAllByCompanyId(id);
    }
}
