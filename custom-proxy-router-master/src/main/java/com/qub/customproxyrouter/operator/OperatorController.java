package com.qub.customproxyrouter.operator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/operators")
public class OperatorController {

    private final OperatorService operatorService;

    @Autowired
    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    public List<Operator> getOperators() {
        return operatorService.getOperators();
    }

    @PostMapping
    public ObjectNode registerNewOperator(@RequestBody Operator operator) {
        if (operator== null) {
            throw new IllegalStateException("Request Body JSON is missing");
        }
        return operatorService.addNewOperator(operator);
    }

    @DeleteMapping(path = "{operatorName}")
    public ObjectNode deleteOperator(@PathVariable("operatorName") String operatorName) {
        return operatorService.deleteOperator(operatorName);
    }

    @PutMapping(path = "{operatorId}")
    public ObjectNode updateOperator(
            @PathVariable("operatorId") Long operatorId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String url) {
        return operatorService.updateOperator(operatorId,name, url);

    }

    @CrossOrigin
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode operatorRequestBodyEmptyHandler() {
        OperatorService operatorService = new OperatorService();
        String stringOutput = "please include request body as JSON format";
        return  operatorService.outputJsonResponse(true, stringOutput,400);
    }

    @CrossOrigin
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ObjectNode operatorMethodArgumentTypeMismatchHandler() {
        OperatorService operatorService = new OperatorService();
        String stringOutput = "Invalid URL path variable";
        return  operatorService.outputJsonResponse(true, stringOutput,400);
    }
}
