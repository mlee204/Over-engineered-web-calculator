import multiprocessing
import pytest
from flask import url_for
import requests
import multiply


# http testing of flask app
def response_assertions_template(expected_status, expected_json, response):
    assert response.status_code == expected_status
    assert response.headers.get("Content-Type") == "application/json"
    assert response.headers.get("Access-Control-Allow-Origin") == "*"
    assert response.json() == expected_json
    assert int(response.headers.get("Content-Length")) == len(str(expected_json))


@pytest.mark.usefixtures('live_server')
class TestApp:
    multiprocessing.set_start_method("fork")

    def test_server_no_parameters(self):
        expected_error = True
        expected_status = 400
        expected_string = "x parameter is required; y parameter is required"
        expected_json = {"error": expected_error, "string": expected_string, "answer": None, "status": expected_status}

        # request app url
        response = requests.get(url_for('get_multiply', _external=True))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_no_x_parameters(self):
        y_param = 3
        expected_error = True
        expected_status = 400
        expected_string = "x parameter is required; "
        expected_json = {"error": expected_error, "string": expected_string, "answer": None, "status": expected_status}

        # request app url
        response = requests.get(url_for('get_multiply', _external=True, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_no_y_parameters(self):
        x_param = 3
        expected_error = True
        expected_status = 400
        expected_string = "y parameter is required"
        expected_json = {"error": expected_error, "string": expected_string, "answer": None, "status": expected_status}

        # request app url
        response = requests.get(url_for('get_multiply', _external=True, x=x_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_valid_parameters(self):
        x_param = 5
        y_param = 3
        expected_error = False
        expected_status = 200
        expected_answer = multiply.multiply_ints(x_param, y_param)
        expected_string = str(x_param) + "*" + str(y_param) + "=" + str(expected_answer)
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer, "status": expected_status}

        # http get request app url with valid parameters
        response = requests.get(url_for('get_multiply', _external=True, x=x_param, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_valid_parameters_negative(self):
        x_param = -5
        y_param = -3
        expected_error = False
        expected_status = 200
        expected_answer = multiply.multiply_ints(x_param, y_param)
        expected_string = str(x_param) + "*" + str(y_param) + "=" + str(expected_answer)
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer, "status": expected_status}

        # http get request app url with valid parameters
        response = requests.get(url_for('get_multiply', _external=True, x=x_param, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_x_parameter_invalid_string(self):
        x_param = "bad"
        y_param = 3
        expected_error = True
        expected_status = 400
        expected_answer = None
        expected_string = "parameter x(" + x_param + ") is not a valid Integer-"
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer,
                         "status": expected_status}

        # http get request app url with x parameters invalid
        response = requests.get(url_for('get_multiply', _external=True, x=x_param, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_both_parameter_invalid_string(self):
        x_param = "bad"
        y_param = "string"
        expected_error = True
        expected_status = 400
        expected_answer = None
        expected_string = "parameter x(" + x_param + ") is not a valid Integer-parameter y(" + y_param + ") is not a valid Integer"
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer,
                         "status": expected_status}

        # request app url with parameters x and y
        response = requests.get(url_for('get_multiply', _external=True, x=x_param, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_both_parameter_invalid_float(self):
        x_param = 5.5
        y_param = 4.4
        expected_error = True
        expected_status = 400
        expected_answer = None
        expected_string = "parameter x(" + str(x_param) + ") is not a valid Integer-parameter y(" + str(y_param) + \
                          ") is not a valid Integer"
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer,
                         "status": expected_status}

        response = requests.get(url_for('get_multiply', _external=True, x=x_param, y=y_param))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_parameter_invalid_array(self):
        x_param = [5.5, 9]
        y_param = [4.4, 10]
        expected_error = True
        expected_status = 400
        expected_answer = None
        expected_string = "parameter x(" + str(x_param) + ") is not a valid Integer-parameter y(" + str(y_param) + \
                          ") is not a valid Integer"
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer,
                         "status": expected_status}

        response = requests.get(url_for('get_multiply', _external=True, x=[x_param], y=[y_param]))

        # call assertions
        response_assertions_template(expected_status, expected_json, response)

    def test_server_page_not_found(self):
        expected_error = True
        expected_status = 404
        expected_answer = None
        expected_string = "Page not found: check url parameter syntax is correct"
        expected_json = {"error": expected_error, "string": expected_string, "answer": expected_answer,
                         "status": expected_status}

        # http get request app url/page-not-found-test
        response = requests.get(url_for('get_multiply', _external=True) + "/page-not-found-test")

        # call assertions
        response_assertions_template(expected_status, expected_json, response)
