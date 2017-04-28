<?php
 
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepareToken($_POST);
$tokenStatus = $auth->isTokenExisted();
$tokenStatus1 = $auth->isTokenNotExisted();
 
 
if ($tokenStatus) {
    // user existed
    // So log him to main page
    $json['success'] = 1;
    $json['message'] = 'Token existed';
 
 
    echo json_encode($json);
} else if ($tokenStatus1) {
	$json['success'] = 0;
    $json['message'] = 'Token not existed';
	
 
    echo json_encode($json);
}
?>