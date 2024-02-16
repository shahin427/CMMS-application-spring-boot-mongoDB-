package org.sayar.net.Controller.Mongo.activityController.post;


import org.sayar.net.Model.Mongo.activityModel.Post;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Service.Mongo.activityServices.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javassist.compiler.ast.Keyword;

@RestController
@RequestMapping("post")
public class PostController {
//    private ResponseContent getResponse() {
//        return new ResponseContent(true, Keyword.c, "");
//    }

    private ResponseEntity<?> getReturn(ResponseContent content) {
        return ResponseEntity.ok().body(content);
    }
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Post post)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.create(post));
        return getReturn(content);
    }

    @GetMapping("getAll/{organizationId}")
    public ResponseEntity<?> getAll(@PathVariable("organizationId") String organizationId){
        ResponseContent content = new ResponseContent();
        content.setData(postService.getAll(organizationId));
        return getReturn(content);
    }

    @GetMapping("setPostToPerson/{postId}/{personId}")
    public ResponseEntity<?> setPostToPerson(@PathVariable("postId") String postId, @PathVariable("personId") String personId)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.setPostToPerson(postId,personId));
        return getReturn(content);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Post post)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.update(post));
        return getReturn(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.getOne(id));
        return getReturn(content);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.delete(id));
        return getReturn(content);
    }
    @GetMapping("getAllByCompanyId/{id}")
    public ResponseEntity<?> getAllByCompanyId(@PathVariable("id") String id)
    {
        ResponseContent content = new ResponseContent();
        content.setData(postService.getAllByCompanyId(id));
        return getReturn(content);
    }

}
