import json
import os
import datetime
from flask import Flask, Response
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow

app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))

app.debug = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://operator:operator@postgres-db-40151615/operator'
#app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://operator:operator@localhost/operator'

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

# Init ma
ma = Marshmallow(app)


class MemoryRecall(db.Model):
    __tablename__ = 'memory_recall'
    id = db.Column(db.Integer, primary_key=True)
    number = db.Column(db.Integer)

    timestamp = db.Column(db.TIMESTAMP, default=datetime.datetime.now)

    def __init__(self, number):
        self.number = number


class MemoryRecallSchema(ma.Schema):
    class Meta:
        fields = ('id', 'number', 'timestamp')


memory_recall_schema = MemoryRecallSchema()
memory_recalls_schema = MemoryRecallSchema(many=True)
db.create_all()
db.session.commit()


def return_response_recall(error, value, status, message):
    output = {'error': error, 'value': value, 'status': status, 'string': message}
    reply = json.dumps(output, sort_keys=True)
    r = Response(response=reply, status=status, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"
    return r


@app.route('/<number_id>', methods=['GET'])
def recall_value(number_id):
    try:
        number_id = int(number_id)
        db_query_output = MemoryRecall.query.get(number_id)
        status = 200
        error = False
    except ValueError:
        status = 400
        value = None
        error = True
        message = "can only recall values of type Integer"

        return return_response_recall(error, value, status, message)

    # check provide ID is stored in DB
    if db_query_output is None and error is False:
        status = 400
        value = None
        error = True
        message = "No valued stored under the ID:" + str(number_id)
    else:
        value = db_query_output.number
        message = "ID:" + str(number_id) + " has recalled value:" + str(value)
        return_response_recall(error, value, status, message)

    return return_response_recall(error, value, status, message)


@app.route('/<number_to_store>', methods=['POST'])
def store_new_value_memory(number_to_store):
    try:
        number_to_store = int(number_to_store)
        new_memory_record = MemoryRecall(number_to_store)
        db.session.add(new_memory_record)
        db.session.commit()
        reference_id = new_memory_record.id
        string_output = "Value:" + str(number_to_store) + " stored successfully and assigned ID:" + str(reference_id)
        error_output = False
        status = 201

    except ValueError:
        string_output = "path parameter(" + str(number_to_store) + ") is not a valid Integer"
        error_output = True
        status = 400
        reference_id = None

    data = {
        'reference_id': reference_id,
        'error': error_output,
        'status': status,
        'string': string_output,
    }

    reply = json.dumps(data, sort_keys=True)
    r = Response(response=reply, status=status, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"
    return r


@app.route('/all-values', methods=['GET'])
def get_all_stored_values():
    all_metrics = MemoryRecall.query.all()
    result = memory_recalls_schema.dump(all_metrics)
    reply = json.dumps(result, sort_keys=True)
    r = Response(response=reply, status=200, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"

    return r


@app.errorhandler(404)
def page_not_found(e):
    output = {"error": True, "string": "Page not found, path variable must of type integer",
              "status": 404}
    reply = json.dumps(output)
    r = Response(response=reply, status=404, mimetype="application/json")
    r.headers['Content-Type'] = "application/json"
    r.headers['Access-Control-Allow-Origin'] = "*"
    return r


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)
