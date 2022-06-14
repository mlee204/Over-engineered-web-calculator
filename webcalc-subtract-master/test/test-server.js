const chai = require('chai')
const chaiHttp = require('chai-http');
const sub = require('../subtract');
const server = require('../server');
const expect = chai.expect

chai.use(chaiHttp);

//http testing of Express app - assertions used
function httpResponseAssertionsTemplate(expectedStatus,expectedJson, response) {
    expect(response).to.have.status(expectedStatus);
    expect(response).to.be.json;
    expect(response).to.have.header('Content-Type', 'application/json');
    expect(response).to.have.header('Access-Control-Allow-Origin', '*');
    expect(response).to.have.property('body');
    expect(response.body).to.deep.equals(expectedJson);


}

describe('Test the Node.js server endpoints for Subtract with HTTP Request on localhost port 80 to test API', () => {

    context('missing both url parameters', function () {
        it('should return a HTTP Bad Request response, Json Format body', (done) => {
            let expectedJson= {'error': true, 'string': "Url Param 'x' is missing |Url Param 'y' is missing ",
                'answer': null, 'status': 400};
            let expectedStatus = 400

            chai.request(server)
                .get('/')
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();    // <= Call done to signal callback end
                });
        });
    })

    context('missing x url parameters', function () {
        it('should return a HTTP Bad Request response, Json Format body', (done) => {
            let y = 50
            let expectedJson= {'error': true, 'string': "Url Param 'x' is missing |",
                'answer': null, 'status': 400};
            let expectedStatus = 400

            chai.request(server)
                .get('/?y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();    // <= Call done to signal callback end
                });
        });
    })

    context('missing y url parameters', function () {
        it('should return a HTTP Bad Request response, Json Format body', (done) => {
            let x = 50
            let expectedJson= {'error': true, 'string': "Url Param 'y' is missing ",
                'answer': null, 'status': 400};
            let expectedStatus = 400

            chai.request(server)
                .get('/?x=' + x)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();    // <= Call done to signal callback end
                });
        });
    })
    context('test server with valid parameters ',  function () {
        it('should return a HTTP OK response, Json Answer field = 105', (done) => {
            let x = 50, y = 55
            let expectedAnswer = sub.subtract(x, y), expectedString = x + '-' + y + '=' + expectedAnswer,
                expectedStatus = 200
            let expectedJson = {'error': false, 'string': expectedString, 'answer': expectedAnswer,
                'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();   // <= Call done to signal callback end
                });
        });
    });

    context('test server with valid parameters negative number',  function () {
        it('should return a HTTP OK response, Json Answer field = -5', (done) => {
            let x = 50, y = -55
            let expectedAnswer = sub.subtract(x, y), expectedString = x + '-' + y + '=' + expectedAnswer,
                expectedStatus = 200
            let expectedJson = {'error': false, 'string': expectedString, 'answer': expectedAnswer,
                'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();   // <= Call done to signal callback end
                });
        });
    });

    context('test server x parameter invalid data type(String)', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let x = "bad", y = -55
            let expectedStatus = 400, expectedString = "Parameter x(" + x + ") is not a valid Integer "
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null,
                'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server y parameter invalid data type(String)', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let y = "bad", x = -55
            let expectedStatus = 400, expectedString = "Parameter y(" + y + ") is not a valid Integer"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null,
                'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server both parameter invalid data type(String)', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let y = "10D", x = "ss"
            let expectedStatus = 400, expectedString = "Parameter x(" + x + ") is not a valid Integer Parameter y(" + y + ") is not a valid Integer"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null,
                'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server http parameters invalid double', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let x = 34.4, y = 4.4
            let expectedStatus = 400, expectedString = "Parameter x(" + x + ") is not a valid Integer " + "Parameter y(" + y + ") is not a valid Integer"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null, 'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server http parameters invalid array', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let x = [22,22], y = [5,1]
            let expectedStatus = 400, expectedString = "Parameter x(" + x + ") is not a valid Integer " + "Parameter y(" + y + ") is not a valid Integer"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null, 'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server http both parameters invalid (x=double y=array)', () => {
        it('should return a HTTP Bad Request response', (done) => {
            let x = 34.4, y = [5,6,2,-4]
            let expectedStatus = 400, expectedString = "Parameter x(" + x + ") is not a valid Integer " + "Parameter y(" + y + ") is not a valid Integer"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null, 'status': expectedStatus};

            chai.request(server)
                .get('/?x=' + x + '&y=' + y)
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });

    context('test server http response page not found handling', () => {
        it('should return a a custom HTTP 404 Not Found - JSON response', (done) => {
            let expectedStatus = 404, expectedString = "Page not found: check url parameter syntax is correct"
            let expectedJson = {'error': true, 'string': expectedString, 'answer': null, 'status': expectedStatus};
            chai.request(server)
                .get('/page-not-found-test')
                .end(function (err, response) {
                    expect(err).to.be.null;

                    // call assertions
                    httpResponseAssertionsTemplate(expectedStatus,expectedJson, response)

                    done();  // <= Call done to signal callback end
                });
        });
    });


});

