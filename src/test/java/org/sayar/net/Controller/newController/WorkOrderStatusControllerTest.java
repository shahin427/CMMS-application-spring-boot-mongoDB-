package org.sayar.net.Controller.newController;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkOrderStatusControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @LocalServerPort
    private int randomServerPort;
    String authorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGFoaW4iLCJwYXNzd29yZCI6IiQyYSQxMiRoV1JXNG5JWUlWVDJIQlpNNDgzbzAuak83Y2lLMWlyLktHMHFpUklLcVcvLklBMWw3cXptSyIsInVzZXJJZCI6IjVlMGM1NzgzNzQ2MjcyMjg2ZmUzZWY1NSIsInVzZXJUeXBlSWQiOiI1ZjhmZTkwYTViYjJiODJlZGI1MzcwODkiLCJleHAiOjE2MTAxMjY1NDl9.fko77n_6_Dpf8BCJu-Vox8vaA7PO6pfhSw_We-UxoXRR6ZHabgHzf1iEqSH9HRylgZmHvhc3DfAkD3xLvCYBmA";

    @Test
    public void saveStatus() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get one WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("id", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<WorkOrderStatus> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURIGet.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURIGet.getBody().getStatus());

        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(query, WorkOrderStatus.class);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());
    }

    @Test
    public void updateStatus() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get one WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("id", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<WorkOrderStatus> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURIGet.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURIGet.getBody().getStatus());

        /* update WorkOrderStatus */
        String workOrderStatusUpdateTitle = "وضعیت ویرایش شده" + rand.nextInt(100000);
        WorkOrderStatus workOrderUpdateStatus = new WorkOrderStatus();
        workOrderUpdateStatus.setName(workOrderStatusUpdateTitle);
        workOrderUpdateStatus.setStatus(WorkOrderStatusEnum.CLOSED);
        workOrderUpdateStatus.setId(workOrderStatusGetOne.getId());

        HttpEntity<WorkOrderStatus> requestEntityForUpdate = new HttpEntity<WorkOrderStatus>(workOrderUpdateStatus, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/work-order-status/update";
        URI uri_put = new URI(Url);


        ResponseEntity<ResponseContent> responseExchangeURIUpdate = testRestTemplate.exchange(uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                ResponseContent.class);


        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        assertThat(responseExchangeURIUpdate.getBody().getData().toString(), JUnitMatchers.containsString(workOrderUpdateStatus.getName()));
        assertThat(responseExchangeURIUpdate.getBody().getData().toString(), JUnitMatchers.containsString(workOrderUpdateStatus.getStatus().toString()));

        /* get one WorkOrderStatus after update */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-one";

         builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("id", workOrderStatusGetOne.getId());

       responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(workOrderUpdateStatus.getName(), responseExchangeURIGet.getBody().getName());
        Assert.assertEquals(workOrderUpdateStatus.getStatus(), responseExchangeURIGet.getBody().getStatus());


        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        Query queryFindDeletedObject = new Query();
        queryFindDeletedObject.addCriteria(Criteria.where("name").is(workOrderUpdateStatus.getName()));
        queryFindDeletedObject.addCriteria(Criteria.where("deleted").is(true));


        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, WorkOrderStatus.class);
        Assert.assertEquals(workOrderUpdateStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderUpdateStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());
    }

    @Test
    public void deleteOne() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get one WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("id", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<WorkOrderStatus> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURIGet.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURIGet.getBody().getStatus());

        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(query, WorkOrderStatus.class);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());
    }

    @Test
    public void gteOne() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get one WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("id", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<WorkOrderStatus> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURIGet.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURIGet.getBody().getStatus());

        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(query, WorkOrderStatus.class);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());
    }

    @Test
    public void getAll() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get All WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-all";


        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<WorkOrderStatus>> responseExchangeURIGet = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<WorkOrderStatus>>() {
                });

        System.out.println(responseExchangeURIGet);
        List<WorkOrderStatus> listOfWorkOrderStatus = responseExchangeURIGet.getBody();

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertThat(listOfWorkOrderStatus,hasSize(greaterThanOrEqualTo(1) ));

        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(query, WorkOrderStatus.class);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());


    }

    @Test
    public void getAllByTermAndPagination() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save WorkOrderStatus*/
        Random rand = new Random();
        String workOrderStatusTitle = "وضعیت" + rand.nextInt(100000);
        WorkOrderStatus workOrderStatus = new WorkOrderStatus();
        workOrderStatus.setName(workOrderStatusTitle);
        workOrderStatus.setStatus(WorkOrderStatusEnum.OPENED);

        HttpEntity<WorkOrderStatus> requestEntityForPost = new HttpEntity<WorkOrderStatus>(workOrderStatus, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/work-order-status/save";
        URI uri = new URI(Url);


        ResponseEntity<WorkOrderStatus> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                WorkOrderStatus.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(workOrderStatus.getName(), responseExchangeURI.getBody().getName());
        Assert.assertEquals(workOrderStatus.getStatus(), responseExchangeURI.getBody().getStatus());


        /*find WorkOrderStatus in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(workOrderStatusTitle));
        WorkOrderStatus workOrderStatusGetOne = mongoOperations.findOne(query, WorkOrderStatus.class);
        System.out.println(workOrderStatusGetOne);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOne.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOne.getStatus());

        /* get All WorkOrderStatus */
        Url = "http://localhost:" + randomServerPort + "/work-order-status/get-all-by-pagination";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("term", workOrderStatusGetOne.getId())
                .queryParam("status", WorkOrderStatusEnum.OPENED);

        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<ResponseContent> responseExchangeURIGet = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                ResponseContent.class);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());

        /* Delete WorkOrderStatus */

        Url = "http://localhost:" + randomServerPort + "/work-order-status";

         builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("workOrderStatusId", workOrderStatusGetOne.getId());
        HttpEntity<?> requestEntityForDelete = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDelete = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDelete,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIDelete.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDelete.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDelete.getBody());

        /* check WorkOrderStatus deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        WorkOrderStatus workOrderStatusGetOneNotFound = mongoOperations.findOne(query, WorkOrderStatus.class);
        Assert.assertEquals(workOrderStatus.getName(), workOrderStatusGetOneNotFound.getName());
        Assert.assertEquals(workOrderStatus.getStatus(), workOrderStatusGetOneNotFound.getStatus());


    }
}