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
	private $PROFILE_PICTURE;
    private $TOKEN;
 
    private $DB_CONNECTION;
    private $serverName = "localhost";
    private $userNameForDB = "root";
    private $passwordOfUserForDB = "root";
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
 
    function insertNewUserIntoDB () {
        $sql = "INSERT INTO `users` (`email`, `password`, `first_name`, `last_name`, `date_of_birth`, `id_school`, `profile_picture`, `token` ) 
        VALUES ( '" . $this->EMAIL . "', '" . $this->PASSWORD . "', '" . $this->FIRST_NAME . "', '" . $this->LAST_NAME . "', 
		'" . $this->DATE_OF_BIRTH . "', '" . $this->ID_SCHOOL . "', '" . $this->PROFILE_PICTURE . "', '" . $this->EMAIL . $this->PASSWORD . "' )";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if ($result) {
            $json['success'] = 1;
            $json['message'] ='Sign Up Successful';
 
            echo json_encode($json);
        } else {
            $json['success'] = 0;
            $json['message'] ='Sign Up was not Successful';
 
            echo json_encode($json);
        }
 
 
    }
 
 
}
?>