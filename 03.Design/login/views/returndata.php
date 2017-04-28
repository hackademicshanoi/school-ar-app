<?php
 
include_once('../user/Authentication.php');
use user\Authentication;
 
$auth = new Authentication();
$auth->prepareToken($_POST);
$tokenStatus = $auth->isTokenExisted();

$firstName = $auth->returnFirstNameForEditAfterCheckingToken();
$lastName = $auth->returnLastNameForEditAfterCheckingToken();
$dateOfBirth = $auth->returnDateOfBirthForEditAfterCheckingToken();
$idSchool = $auth->returnIdSchoolForEditAfterCheckingToken();
$idSchool2 = $auth->returnIdSchool2ForEditAfterCheckingToken();
$profilePicture =  $auth->returnIdImageCheckingToken();
if ($tokenStatus) {
	$json['success'] = 1;
	$json['firstName'] = $firstName;
	$json['lastName'] = $lastName;
	$json['dateOfBirth'] = $dateOfBirth;
	$json['idSchool'] = $idSchool;
	if ($idSchool2!=0){
		$json['idSchool2'] = $idSchool2; 
	} 
	else {
		$json['idSchool2'] = "0";
	};
	if($profilePicture != ""){
		$json['profilePicture'] = $profilePicture;
	}
	else {
		$json['profilePicture'] = "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png";
	};
    echo json_encode($json);
} else {
	$json['success'] = 0;
	
	echo json_encode($json);
}
?>