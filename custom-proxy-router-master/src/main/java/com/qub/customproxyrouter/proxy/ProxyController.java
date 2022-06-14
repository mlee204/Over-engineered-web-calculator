package com.qub.customproxyrouter.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qub.customproxyrouter.operator.Operator;
import com.qub.customproxyrouter.operator.OperatorRepository;
import com.qub.customproxyrouter.operator.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@RestController
@RequestMapping(path = "/", produces = "application/json")
public class ProxyController {

    private  OperatorService operatorService;
    private  OperatorRepository operatorRepository;
    private  RestTemplate restTemplate;

    public ProxyController() {
    }

    @Autowired
    public ProxyController(OperatorService operatorService, OperatorRepository operatorRepository, RestTemplate restTemplate) {
        this.operatorService = operatorService;
        this.operatorRepository = operatorRepository;
        this.restTemplate = restTemplate;
    }


    ObjectMapper mapper = new ObjectMapper();


    public ObjectNode outputJsonResponse(boolean error, String string, Integer answer, int status) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("error", error);
        objectNode.put("string", string);
        objectNode.put("answer", answer);
        objectNode.put("status", status);
        return objectNode;
    }

    @GetMapping(path="/", produces = "application/json")
    public ResponseEntity<byte[]> getOperatorsResponse(
            @RequestParam(value = "x", required=false) String x,
            @RequestParam(value = "y", required=false) String y,
            @RequestParam(value = "o") String operator )  throws ServletRequestBindingException  {


        Optional<Operator> operatorName = operatorRepository.findOperatorsByName(operator);

        if (!operatorName.isPresent()) {
            throw new ServletRequestBindingException("operator not found in Database");
        }

        String url = operatorService.getUrlEndPointOfOperatorFromDB(operator);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(url + "/?x=" + x + "&y=" + y, HttpMethod.GET, HttpEntity.EMPTY, byte[].class);
            return response;
        } catch(HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        }

    }

    @CrossOrigin
    @GetMapping(path="/square", produces = "application/json")
    public ResponseEntity<byte[]> squareXOnly(
            @RequestParam(value = "x", required=false) String x) throws ServletRequestBindingException  {


        Optional<Operator> operatorName = operatorRepository.findOperatorsByName("square");

        if (!operatorName.isPresent()) {
            throw new ServletRequestBindingException("operator not found in Database");
        }

        String url = operatorService.getUrlEndPointOfOperatorFromDB("square");


        try {
            return restTemplate.exchange(url + "/?x=" + x, HttpMethod.GET, HttpEntity.EMPTY, byte[].class);
        } catch(HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        }

    }

    @CrossOrigin
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode missingParameterExceptionHandler() {
        String stringOutput = "operator(o) parameter is required";
        return outputJsonResponse(true, stringOutput, null,400);
    }

    @CrossOrigin
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode operatorNotFoundExceptionHandler() {
        String stringOutput = "operator not found in database";
        return outputJsonResponse(true, stringOutput, null,400);
    }



}
