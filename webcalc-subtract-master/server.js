'use strict';

const express = require('express');

const PORT = 80;
const HOST = '0.0.0.0';
//test
const sub = require('./subtract');

const app = express();
app.get('/', (req,res) => {

    let output = {
        'error': false,
        'string': '',
        'answer': null,
        'status': 200
    };

    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Access-Control-Allow-Origin', '*')
    const x = req.query.x;
    const y = req.query.y;

    let stringMessages = "";
    let statusOutput = 200;
    let errorOutput = false;
    let answerOutput = null;

    if(x == null) {
        stringMessages += "Url Param 'x' is missing |"
        statusOutput = 400
        errorOutput = true
    }

    if(y == null) {
        stringMessages += "Url Param 'y' is missing "
        statusOutput = 400
        errorOutput = true
    }


    if (!isInteger(x) && x!= null) {
        stringMessages += "Parameter x(" + x + ") is not a valid Integer "
        statusOutput = 400
        errorOutput = true

    }

    if (!isInteger(y) && y!= null) {
        stringMessages += "Parameter y(" + y + ") is not a valid Integer"
        statusOutput = 400
        errorOutput = true

    }

    if (!errorOutput) {
        answerOutput = sub.subtract(x,y);
        stringMessages += x + '-' + y + '=' + answerOutput
        statusOutput = 200
    }

    output.string = stringMessages;
    output.answer = answerOutput;
    output.error = errorOutput;
    output.status = statusOutput;

    res.status(statusOutput);
    res.end(JSON.stringify(output));
});

app.get('/service-discovery', (req,res) => {
    let output = {
        "service":"subtract",
        "url parameter 1":"x",
        "url parameter 2":"y",
        "operator":"-",
        "parameter 1 data type":"Integer",
        "parameter 2 data type":"Integer","returns":"x-y",
        "return type":"Integer"
    };
    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Access-Control-Allow-Origin', '*')
    res.status(200);
    res.end(JSON.stringify(output));

});

// Handle 404 - Keep this as a last route
app.use(function(req, res) {
    let output = {
        'error': true,
        'string': "Page not found: check url parameter syntax is correct",
        'answer': null,
        'status': 404
    };
    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Access-Control-Allow-Origin', '*')
    res.status(404);
    res.end(JSON.stringify(output));
});


function isInteger(value) {
    return /^-?\d+$/.test(value);
}

let server = app.listen(PORT, HOST);
module.exports = server