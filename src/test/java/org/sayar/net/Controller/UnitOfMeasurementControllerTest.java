package org.sayar.net.Controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
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
public class UnitOfMeasurementControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @LocalServerPort
    private int randomServerPort;
    String authorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGFoaW4iLCJwYXNzd29yZCI6IiQyYSQxMiRoV1JXNG5JWUlWVDJIQlpNNDgzbzAuak83Y2lLMWlyLktHMHFpUklLcVcvLklBMWw3cXptSyIsInVzZXJJZCI6IjVlMGM1NzgzNzQ2MjcyMjg2ZmUzZWY1NSIsInVzZXJUeXBlSWQiOiI1ZjhmZTkwYTViYjJiODJlZGI1MzcwODkiLCJleHAiOjE2MTAxMjY1NDl9.fko77n_6_Dpf8BCJu-Vox8vaA7PO6pfhSw_We-UxoXRR6ZHabgHzf1iEqSH9HRylgZmHvhc3DfAkD3xLvCYBmA";

    @Test
    public void findOne() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "واحد اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());

        /* get one Measure*/
        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<UnitOfMeasurement> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURIGet.getBody().getUnit());
        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(query, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }

    @Test
    public void getAll() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "واحد اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());

        /* get All  Measure*/
        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/get-all";


        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<UnitOfMeasurement>> responseExchangeURIGet = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<UnitOfMeasurement>>() {
                });

        List<UnitOfMeasurement> listOfUnitOfMeasurement = responseExchangeURIGet.getBody();

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertThat(listOfUnitOfMeasurement,hasSize(greaterThanOrEqualTo(1) ));


        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(query, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }

    @Test
    public void saveMeasure() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "واحد اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());


        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(query, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }

    @Test
    public void update() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "مائده اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());

        /* get one Measure*/
        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<UnitOfMeasurement> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURIGet.getBody().getUnit());

        /* update unitOfMeasurement*/

        String unitOfMeasurementUpdateTitle = "مائده اندازه گیری ویرایش شده" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurementUpdate = new UnitOfMeasurement();
        unitOfMeasurementUpdate.setTitle(unitOfMeasurementUpdateTitle);
        unitOfMeasurementUpdate.setUnit("واحد ویرایش شده"+ rand.nextInt(100000));
        unitOfMeasurementUpdate.setId(unitOfMeasurementGetOne.getId());

        HttpEntity<UnitOfMeasurement> requestEntityForUpdate = new HttpEntity<UnitOfMeasurement>(unitOfMeasurementUpdate, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/update";
        URI uri_put = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURIUpdate = testRestTemplate.exchange(
                uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURIUpdate);

        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        Assert.assertEquals(unitOfMeasurementUpdate.getTitle(), responseExchangeURIUpdate.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurementUpdate.getUnit(), responseExchangeURIUpdate.getBody().getUnit());

        /* get one Measure after update*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/get-one";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());

        responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(unitOfMeasurementUpdate.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurementUpdate.getUnit(), responseExchangeURIGet.getBody().getUnit());

        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        Query queryFindDeletedObject = new Query();
        queryFindDeletedObject.addCriteria(Criteria.where("title").is(unitOfMeasurementUpdateTitle));
        queryFindDeletedObject.addCriteria(Criteria.where("deleted").is(true));

        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementUpdate.getTitle(), unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurementUpdate.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }

    @Test
    public void delete() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "واحد اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());

        /* get one Measure*/
        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<UnitOfMeasurement> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURIGet.getBody().getUnit());

        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(query, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }

    @Test
    public void checkIfUnitOfMeasurementIsUnique() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Measure*/
        Random rand = new Random();
        String unitOfMeasurementTitle = "واحد اندازه گیری" + rand.nextInt(100000);
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setTitle(unitOfMeasurementTitle);
        unitOfMeasurement.setUnit("واحد"+ rand.nextInt(100000));


        HttpEntity<UnitOfMeasurement> requestEntityForPost = new HttpEntity<UnitOfMeasurement>(unitOfMeasurement, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/save";
        URI uri = new URI(Url);


        ResponseEntity<UnitOfMeasurement> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                UnitOfMeasurement.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(unitOfMeasurementTitle, responseExchangeURI.getBody().getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), responseExchangeURI.getBody().getUnit());

        /*find Measure in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementTitle));
        UnitOfMeasurement unitOfMeasurementGetOne = mongoOperations.findOne(query, UnitOfMeasurement.class);
        System.out.println(unitOfMeasurementGetOne);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOne.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOne.getUnit());


        /*check-if-unit-of-measurement-is-unique_is_true*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/check-if-unit-of-measurement-is-unique";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("title", unitOfMeasurementTitle);
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Boolean.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIGet.getBody());

        /*check-if-unit-of-measurement-is-unique_is_false*/


        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement/check-if-unit-of-measurement-is-unique";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("title", unitOfMeasurementTitle+ rand.nextInt(100000));
        requestEntityForGet = new HttpEntity<>(requestHeaders);

        responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Boolean.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(false, responseExchangeURIGet.getBody());

        /* delete UnitOfMeasurement*/

        Url = "http://localhost:" + randomServerPort + "/unit-of-measurement";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("unitOfMeasurementId", unitOfMeasurementGetOne.getId());
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

        /* check province deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        UnitOfMeasurement unitOfMeasurementGetOneNotFound = mongoOperations.findOne(query, UnitOfMeasurement.class);
        Assert.assertEquals(unitOfMeasurementTitle, unitOfMeasurementGetOneNotFound.getTitle());
        Assert.assertEquals(unitOfMeasurement.getUnit(), unitOfMeasurementGetOneNotFound.getUnit());
    }
}