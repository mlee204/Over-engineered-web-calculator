package com.qub.webcalcsqaure;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PageNotFoundErrorController implements ErrorController {

    private Webserver webserver = new Webserver();


    @Override
    public String getErrorPath() {
        return "/error";
    }


    @RequestMapping("/error")
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ObjectNode handlePageNotFoundError(HttpServletRequest request) {

        String stringOutput = "Page not found: check url parameter syntax is correct";
        return webserver.outputJsonResponse(true, stringOutput, null, 404);

    }
}
