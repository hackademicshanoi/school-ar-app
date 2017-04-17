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
 
if ($tokenStatus) {
	$json['success'] = 1;
	$json['firstName'] = $firstName;
	$json['lastName'] = $lastName;
	$json['dateOfBirth'] = $dateOfBirth;
	$json['idSchool'] = $idSchool;
	if ($idSchool2!=0){
	$json['idSchool2'] = $idSchool2; 
	} else {
		$json['idSchool2'] = "0"; 
	}
 
    echo json_encode($json);
} else {
	$json['success'] = 0;
	
	echo json_encode($json);
}
?>