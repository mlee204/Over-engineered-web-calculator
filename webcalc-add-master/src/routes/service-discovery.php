<?php
header("Access-Control-Allow-Origin: *");
header("Content-type: application/json");


$output = array(
    "service" => "add",
    "url parameter 1" => "x",
    "url parameter 2" => "y",
    "operator" => "+",
    "parameter 1 data type" => "Integer",
    "parameter 2 data type" => "Integer",
    "returns" => "x+y",
    "return type" => "Integer"
);
http_response_code(200);
echo json_encode($output);



