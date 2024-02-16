package org.sayar.net.Controller.newController;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Location;
import org.sayar.net.Model.newModel.Location.Province;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
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
public class CityControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MongoOperations mongoOperations;

    @LocalServerPort
    private int randomServerPort;
    String authorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGFoaW4iLCJwYXNzd29yZCI6IiQyYSQxMiRoV1JXNG5JWUlWVDJIQlpNNDgzbzAuak83Y2lLMWlyLktHMHFpUklLcVcvLklBMWw3cXptSyIsInVzZXJJZCI6IjVlMGM1NzgzNzQ2MjcyMjg2ZmUzZWY1NSIsInVzZXJUeXBlSWQiOiI1ZjhmZTkwYTViYjJiODJlZGI1MzcwODkiLCJleHAiOjE2MTAxMjY1NDl9.fko77n_6_Dpf8BCJu-Vox8vaA7PO6pfhSw_We-UxoXRR6ZHabgHzf1iEqSH9HRylgZmHvhc3DfAkD3xLvCYBmA";

    @Test
    public void getCityOrganes() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find   city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);

        /* get city Organization*/
        Url = "http://localhost:" + randomServerPort + "/city/organization";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", provinceGetOne.getId())
                .queryParam("parentOrganId" , "");

        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<Organization>> responseExchangeURIGetOrzanization = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<Organization>>() {
                });

        System.out.println(responseExchangeURIGetOrzanization);
        List<Organization> listOfOrganization = responseExchangeURIGetOrzanization.getBody();

        Assert.assertTrue(responseExchangeURIGetOrzanization.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetOrzanization.getStatusCode());
        Assert.assertThat(listOfOrganization,hasSize(lessThanOrEqualTo(0) ));


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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

    @Test
    public void findOne() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);

        /* get one city */
        Url = "http://localhost:" + randomServerPort + "/city/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<City> responseExchangeURIGetCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                City.class);

        System.out.println(responseExchangeURIGetCity);

        Assert.assertTrue(responseExchangeURIGetCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCity.getStatusCode());
        Assert.assertEquals(cityTitle, responseExchangeURIGetCity.getBody().getName());
        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(cityLocation.getLat(), responseExchangeURIGetCity.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), responseExchangeURIGetCity.getBody().getLocation().getLng(), 0);



        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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

    @Test
    public void getAll() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find   city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);

        /* get All city */
        Url = "http://localhost:" + randomServerPort + "/city/all";


        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<City>> responseExchangeURIGetCity = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<City>>() {
                });

        System.out.println(responseExchangeURIGetCity);
        List<City> listOfCity = responseExchangeURIGetCity.getBody();

        Assert.assertTrue(responseExchangeURIGetCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCity.getStatusCode());
        Assert.assertThat(listOfCity,hasSize(greaterThanOrEqualTo(1) ));


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        UriComponentsBuilder  builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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

    @Test
    public void update() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);

        /* get one city */
        Url = "http://localhost:" + randomServerPort + "/city/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<City> responseExchangeURIGetCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                City.class);

        System.out.println(responseExchangeURIGetCity);

        Assert.assertTrue(responseExchangeURIGetCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCity.getStatusCode());
        Assert.assertEquals(cityTitle, responseExchangeURIGetCity.getBody().getName());
        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(cityLocation.getLat(), responseExchangeURIGetCity.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), responseExchangeURIGetCity.getBody().getLocation().getLng(), 0);

        /* update city */

        String cityUpdateTitle = "خرمدره" + rand.nextInt(100000);
        Location cityLocationUpdate = new Location();
        cityLocationUpdate.setLat(36.6970118);
        cityLocationUpdate.setLng(59.4899051);

        City cityUpdate = new City();
        cityUpdate.setId(cityGetOne.getId());
        cityUpdate.setName(cityUpdateTitle);
        cityUpdate.setLocation(cityLocationUpdate);
        cityUpdate.setProvinceId(provinceGetOne.getId());

        HttpEntity<City> requestEntityForUpdate = new HttpEntity<City>(cityUpdate, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/update";
        URI uri_put = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIUpdate = testRestTemplate.exchange(uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                Boolean.class);
        System.out.println(responseExchangeURIUpdate);

        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIUpdate.getBody());



        /* get one city */
        Url = "http://localhost:" + randomServerPort + "/city/get-one";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());

        responseExchangeURIGetCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                City.class);

        System.out.println(responseExchangeURIGetCity);

        Assert.assertTrue(responseExchangeURIGetCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCity.getStatusCode());
        Assert.assertEquals(cityUpdate.getName(), responseExchangeURIGetCity.getBody().getName());
        /*
        assertEquals(expected, actual, delta) to compare floating-point numbers
        delta= expected - actual
        */

        Assert.assertEquals(cityLocationUpdate.getLat(), responseExchangeURIGetCity.getBody().getLocation().getLat(), 0);
        Assert.assertEquals(cityLocationUpdate.getLng(), responseExchangeURIGetCity.getBody().getLocation().getLng(), 0);


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        Query queryFindDeletedObject = new Query();
        queryFindDeletedObject.addCriteria(Criteria.where("name").is(cityUpdateTitle));
        queryFindDeletedObject.addCriteria(Criteria.where("deleted").is(true));

        City cityGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, City.class);
        Assert.assertEquals(cityUpdateTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocationUpdate.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocationUpdate.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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

    @Test
    public void delete() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);


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

    @Test
    public void getAllByProvinceId() throws URISyntaxException {
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


        /* save city*/
        String cityTitle = "ابهر" + rand.nextInt(1000);
        Location cityLocation = new Location();
        cityLocation.setLat(36.6970118);
        cityLocation.setLng(55.4899051);
        City city = new City();
        city.setLocation(cityLocation);
        city.setName(cityTitle);
        city.setProvinceId(provinceGetOne.getId());


        HttpEntity<City> requestEntityForPostCity = new HttpEntity<City>(city, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/city/save";
        URI uriPostCity = new URI(Url);


        ResponseEntity<Boolean> responseExchangeURIPostCity = testRestTemplate.exchange(
                uriPostCity,
                HttpMethod.POST,
                requestEntityForPostCity,
                Boolean.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURIPostCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIPostCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIPostCity.getBody());


        /* find   city in database */

        Query findCity = new Query();
        findCity.addCriteria(Criteria.where("name").is(cityTitle));
        City cityGetOne = mongoOperations.findOne(findCity, City.class);
        System.out.println(cityGetOne);
        Assert.assertEquals(cityTitle, cityGetOne.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOne.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOne.getLocation().getLng(), 0);

        /* get All city  by provinceId*/
        Url = "http://localhost:" + randomServerPort + "/city/get-all-by-province-id";

        UriComponentsBuilder  builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("provinceId", provinceGetOne.getId());

        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<City>> responseExchangeURIGetCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<City>>() {
                });

        System.out.println(responseExchangeURIGetCity);
        List<City> listOfCity = responseExchangeURIGetCity.getBody();

        Assert.assertTrue(responseExchangeURIGetCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCity.getStatusCode());
        Assert.assertThat(listOfCity,hasSize(greaterThanOrEqualTo(1) ));


        /* delete city */
        Url = "http://localhost:" + randomServerPort + "/city";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("cityId", cityGetOne.getId());
        HttpEntity<?> requestEntityForDeleteCity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> responseExchangeURIDeleteCity = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.DELETE,
                requestEntityForDeleteCity,
                Boolean.class);

        System.out.println(responseExchangeURIDeleteCity);
        Assert.assertTrue(responseExchangeURIDeleteCity.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIDeleteCity.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIDeleteCity.getBody());

        /* check city deleted true*/
        findCity.addCriteria(Criteria.where("deleted").is(true));
        City cityGetOneNotFound = mongoOperations.findOne(findCity, City.class);
        Assert.assertEquals(cityTitle, cityGetOneNotFound.getName());
        Assert.assertEquals(cityLocation.getLat(), cityGetOneNotFound.getLocation().getLat(), 0);
        Assert.assertEquals(cityLocation.getLng(), cityGetOneNotFound.getLocation().getLng(), 0);

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