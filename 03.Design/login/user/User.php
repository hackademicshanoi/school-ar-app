<?php
/**
 * Created by PhpStorm.
 * User: Shaon
 * Date: 2/2/2017
 * Time: 1:25 AM
 */
 
namespace user;
 
 
class User
{
    private $EMAIL;
    private $PASSWORD;
	private $FIRST_NAME;
    private $LAST_NAME;
	private $DATE_OF_BIRTH;
    private $ID_SCHOOL;
	private $ID_SCHOOL2;
	private $PROFILE_PICTURE;
    private $TOKEN;
 
    private $DB_CONNECTION;
    private $serverName = "localhost";
    private $userNameForDB = "root";
    private $passwordOfUserForDB = "";
    private $databaseName = "uar";
 
    function __construct()
    {
        $this->DB_CONNECTION = mysqli_connect($this->serverName,
            $this->userNameForDB, $this->passwordOfUserForDB,
            $this->databaseName);
    }
 
    function prepare($data) {
        if(array_key_exists('email', $data))
            $this->EMAIL = $data['email'];
        if(array_key_exists('password', $data))
            $this->PASSWORD = $data['password'];
		if(array_key_exists('first_name', $data))
            $this->FIRST_NAME = $data['first_name'];
        if(array_key_exists('last_name', $data))
            $this->LAST_NAME = $data['last_name'];
		if(array_key_exists('date_of_birth', $data))
            $this->DATE_OF_BIRTH = $data['date_of_birth'];
        if(array_key_exists('id_school', $data))
            $this->ID_SCHOOL = $data['id_school'];
		if(array_key_exists('profile_picture', $data))
            $this->PROFILE_PICTURE = $data['profile_picture'];
        if(array_key_exists('token', $data))
            $this->TOKEN = $data['token'];
    }
	
	function prepareEdit($data) {
        if(array_key_exists('first_name', $data))
            $this->FIRST_NAME = $data['first_name'];
        if(array_key_exists('last_name', $data))
            $this->LAST_NAME = $data['last_name'];
		if(array_key_exists('date_of_birth', $data))
            $this->DATE_OF_BIRTH = $data['date_of_birth'];
        if(array_key_exists('id_school', $data))
            $this->ID_SCHOOL = $data['id_school'];
		if(array_key_exists('id_school2', $data))
            $this->ID_SCHOOL2 = $data['id_school2'];
        if(array_key_exists('password', $data))
            $this->PASSWORD = $data['password'];
		if(array_key_exists('token', $data))
            $this->TOKEN = $data['token'];
    }
 
    function insertNewUserIntoDB () {
       
		$sql = "INSERT INTO `users`( `email`, `password`, `first_name`, `last_name`, `data_of_birth`, `id_school1`,`profile_picture`, `token`) VALUES ('" . $this->EMAIL . "','" . $this->PASSWORD . "','" . $this->FIRST_NAME . "','" . $this->LAST_NAME . "','" . $this->DATE_OF_BIRTH . "', '" . $this->ID_SCHOOL . "','" . $this->PROFILE_PICTURE . "','" . $this->EMAIL . $this->PASSWORD . "' )";
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if ($result) {
            $json['success'] = 1;
            $json['message'] ='Sign Up Successful';
			$json['token'] =$this->EMAIL.$this->PASSWORD;
 
            echo json_encode($json);
        } else {
            $json['success'] = 0;
            $json['message'] ='Sign Up was not Successful';
 
            echo json_encode($json);
        }
 
 
    }
	
	function updateUserInfo () {
		$sqlEmail = "SELECT `email` FROM `users` WHERE token = '". $this->TOKEN."'"; 
        $resultEmail = mysqli_query($this->DB_CONNECTION, $sqlEmail);
		$rowEmail = mysqli_fetch_array($resultEmail);
		
//		if ($_POST['password']!="")
//		{
		
        $sql = "UPDATE `users` (`first_name`, `last_name`, `date_of_birth`, `id_school`, `id_school2`, `password` ) 
        VALUES ( '" . $this->FIRST_NAME . "', '" . $this->LAST_NAME . "', '" . $this->DATE_OF_BIRTH . "', '" . $this->ID_SCHOOL . "', '" . $this->ID_SCHOOL2 . "', '" . $this->PASSWORD . "' )
		WHERE token = '". $this->TOKEN."'"; 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
		
		$sqlToken = "SELECT `token` FROM `users` WHERE email = '". $rowEmail['email']."'"; 
        $resultToken = mysqli_query($this->DB_CONNECTION, $sqlToken);
		$rowToken = mysqli_fetch_array($resultToken);
 
        if ($result) {
            $json['success'] = 1;
            $json['message'] ='Edit Successful';
			$json['token'] = $rowToken['token'];
 
            echo json_encode($json);
        } else {
            $json['success'] = 0;
            $json['message'] ='Edit was not Successful';
 
            echo json_encode($json);
        }
//		}
 
 
    }
 
 
}
?>