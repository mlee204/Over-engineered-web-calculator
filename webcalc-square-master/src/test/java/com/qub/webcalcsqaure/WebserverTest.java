package com.qub.webcalcsqaure;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebserverTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Webserver webserver;
    private TestRestTemplate restTemplate =  new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    @DisplayName("Test application loads")
    public void contextLoads() throws Exception {
        assertThat(webserver).isNotNull();
    }

    @Test
    @DisplayName("Http request with no parameter")
    public void testResponseNoParameter() throws Exception {
        String expectedAnswer = "x parameter is required";

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/"),
                HttpMethod.GET, entity, String.class);

        ObjectNode expectedBody = webserver.outputJsonResponse(true, expectedAnswer, null,400);

        JSONAssert.assertEquals(String.valueOf(expectedBody), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Http request with valid parameter = 5")
    public void testResponseValidParameter() throws Exception {
        int x = 5;
        int expectedAnswer = Square.square(x);
        String expectedString = x + "²" + "= " + expectedAnswer;
        int expectedStatus = 200;

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/?x=" + x),
                HttpMethod.GET, entity, String.class);

        ObjectNode expectedBody = webserver.outputJsonResponse(false, expectedString, expectedAnswer,expectedStatus);
        JSONAssert.assertEquals(String.valueOf(expectedBody), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Http request with valid parameter negative = -5")
    public void testResponseValidNegativeParameter() throws Exception {
        int x = -5;
        int expectedAnswer = Square.square(x);
        String expectedString = x + "²" + "= " + expectedAnswer;
        int expectedStatus = 200;

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/?x=" + x),
                HttpMethod.GET, entity, String.class);

        ObjectNode expectedBody = webserver.outputJsonResponse(false, expectedString, expectedAnswer,expectedStatus);
        JSONAssert.assertEquals(String.valueOf(expectedBody), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Http request with invalid parameter - String")
    public void testResponseInValidParameterString() throws Exception {
        String xParam = "bad";
        String expectedString = "parameter x is not a valid Integer";
        int expectedStatus = 400;

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/?x=" + xParam),
                HttpMethod.GET, entity, String.class);

        ObjectNode expected = webserver.outputJsonResponse(true, expectedString, null,expectedStatus);
        JSONAssert.assertEquals(String.valueOf(expected), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Http request with invalid parameter - Double")
    public void testResponseInValidParameterDouble() throws Exception {
        double xParam = 5.5;
        String expectedString = "parameter x is not a valid Integer";
        int expectedStatus = 400;

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/?x=" + xParam),
                HttpMethod.GET, entity, String.class);

        ObjectNode expected = webserver.outputJsonResponse(true, expectedString, null,expectedStatus);
        JSONAssert.assertEquals(String.valueOf(expected), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Http request to unknown url= check page not found error handling")
    public void testResponsePageNotFound() throws Exception {
        String expectedString = "Page not found: check url parameter syntax is correct";
        int expectedStatus = 404;

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/bad/url"),
                HttpMethod.GET, entity, String.class);

        ObjectNode expectedBody = webserver.outputJsonResponse(true, expectedString, null,expectedStatus);
        JSONAssert.assertEquals(String.valueOf(expectedBody), response.getBody(), true);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
        assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }


}