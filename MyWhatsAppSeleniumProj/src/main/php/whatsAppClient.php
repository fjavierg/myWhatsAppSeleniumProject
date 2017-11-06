<?php
require_once 'WhatsAppAPI.php';
require_once 'configuration.php';

$destinationNb = "34644016790";
$message = "TestPHP";

$api = new WhatsAppAPI();

echo $api->send($destinationNb,$message,$userId,$userSecret);

?>