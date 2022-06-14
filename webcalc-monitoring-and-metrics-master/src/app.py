import json

import datetime
import statistics
from flask import Flask, Response, render_template
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
import requests
import os
from operation_parse import get_operator_calculate_value
import random
import error_email

app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))

app.debug = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://operator:operator@postgres-db-40151615/operator'
#app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://operator:operator@localhost/operator'

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

# Init ma
ma = Marshmallow(app)


class Metrics(db.Model):
    __tablename__ = 'metrics'
    id = db.Column(db.Integer, primary_key=True)
    service = db.Column(db.String(200))
    operator = db.Column(db.String(1))
    parma1 = db.Column(db.Integer)
    parma2 = db.Column(db.Integer)
    expected = db.Column(db.Integer)
    actual = db.Column(db.Integer)
    correct = db.Column(db.Boolean)
    response_time = db.Column(db.Float)
    timestamp = db.Column(db.DateTime, default=datetime.datetime.now())
    response_status = db.Column(db.Integer)
    test_excitation = db.Column(db.Boolean)
    test_feedback = db.Column(db.String(200))

    def __init__(self, service, operator, parma1, parma2, expected, actual, correct, response_time, response_status,
                 test_excitation, test_feedback):
        self.service = service
        self.operator = operator
        self.parma1 = parma1
        self.parma2 = parma2
        self.expected = expected
        self.actual = actual
        self.correct = correct
        self.response_time = response_time
        self.response_status = response_status
        self.test_excitation = test_excitation
        self.test_feedback = test_feedback


class Operators(db.Model):
    __tablename__ = 'operator'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(200), unique=True)
    urls = db.Column(db.String(200), unique=True)

    def __init__(self, name, urls):
        self.name = name
        self.urls = urls


class OperatorSchema(ma.Schema):
    class Meta:
        fields = ('id', 'name', 'urls')


class MetricsSchema(ma.Schema):
    class Meta:
        fields = (
            'id', 'service', 'operator', 'parma1', 'parma2', 'expected', 'actual', 'correct', 'response_time',
            'response_status', 'test_excitation', 'test_feedback','timestamp')


operator_schema = OperatorSchema()
operators_schema = OperatorSchema(many=True)

metric_schema = MetricsSchema()
metrics_schema = MetricsSchema(many=True)

db.create_all()
db.session.commit()


@app.route('/')
def dashboard():
    response_time = [r.response_time for r in db.session.query(Metrics.response_time)]
    correct_array = [r.correct for r in db.session.query(Metrics.correct)]

    avg_response_time = statistics.mean(response_time)
    min_value = min(response_time)
    max_value = max(response_time)

    true_count = sum(correct_array)
    false_count = len(correct_array) - true_count
    true_count = sum(correct_array)

    time_array = {
        'avg': round(avg_response_time, 4),
        'min': round(min_value, 4),
        'max': round(max_value, 4)
    }

    response_time = json.dumps(response_time, sort_keys=True)

    stamps = [r.timestamp.strftime("%Y-%m-%d %H:%M:%S") for r in db.session.query(Metrics.timestamp)]
    stamps = json.dumps(stamps, sort_keys=True)

    test_result = {
        'per': round((true_count / len(correct_array)) * 100, 2),
        'passed': true_count,
        'failed': false_count
    }

    response_time_add = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='add').all()]
    response_time_add_avg = statistics.mean(response_time_add)

    response_time_mul = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='multiply').all()]
    response_time_mul_avg = statistics.mean(response_time_mul)

    response_time_sub = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='subtract').all()]
    response_time_sub_avg = statistics.mean(response_time_sub)

    response_time_mod = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='modulo').all()]
    response_time_mod_avg = statistics.mean(response_time_mod)

    response_time_squ = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='square').all()]
    response_time_squ_avg = statistics.mean(response_time_squ)

    response_time_div = [r.response_time for r in
                         db.session.query(Metrics.response_time).filter_by(service='divide').all()]
    response_time_div_avg = statistics.mean(response_time_div)

    per_service = {
        'add': round(response_time_add_avg * 1000, 1),
        'mul': round(response_time_mul_avg * 1000, 1),
        'squ': round(response_time_squ_avg * 1000, 1),
        'mod': round(response_time_mod_avg * 1000, 1),
        'sub': round(response_time_sub_avg * 1000, 1),
        'div': round(response_time_div_avg * 1000, 1)
    }

    raw = {
        'add': response_time_add,
        'mul': response_time_mul,
        'squ': response_time_squ,
        'mod': response_time_mod,
        'sub': response_time_sub,
        'div': response_time_div
    }

    latest_result = Metrics.query.order_by(Metrics.id.desc()).filter_by(correct=False).first()

    latest = {
        'date': latest_result.timestamp.strftime("%m-%d %H:%M"),
        'service': latest_result.service,
        'time': latest_result.response_time,
        'expected': latest_result.expected,
        'actual': latest_result.actual
    }

    per_service['max'] = max(per_service, key=per_service.get)
    per_service['worst'] = per_service[per_service['max']]

    return render_template('index.html', time=response_time, stats=time_array, test=test_result,
                           per_service=per_service, latest=latest, raw=raw)


@app.route('/test-service', methods=['POST'])
def test_functions():
    all_operators = Operators.query.all()
    result = operators_schema.dump(all_operators)
    error_flag = False

    for service in result:
        service_name = service['name']
        response = requests.get(service['urls'] + "/service-discovery")

        if response.ok is False:
            error_email.send_error_email(service_name, "Status 200", response.status_code,
                                         response.elapsed.total_seconds(), "Service discovery failed")

            new_metrics = Metrics(service_name, None, None, None, None, None, False,
                                  response.elapsed.total_seconds(), response.status_code, False,
                                  "Service discovery failed")
            error_flag = True
            db.session.add(new_metrics)
            db.session.commit()
            continue

        response_json = response.json()

        service_operator = response_json['operator']

        x = random.randint(-9999, 9999)
        y = random.randint(-9999, 9999)

        if service_name == "square":
            expected_result = x * x
            y = None

        else:
            expected_result = get_operator_calculate_value(service_operator, x, y)

        response_actual = requests.get(service['urls'] + "/?x=" + str(x) + "&y=" + str(y))

        if response_actual.ok is False:
            error_flag = True
            error_email.send_error_email(service_name, "Status 200", response_actual.status_code,
                                         response_actual.elapsed.total_seconds(), "invalid response for maths function " +
                                         service_name)
            new_metrics = Metrics(service_name, service_operator, x, y, expected_result, response_actual.answer, False,
                                  response_actual.elapsed.total_seconds(), response.status_code, False,
                                  "invalid response from maths function")
            db.session.add(new_metrics)
            db.session.commit()
            continue

        actual_json = response_actual.json()
        actual_result = actual_json['answer']

        response_time = response_actual.elapsed.total_seconds()

        correct = True
        test_execution = True
        status_code = response_actual.status_code

        if expected_result != actual_result:
            correct = False
            error_flag = True
            error_email.send_error_email(service_name, expected_result, actual_result, response_time,
                                         "Actual Answer did not Match Expected Answer")

        new_metrics = Metrics(service_name, service_operator, x, y, expected_result, actual_result, correct,
                              response_time, status_code, test_execution, "Tested Passed")
        db.session.add(new_metrics)
        db.session.commit()

    data = {'test_error': error_flag, 'status': 201}
    reply = json.dumps(data, sort_keys=True)
    r = Response(response=reply, status=201, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"
    return r



@app.route('/metrics', methods=['GET'])
def get_metrics():
    all_metrics = Metrics.query.all()
    result = metrics_schema.dump(all_metrics)
    reply = json.dumps(result, sort_keys=True)
    r = Response(response=reply, status=200, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"

    return r


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)
