<?php
header("Access-Control-Allow-Origin: *");
header("Content-type: application/json");
$url = $_SERVER['REQUEST_URI'];
$url_path = parse_url($url, PHP_URL_PATH);

switch ($url_path) {
    case '':
    case '/' :
        require __DIR__ . '/routes/index.php';
        break;
    case '/service-discovery' :
        require __DIR__ . '/routes/service-discovery.php';
        break;
    default:
        require __DIR__ . '/routes/error404Response.php';
        break;
}



