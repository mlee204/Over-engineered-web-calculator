from flask import Flask
from flask import request
from flask import Response
import multiply
import json


def create_app():
    app = Flask(__name__)

    @app.route('/')
    def get_multiply():
        x_param = request.args.get('x')
        y_param = request.args.get('y')
        string_output = ""
        error_output = False

        if not x_param or not y_param:
            if not x_param:
                string_output += "x parameter is required; "
            if not y_param:
                string_output += "y parameter is required"
            output = {"error": True, "string": string_output, "answer": None, "status": 400}
            reply = json.dumps(output)
            r = Response(response=reply, status=400, mimetype="application/json")
            r.headers['Content-Type'] = "application/json"
            r.headers['Access-Control-Allow-Origin'] = "*"
            return r

        try:
            x_param = int(x_param)
        except ValueError:
            string_output += "parameter x(" + str(x_param) + ") is not a valid Integer-"
            error_output = True

        try:
            y_param = int(y_param)
        except ValueError:
            string_output += "parameter y(" + str(y_param) + ") is not a valid Integer"
            error_output = True

        if error_output:
            output = {"error": error_output, "string": string_output, "answer": None, "status": 400}
            reply = json.dumps(output)
            r = Response(response=reply, status=400, mimetype="application/json")
            r.headers['Content-Type'] = "application/json"
            r.headers['Access-Control-Allow-Origin'] = "*"
            return r

        answer = multiply.multiply_ints(x_param, y_param)
        string_output = str(x_param) + "*" + str(y_param) + "=" + str(answer)

        output = {"error": error_output, "string": string_output, "answer": answer, "status": 200}
        reply = json.dumps(output)
        r = Response(response=reply, status=200, mimetype="application/json")
        r.headers['Content-Type'] = "application/json"
        r.headers['Access-Control-Allow-Origin'] = "*"

        return r

    @app.errorhandler(404)
    def page_not_found(e):
        output = {"error": True, "string": "Page not found: check url parameter syntax is correct", "answer": None,
                  "status": 404}
        reply = json.dumps(output)
        r = Response(response=reply, status=404, mimetype="application/json")
        r.headers['Content-Type'] = "application/json"
        r.headers['Access-Control-Allow-Origin'] = "*"
        return r

    @app.route('/service-discovery')
    def service_discovery():
        output = {
            "service": "multiply",
            "url parameter 1": "x",
            "url parameter 2": "y",
            "operator": "*",
            "parameter 1 data type": "Integer",
            "parameter 2 data type": "Integer",
            "returns": "x*y",
            "return type": "Integer"}

        reply = json.dumps(output)
        r = Response(response=reply, status=200, mimetype="application/json")
        r.headers['Content-Type'] = "application/json"
        r.headers['Access-Control-Allow-Origin'] = "*"
        return r

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(host="0.0.0.0", port=5000)
