package com.qub.customproxyrouter.operator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OperatorService {

    private OperatorRepository operatorRepository;

    @Autowired
    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }
    public OperatorService() { }

    public List<Operator> getOperators() {
     return operatorRepository.findAll();

    }

    public ObjectNode addNewOperator(Operator operator) {
      Optional<Operator> operatorsByName = operatorRepository.findOperatorsByName(operator.getName());

        if(operator.getName() == null || operator.getUrls() == null) {
            String outputString = "'name' or 'urls' key from request JSON is missing";
            return outputJsonResponse(true,outputString, 400);
        }

        if(operatorsByName.isPresent()) {
            String outputString = "Operator " + operator.getName() + " already exists the url for it is:" + operator.getUrls();
            return outputJsonResponse(true,outputString, 400);
        }

        operatorRepository.save(operator);

        String outputString = "Operator:" + operator.getName() + " added to DB successfully";
        System.out.println(operator);
        return outputJsonResponse(false,outputString, 201);

    }

    public ObjectNode deleteOperator(String operatorName) {

        Optional<Operator> operator = operatorRepository.findOperatorsByName(operatorName);

        if(!operator.isPresent()) {
            return outputJsonResponse(true,"No Operator named:" + operatorName + " found in DB", 400);
        }
        operatorRepository.deleteById(operator.get().getId());

        return outputJsonResponse(false,"Operator named:" + operatorName + " deleted successfully", 200);

     }

    @Transactional
    public ObjectNode updateOperator(Long operatorId, String name, String url) {
        boolean operatorPresent = operatorRepository.existsById(operatorId);

        if(!operatorPresent) {
            return outputJsonResponse(true,"No operator found for ID:" + operatorId, 400);
        }

        if (name == null && url == null) {
            return outputJsonResponse(true,"Please provide url parameter 'name' or 'urls' inorder to update DB", 400);
        }

        Operator operator = operatorRepository.findById(operatorId).get();

        if(name != null && name.length() > 0 && !Objects.equals(operator.getName(), name)) {
            operator.setName(name);

        }

        if(url != null && url.length() > 0 && !Objects.equals(operator.getUrls(), url)) {
            Optional<Operator> operatorOptional = operatorRepository.findOperatorsByUrls(url);

            if(operatorOptional.isPresent()) {
                return outputJsonResponse(true,"URL:" + url + " already present", 400);
            }

            operator.setUrls(url);
        }

        return outputJsonResponse(false,"Operator ID:" + operatorId + " updated successfully", 200);
    }

    public String getUrlEndPointOfOperatorFromDB(String operatorName) {

        Optional<Operator> operator = operatorRepository.findOperatorsByName(operatorName);

        if(operator.isPresent()) {
            return operator.get().getUrls();
        }


        return null;
    }

    public ObjectNode outputJsonResponse(boolean error, String string, int status) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("error", error);
        objectNode.put("string", string);
        objectNode.put("status", status);
        return objectNode;
    }
}
