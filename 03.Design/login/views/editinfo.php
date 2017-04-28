<?php
 
include_once ('../user/User.php');
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepareToken($_POST);
$tokenStatus = $auth->isTokenExisted();
 
 
if ($tokenStatus) {
    // user existed
    // So log him to main page
	$user = new \user\User();
    $user->prepareEdit($_POST);
    $user->updateUserInfo();
	
}
?>