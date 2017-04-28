<?php
 
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepareEmail($_POST);
$emailStatus = $auth->isUserExisted();
 
 
if ($emailStatus) {
    // user existed
    // So log him to main page
    $json['success'] = 1;
    $json['message'] = 'Sending email successful';
 
 
    echo json_encode($json);
} else {
	$json['success'] = 0;
    $json['message'] = 'Sending email failed';
	
 
    echo json_encode($json);
}
?>