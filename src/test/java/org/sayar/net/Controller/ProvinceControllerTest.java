package org.sayar.net.Controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Location;
import org.sayar.net.Model.newModel.Location.Province;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProvinceControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @LocalServerPort
    private int randomServerPort;
    String authorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGFoaW4iLCJwYXNzd29yZCI6IiQyYSQxMiRoV1JXNG5JWUlWVDJIQlpNNDgzbzAuak83Y2lLMWlyLktHMHFpUklLcVcvLklBMWw3cXptSyIsInVzZXJJZCI6IjVlMGM1NzgzNzQ2MjcyMjg2ZmUzZWY1NSIsInVzZXJUeXBlSWQiOiI1ZjhmZTkwYTViYjJiODJlZGI1MzcwODkiLCJleHAiOjE2MTAxMjY1NDl9.fko77n_6_Dpf8BCJu-Vox8vaA7PO6pfhSw_We-UxoXRR6ZHabgHzf1iEqSH9HRylgZmHvhc3DfAkD3xLvCYBmA";


    @Test
    public void getCity() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Province*/
        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(100000);
        Location location = new Location();
        location.setLat(12.56526);
        location.setLng(15.454596);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntityForPost = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/save";
        URI uri = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(true, responseExchangeURI.getBody());

        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* get All City of Province*/
        Url = "http://localhost:" + randomServerPort + "/province/city/"+provinceGetOne.getId();

        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<City>> responseExchangeURIGet = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<City>>() {
                });


        System.out.println(responseExchangeURIGet);
        List<City> listOfCity = responseExchangeURIGet.getBody();


        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertThat(listOfCity,hasSize(lessThanOrEqualTo(0) ));


        /* delete province*/

        Url = "http://localhost:" + randomServerPort + "/province";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        Province provinceGetOneNotFound = mongoOperations.findOne(query, Province.class);
        Assert.assertEquals(provinceTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(location.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);
    }
    /*
    بازگشتی این متد در کد برابر با null هستش
     */
    @Test
    public void getProvinceByOrganId() {

    }
    /*
    بازگشتی این متد در کد برابر با null هستش
     */
    @Test
    public void getAllNameIdAndLocation() {
    }

    @Test
    public void findOne() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Province*/
        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(100000);
        Location location = new Location();
        location.setLat(12.56526);
        location.setLng(15.454596);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntityForPost = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/save";
        URI uri = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(true, responseExchangeURI.getBody());

        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* get one province*/
        Url = "http://localhost:" + randomServerPort + "/province/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Province> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Province.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(provinceTitle, responseExchangeURIGet.getBody().getName());
        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(location.getLat(), responseExchangeURIGet.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), responseExchangeURIGet.getBody().getLocation().getLng(), 0);



        /*find Province in database*/

        Query queryFindProvince = new Query();
        queryFindProvince.addCriteria(Criteria.where("name").is(provinceTitle));
        provinceGetOne = mongoOperations.findOne(queryFindProvince, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* delete province*/

        Url = "http://localhost:" + randomServerPort + "/province";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        Province provinceGetOneNotFound = mongoOperations.findOne(query, Province.class);
        Assert.assertEquals(provinceTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(location.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);

    }
    /*
        متد save با put فراخوانی شده  و URL اش هم شامل update است ولی بدون ID وقتی یک استان رو به عنوان ورودی بهش میدیم save میکنه
         */
    @Test
    public void save() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(1000);
        Location location = new Location();
        location.setLat(35.6970118);
        location.setLng(51.4899051);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntity = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/update";
        URI uri = new URI(Url);


        ResponseEntity<Province> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.PUT,
                requestEntity,
                Province.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(provinceTitle, responseExchangeURI.getBody().getName());
        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(35.6970118, responseExchangeURI.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(51.4899051, responseExchangeURI.getBody().getLocation().getLng(), 0);


        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);

        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);


        /* delete province*/

        Url = "http://localhost:" + randomServerPort + "/province";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        Province provinceGetOneNotFound = mongoOperations.findOne(query, Province.class);
        Assert.assertEquals(provinceTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(location.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);
    }

    @Test
    public void update() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Province*/
        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(100000);
        Location location = new Location();
        location.setLat(12.56526);
        location.setLng(15.454596);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntityForPost = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/save";
        URI uri = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(true, responseExchangeURI.getBody());

        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);

        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /*update Province*/
        String provinceUpdateTitle = "تهران" + rand.nextInt(100000);
        Location locationUpdate = new Location();
        locationUpdate.setLat(36.6970118);
        locationUpdate.setLng(59.4899051);
        Province provinceUpdate = new Province();
        provinceUpdate.setId(provinceGetOne.getId());
        provinceUpdate.setName(provinceUpdateTitle);
        provinceUpdate.setLocation(locationUpdate);

        HttpEntity<Province> requestEntityForUpdate = new HttpEntity<Province>(provinceUpdate, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/province/update";
        URI uri_put = new URI(Url);


        ResponseEntity<Province> responseExchangeURIUpdate = testRestTemplate.exchange(uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                Province.class);
        System.out.println(responseExchangeURIUpdate);

        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        Assert.assertEquals(provinceUpdateTitle, responseExchangeURIUpdate.getBody().getName());


        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(locationUpdate.getLat(), responseExchangeURIUpdate.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(locationUpdate.getLng(), responseExchangeURIUpdate.getBody().getLocation().getLng(), 0);


        /* delete province*/
//
        Url = "http://localhost:" + randomServerPort + "/province";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        queryFindDeletedObject.addCriteria(Criteria.where("name").is(provinceUpdateTitle));
        queryFindDeletedObject.addCriteria(Criteria.where("deleted").is(true));

        Province provinceGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, Province.class);
        Assert.assertEquals(provinceUpdateTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(locationUpdate.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(locationUpdate.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);
    }

    @Test
    public void delete() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Province*/
        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(100000);
        Location location = new Location();
        location.setLat(12.56526);
        location.setLng(15.454596);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntityForPost = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/save";
        URI uri = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(true, responseExchangeURI.getBody());

        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* delete province*/

        Url = "http://localhost:" + randomServerPort + "/province";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        Province provinceGetOneNotFound = mongoOperations.findOne(query, Province.class);
        Assert.assertEquals(provinceTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(location.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);
    }

    @Test
    public void getAllProvince() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save Province*/
        Random rand = new Random();
        String provinceTitle = "زنجان" + rand.nextInt(100000);
        Location location = new Location();
        location.setLat(12.56526);
        location.setLng(15.454596);
        Province province = new Province();
        province.setName(provinceTitle);
        province.setLocation(location);

        HttpEntity<Province> requestEntityForPost = new HttpEntity<Province>(province, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/province/save";
        URI uri = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURI = testRestTemplate.exchange(uri,
                HttpMethod.POST,
                requestEntityForPost,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());
        Assert.assertEquals(true, responseExchangeURI.getBody());

        /*find Province in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(provinceTitle));
        Province provinceGetOne = mongoOperations.findOne(query, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* get All province*/
        Url = "http://localhost:" + randomServerPort + "/province/get-all-by-term";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("term", provinceTitle);
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<Province>> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<Province>>() {
                });
        List<Province> listOfProvince = responseExchangeURIGet.getBody();


        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertThat(listOfProvince,hasSize(greaterThanOrEqualTo(1) ));

        /*find Province in database*/

        Query queryFindProvince = new Query();
        queryFindProvince.addCriteria(Criteria.where("name").is(provinceTitle));
        provinceGetOne = mongoOperations.findOne(queryFindProvince, Province.class);
        System.out.println(provinceGetOne);
        Assert.assertEquals(provinceTitle, provinceGetOne.getName());
        Assert.assertEquals(location.getLat(), provinceGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOne.getLocation().getLng(), 0);

        /* delete province*/

        Url = "http://localhost:" + randomServerPort + "/province";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());
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
        Province provinceGetOneNotFound = mongoOperations.findOne(query, Province.class);
        Assert.assertEquals(provinceTitle, provinceGetOneNotFound.getName());
        Assert.assertEquals(location.getLat(), provinceGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(location.getLng(), provinceGetOneNotFound.getLocation().getLng(), 0);
    }
}