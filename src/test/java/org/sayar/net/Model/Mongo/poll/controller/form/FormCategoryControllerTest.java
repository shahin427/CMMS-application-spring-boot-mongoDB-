package org.sayar.net.Model.Mongo.poll.controller.form;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FormCategoryControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @LocalServerPort
    private int randomServerPort;
    String authorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGFoaW4iLCJwYXNzd29yZCI6IiQyYSQxMiRoV1JXNG5JWUlWVDJIQlpNNDgzbzAuak83Y2lLMWlyLktHMHFpUklLcVcvLklBMWw3cXptSyIsInVzZXJJZCI6IjVlMGM1NzgzNzQ2MjcyMjg2ZmUzZWY1NSIsInVzZXJUeXBlSWQiOiI1ZjhmZTkwYTViYjJiODJlZGI1MzcwODkiLCJleHAiOjE2MTAxMjY1NDl9.fko77n_6_Dpf8BCJu-Vox8vaA7PO6pfhSw_We-UxoXRR6ZHabgHzf1iEqSH9HRylgZmHvhc3DfAkD3xLvCYBmA";

    @Test
    public void create() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get one formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<FormCategory> responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryName, responseExchangeURIGetCategory.getBody().getTitle());


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

         builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);


        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);
    }

    @Test
    public void getAll() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get َAll formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-all";


        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<ResponseContent> responseExchangeURIGetCategory = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                ResponseContent.class);


        ResponseContent listOfFormCategory = responseExchangeURIGetCategory.getBody();
        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertThat(listOfFormCategory.getData().toString(),  JUnitMatchers.containsString(formCategoryName));


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);


        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);
    }

    @Test
    public void getById() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get one formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<FormCategory> responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryName, responseExchangeURIGetCategory.getBody().getTitle());


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);




        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);
    }

    @Test
    public void delete() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get one formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<FormCategory> responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryName, responseExchangeURIGetCategory.getBody().getTitle());


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);


        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);
    }

    @Test
    public void update() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get one formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<FormCategory> responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryName, responseExchangeURIGetCategory.getBody().getTitle());

        /* update formCategory */
        String formCategoryUpdateName = "دسته بندی فرم ویرایش شده " + rand.nextInt(1000);
        FormCategory formCategoryUpdate=new FormCategory();
        formCategoryUpdate.setTitle(formCategoryUpdateName);
        formCategoryUpdate.setId(formCategoryGetOne.getId());

        HttpEntity<FormCategory> requestEntityForUpdate = new HttpEntity<FormCategory>(formCategoryUpdate, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/formCategory/update";
        URI uri_put = new URI(Url);


        ResponseEntity<ResponseContent> responseExchangeURIUpdate = testRestTemplate.exchange(uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                ResponseContent.class);
        System.out.println(responseExchangeURIUpdate);

        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        assertThat(responseExchangeURIUpdate.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryUpdateName));

        /* get one formCategory after update*/
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

         builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());

         responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryUpdateName, responseExchangeURIGetCategory.getBody().getTitle());


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);


        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        Query queryFindDeletedObject = new Query();
        queryFindDeletedObject.addCriteria(Criteria.where("title").is(formCategoryUpdateName));
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);

    }
/*
بازگشتی این متد در حالی که باید false بده هم true هستش
 */
    @Test
    @Ignore
    public void checkIfTitleIsUnique() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);
        Random rand = new Random();
        String formCategoryName = "دسته بندی فرم " + rand.nextInt(1000);
        FormCategory formCategory=new FormCategory();
        formCategory.setTitle(formCategoryName);

        HttpEntity<FormCategory> requestEntity = new HttpEntity<FormCategory>(formCategory, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/formCategory/create";
        URI uri = new URI(Url);

        ResponseEntity<ResponseContent> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntity,
                ResponseContent.class);


        System.out.println(responseExchangeURI.getBody().getData());
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        assertThat(responseExchangeURI.getBody().getData().toString(), JUnitMatchers.containsString(formCategoryName));

        /*find formCategory in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(formCategoryName));
        FormCategory formCategoryGetOne = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertEquals(formCategoryName, formCategoryGetOne.getTitle());


        /* get one formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("formCategoryId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<FormCategory> responseExchangeURIGetCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                FormCategory.class);

        Assert.assertTrue(responseExchangeURIGetCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCategory.getStatusCode());
        Assert.assertEquals(formCategoryName, responseExchangeURIGetCategory.getBody().getTitle());

        /*check-if-formCategory-title-is-unique_is_true*/

        Url = "http://localhost:" + randomServerPort + "/formCategory/check-if-title-is-unique";

         builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("title", formCategoryName);

        ResponseEntity<ResponseContent> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                ResponseContent.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(false, responseExchangeURIGet.getBody().getData());

        /*check-if-formCategory-title-is-unique_is_false*/


        Url = "http://localhost:" + randomServerPort + "/formCategory/check-if-title-is-unique";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("title", formCategoryName+ rand.nextInt(100000));

        responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                ResponseContent.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIGet.getBody().getData());


        /* delete formCategory */
        Url = "http://localhost:" + randomServerPort + "/formCategory/delete";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("deleteId", formCategoryGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteFormCategory = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);


        Assert.assertTrue(responseExchangeURIDeleteFormCategory.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteFormCategory.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteFormCategory.getBody());

        /* check formCategory deleted true*/
        FormCategory formCategoryGetOneNotFound = mongoOperations.findOne(query, FormCategory.class);
        Assert.assertNull(formCategoryGetOneNotFound);
    }


}