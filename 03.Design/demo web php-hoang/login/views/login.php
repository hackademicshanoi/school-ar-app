<?php
/**
 * Created by PhpStorm.
 * User: Shaon
 * Date: 2/2/2017
 * Time: 1:25 AM
 */
 
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepare($_POST);
$userStatus = $auth->isUserValidToLogIn();
$userStatus1 = $auth->isUserNotExisted();
 
 
if ($userStatus) {
    // user existed
    // So log him to main page
    $json['success'] = 1;
    $json['message'] = 'User Successfully logged';
 
 
    echo json_encode($json);
} else if ($userStatus1) {
	$json['success'] = 0;
    $json['message'] = 'Wrong email';
	
 
    echo json_encode($json);
} else {

    $json['success'] = 0;
    $json['message'] = 'Wrong password';
 
 
    echo json_encode($json);
}
?>