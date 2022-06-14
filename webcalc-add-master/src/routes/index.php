<?php

header("Access-Control-Allow-Origin: *");
header("Content-type: application/json");
require_once(__DIR__ . '/../functions.inc.php');
$output = array(
    "error" => false,
    "string" => "",
    "answer" => null,
    "status" => 200
);

$x = $_REQUEST['x'];
$y = $_REQUEST['y'];

$string_output = "";
$error_output = false;
$status_output = 200;
$answer_output = null;

// check if x and y parameters are set
if (!isset($x)) {
    $string_output .= "x parameter is required ";
    $error_output = true;
    $status_output = 400;

}

if (!isset($y)) {
    $string_output .= "y parameter is required ";
    $error_output = true;
    $status_output = 400;
}

// check if x and y are whole number integers
if (!isInteger($x) && isset($x)) {
    $string_output .= "parameter x(" . $x . ") is not a valid Integer ";
    $error_output = true;
    $status_output = 400;

} else {
    $x = (int)$x;
}

if (!isInteger($y) && isset($y)) {
    $string_output .= "parameter y(" . $y . ") is not a valid Integer";
    $error_output = true;
    $status_output = 400;
} else {
    $y = (int)$y;
}

if (!$error_output) {
    $answer_output = add($x, $y);
    $string_output .= $x . "+" . $y . "=" . $answer_output;
}

$output['string'] = $string_output;
$output['answer'] = $answer_output;
$output['error'] = $error_output;
$output['status'] = $status_output;

http_response_code($status_output);
echo json_encode($output);

