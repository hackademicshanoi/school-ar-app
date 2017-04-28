<?php
 
include_once ('../user/User.php');
include_once ('../user/Authentication.php');
 
$auth = new \user\Authentication();
$auth->prepare($_POST);
 
$userStatus = $auth->isUserExisted();
 
if ($userStatus == false) {
    $user = new \user\User();
    $user->prepare($_POST);
    $user->insertNewUserIntoDB();
}else {
    $json['success'] = 0;
    $json['message'] = 'User exist';
 
    echo json_encode($json);
}
?>