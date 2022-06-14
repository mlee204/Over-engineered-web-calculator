<?php
header("Access-Control-Allow-Origin: *");
header("Content-type: application/json");

$output = array(
    "error" => true,
    "string" => "Page not found: check url parameter syntax is correct",
    "answer" => null,
    "status" => 404
);
http_response_code(404);
echo json_encode($output);
exit();