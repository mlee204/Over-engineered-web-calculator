package com.qub.customproxyrouter.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qub.customproxyrouter.operator.Operator;
import com.qub.customproxyrouter.operator.OperatorRepository;
import com.qub.customproxyrouter.operator.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping(path = "/service-discovery", produces = "application/json")
public class ServiceDiscovery {

    private OperatorService operatorService;
    private OperatorRepository operatorRepository;
    private RestTemplate restTemplate;
    protected String operatorNameString;

    @Autowired
    public ServiceDiscovery(OperatorService operatorService, OperatorRepository operatorRepository, RestTemplate restTemplate) {
        this.operatorService = operatorService;
        this.operatorRepository = operatorRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<byte[]> getServiceInfo(
            @RequestParam(value = "operator") String operator) throws ServletRequestBindingException {


        Optional<Operator> operatorName = operatorRepository.findOperatorsByName(operator);

        if (!operatorName.isPresent()) {
            operatorNameString = operator;
            throw new ServletRequestBindingException("operator not found in Database");
        }

        String url = operatorService.getUrlEndPointOfOperatorFromDB(operator);
        try {
            return restTemplate.exchange(url + "/service-discovery?operator=" + operator, HttpMethod.GET, HttpEntity.EMPTY, byte[].class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        }

    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode missingParameterExceptionHandler() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonOutput = mapper.createObjectNode();
        jsonOutput.put("error", true);
        jsonOutput.put("message", "Please enter operator parameter");

        return jsonOutput;
    }


    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode operatorNotFoundExceptionHandler() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonOutput = mapper.createObjectNode();
        jsonOutput.put("error", true);
        jsonOutput.put("message", "There is no service for operator: '" + operatorNameString + "'");
        return jsonOutput;
    }



}
