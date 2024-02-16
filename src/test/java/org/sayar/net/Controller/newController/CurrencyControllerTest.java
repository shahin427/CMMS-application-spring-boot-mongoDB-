package org.sayar.net.Controller.newController;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sayar.net.Model.newModel.Currency;
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
public class CurrencyControllerTest {
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

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get one currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Currency> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currency.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currency.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());


        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        Currency currencyGetOneNotFound = mongoOperations.findOne(query, Currency.class);
        Assert.assertEquals(currency.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }

    @Test
    public void getAll() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get All currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-all";


        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<List<Currency>> responseExchangeURIGet = testRestTemplate.exchange(
                Url,
                HttpMethod.GET,
                requestEntityForGet,
                new ParameterizedTypeReference<List<Currency>>() {
                });

        System.out.println(responseExchangeURIGet);
        List<Currency> listOfCurrency = responseExchangeURIGet.getBody();

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertThat(listOfCurrency,hasSize(greaterThanOrEqualTo(1) ));



        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        Currency currencyGetOneNotFound = mongoOperations.findOne(query, Currency.class);
        Assert.assertEquals(currency.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }

    @Test
    public void saveCurrency() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get one currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Currency> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currency.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currency.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());


        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        Currency currencyGetOneNotFound = mongoOperations.findOne(query, Currency.class);
        Assert.assertEquals(currency.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }

    @Test
    public void update() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get one currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Currency> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currency.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currency.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());

        /* update currency*/

        String currencyUpdateTitle = "واحد پول ویرایش شده" + rand.nextInt(100000);
        Currency currencyUpdate = new Currency();
        currencyUpdate.setTitle(currencyUpdateTitle);
        currencyUpdate.setIsoCode("کد ویرایش شده"+ rand.nextInt(100000));
        currencyUpdate.setId(currencyGetOne.getId());


        HttpEntity<Currency> requestEntityForUpdate = new HttpEntity<Currency>(currencyUpdate, requestHeaders);

        Url = "http://localhost:" + randomServerPort + "/currency/update";
        URI uri_put = new URI(Url);


        ResponseEntity<Currency> responseExchangeURIUpdate = testRestTemplate.exchange(
                uri_put,
                HttpMethod.PUT,
                requestEntityForUpdate,
                Currency.class);

        System.out.println(responseExchangeURIUpdate);

        Assert.assertTrue(responseExchangeURIUpdate.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIUpdate.getStatusCode());
        Assert.assertEquals(currencyUpdate.getTitle(), responseExchangeURIUpdate.getBody().getTitle());
        Assert.assertEquals(currencyUpdate.getIsoCode(), responseExchangeURIUpdate.getBody().getIsoCode());

        /* get one currency after update*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());

        responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currencyUpdate.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currencyUpdate.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());

        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        Query queryFindDeletedObject = new Query();
        queryFindDeletedObject.addCriteria(Criteria.where("title").is(currencyUpdate.getTitle()));
        queryFindDeletedObject.addCriteria(Criteria.where("deleted").is(true));

        Currency currencyGetOneNotFound = mongoOperations.findOne(queryFindDeletedObject, Currency.class);
        Assert.assertEquals(currencyUpdate.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currencyUpdate.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }

    @Test
    public void delete() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get one currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Currency> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currency.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currency.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());


        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        Currency currencyGetOneNotFound = mongoOperations.findOne(query, Currency.class);
        Assert.assertEquals(currency.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }

    @Test
    public void checkIfCurrencyIsUnique() throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Authorization", authorizationToken);

        /*save currency*/
        Random rand = new Random();
        String currencyTitle = "واحد پول" + rand.nextInt(100000);
        Currency currency = new Currency();
        currency.setTitle(currencyTitle);
        currency.setIsoCode("کد"+ rand.nextInt(100000));

        HttpEntity<Currency> requestEntityForPost = new HttpEntity<Currency>(currency, requestHeaders);

        String Url = "http://localhost:" + randomServerPort + "/currency/save";
        URI uri = new URI(Url);


        ResponseEntity<Currency> responseExchangeURI = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntityForPost,
                Currency.class);

        System.out.println(responseExchangeURI);
        Assert.assertTrue(responseExchangeURI.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURI.getStatusCode());

        /*find currency in database*/

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(currencyTitle));
        Currency currencyGetOne = mongoOperations.findOne(query, Currency.class);
        System.out.println(currencyGetOne);
        Assert.assertEquals(currency.getTitle(), currencyGetOne.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOne.getIsoCode());

        /* get one currency*/
        Url = "http://localhost:" + randomServerPort + "/currency/get-one";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
        HttpEntity<?> requestEntityForGet = new HttpEntity<>(requestHeaders);

        ResponseEntity<Currency> responseExchangeURIGet = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Currency.class);

        System.out.println(responseExchangeURIGet);

        Assert.assertTrue(responseExchangeURIGet.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGet.getStatusCode());
        Assert.assertEquals(currency.getTitle(), responseExchangeURIGet.getBody().getTitle());
        Assert.assertEquals(currency.getIsoCode(), responseExchangeURIGet.getBody().getIsoCode());

        /*check_if-currency-is-unique_is_true*/

        Url = "http://localhost:" + randomServerPort + "/currency/check=if-currency-is-unique";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("isoCode", currency.getIsoCode())
                .queryParam("title", currency.getTitle() );

        ResponseEntity<Boolean> responseExchangeURIGetCheckIfCurrencyIsUnique = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Boolean.class);

        System.out.println(responseExchangeURIGetCheckIfCurrencyIsUnique);

        Assert.assertTrue(responseExchangeURIGetCheckIfCurrencyIsUnique.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCheckIfCurrencyIsUnique.getStatusCode());
        Assert.assertEquals(true, responseExchangeURIGetCheckIfCurrencyIsUnique.getBody());

        /*check_if-currency-is-unique_is_false*/


        Url = "http://localhost:" + randomServerPort + "/currency/check=if-currency-is-unique";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("isoCode", currency.getIsoCode()+ rand.nextInt(100000))
                .queryParam("title", currency.getTitle() + rand.nextInt(100000));

        responseExchangeURIGetCheckIfCurrencyIsUnique = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntityForGet,
                Boolean.class);

        System.out.println(responseExchangeURIGetCheckIfCurrencyIsUnique);

        Assert.assertTrue(responseExchangeURIGetCheckIfCurrencyIsUnique.getStatusCode() == HttpStatus.OK);
        Assert.assertEquals(HttpStatus.OK, responseExchangeURIGetCheckIfCurrencyIsUnique.getStatusCode());
        Assert.assertEquals(false, responseExchangeURIGetCheckIfCurrencyIsUnique.getBody());

        /* delete currency*/

        Url = "http://localhost:" + randomServerPort + "/currency";

        builder = UriComponentsBuilder.fromHttpUrl(Url)
                .queryParam("currencyId", currencyGetOne.getId());
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

        /* check currency deleted true*/
        query.addCriteria(Criteria.where("deleted").is(true));
        Currency currencyGetOneNotFound = mongoOperations.findOne(query, Currency.class);
        Assert.assertEquals(currency.getTitle(), currencyGetOneNotFound.getTitle());
        Assert.assertEquals(currency.getIsoCode(), currencyGetOneNotFound.getIsoCode());
    }
}