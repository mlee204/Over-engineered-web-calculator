package com.qub.webcalcsqaure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@CrossOrigin(origins="*", methods=RequestMethod.GET, allowedHeaders="*")
@RestController
public class Webserver {

	public static void main(String[] args) {
		SpringApplication.run(Webserver.class, args);
	}

	ObjectMapper mapper = new ObjectMapper();
	public ObjectNode outputJsonResponse(boolean error, String string, Integer answer, int status) {
		ObjectNode jsonOutput = mapper.createObjectNode();
		jsonOutput.put("error", error);
		jsonOutput.put("string", string);
		jsonOutput.put("answer", answer);
		jsonOutput.put("status", status);
		return jsonOutput;
	}

	@GetMapping(path="/", produces = "application/json")
	public ObjectNode squareRoute(@RequestParam(value = "x") int x) {
		int answerOutput = Square.square(x);
		String stringOutput = x + "²" + "= " + answerOutput;
		return outputJsonResponse(false, stringOutput, answerOutput,200);
	}

	@GetMapping(path="/service-discovery", produces = "application/json")
	public ObjectNode serverDiscoverRoute() {
		ObjectNode jsonOutput = mapper.createObjectNode();
		jsonOutput.put("service", "square");
		jsonOutput.put("operator", "*");
		jsonOutput.put("parameter 1 data type", "Integer");
		jsonOutput.put("returns", "x*x");
		jsonOutput.put("return type", "Integer");
		return jsonOutput;


	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ObjectNode wrongDataTypeExceptionHandler(Exception exception, HttpServletRequest request) {
		String stringOutput = "parameter x is not a valid Integer";
		return outputJsonResponse(true, stringOutput, null,400);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ObjectNode missingParameterExceptionHandler(Exception exception, HttpServletRequest request) {
		String stringOutput = "x parameter is required";
		return outputJsonResponse(true, stringOutput, null,400);
	}

}
