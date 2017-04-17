<?php
 
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepare($_POST);
$userStatus = $auth->isUserValidToLogIn();
$userStatus1 = $auth->isUserNotExisted();
$userSSS = $auth->returnTokenForLogin();
 
 
if ($userStatus) {
    // user existed
    // So log him to main page
    $json['success'] = 1;
    $json['message'] = 'User Successfully logged';
	$json['token'] = $userSSS;
 
 
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