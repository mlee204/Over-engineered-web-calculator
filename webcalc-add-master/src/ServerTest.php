<?php

use GuzzleHttp\Exception\GuzzleException;
use PHPUnit\Framework\TestCase;

use GuzzleHttp\Client;

include 'functions.inc.php';


final class ServerTest extends TestCase
{


    public function testServerNoUrlParameters(): void
    {
        $client = new Client();

        try {
            $response = $client->get('http://localhost/');
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }

        $expect_response = array(
            "error" => true,
            "string" => "x parameter is required y parameter is required ",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerNoXParameters(): void
    {
        $client = new Client();
        $y = 50;
        try {
            $response = $client->get('http://localhost',['query' => ['y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "x parameter is required ",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerNoYParameters(): void
    {
        $client = new Client();
        $x = 50;

        try {
            $response = $client->get('http://localhost', ['query' => ['x' => $x]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "y parameter is required ",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerInvalidUrlParametersTypeString(): void
    {
        $client = new Client();
        $x = "bad";
        $y = "string";


        try {
            $response = $client->get('http://localhost', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "parameter x(".$x.") is not a valid Integer parameter y(" . $y . ") is not a valid Integer",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerInvalidXUrlParameterTypeString(): void
    {
        $client = new Client();
        $x = "bad";
        $y = 55;


        try {
            $response = $client->get('http://localhost', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "parameter x(".$x.") is not a valid Integer ",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerInvalidYUrlParameterTypeString(): void
    {
        $client = new Client();
        $y = "bad";
        $x = 55;


        try {
            $response = $client->get('http://localhost', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "parameter y(".$y.") is not a valid Integer",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerInvalidUrlParametersTypeDouble(): void
    {
        $client = new Client();
        $x = 55.55;
        $y = 2;

        try {
            $response = $client->get('http://localhost', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "parameter x(".$x.") is not a valid Integer ",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerInvalidUrlParametersTypeArray(): void
    {
        $client = new Client();
        $x = 33;
        $y = [33];

        try {
            $response = $client->get('http://localhost:80', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "parameter y(".$y.") is not a valid Integer",
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerUrlParametersEmpty(): void
    {
        $client = new Client();
        $x = null;
        $y = null;

        try {
            $response = $client->get('http://localhost:80', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => 'x parameter is required y parameter is required ',
            "answer" => null,
            "status" => 400
        );

        $this->assertEquals(400, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerValidParameters(): void
    {
        $client = new Client();
        $x = 44;
        $y = 33;
        $answer_output=add($x,$y);
        try {
            $response = $client->get('http://localhost:80', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => false,
            "string" => $x."+".$y."=".$answer_output,
            "answer" => $answer_output,
            "status" => 200
        );

        $this->assertEquals(200, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerValidParametersNegative(): void
    {
        $client = new Client();
        $x = -44;
        $y = -33;
        $answer_output=add($x,$y);
        try {
            $response = $client->get('http://localhost:80', ['query' => ['x' => $x, 'y' => $y]]);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => false,
            "string" => $x."+".$y."=".$answer_output,
            "answer" => $answer_output,
            "status" => 200
        );

        $this->assertEquals(200, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

    public function testServerPageNotFound(): void
    {
        $client = new Client();

        $not_found_path = 'page-not-found-test';

        try {
            $response = $client->get('http://localhost:80/'.$not_found_path);
        } catch (GuzzleException $e) {
            $response = $e->getResponse();
        }
        $expect_response = array(
            "error" => true,
            "string" => "Page not found: check url parameter syntax is correct",
            "answer" => null,
            "status" => 404
        );

        $this->assertEquals(404, $response->getStatusCode());
        $this->assertEquals("application/json", $response->getHeaderLine("Content-Type"));
        $this->assertEquals("*", $response->getHeaderLine("Access-Control-Allow-Origin"));
        $this->assertJson($response->getBody());
        $this->assertEquals($expect_response, json_decode($response->getBody(), true));

    }

}