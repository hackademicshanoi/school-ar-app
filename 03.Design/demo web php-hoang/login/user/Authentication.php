<?php
/**
 * Created by PhpStorm.
 * User: Shaon
 * Date: 2/2/2017
 * Time: 1:25 AM
 */
 
namespace user;
 
 
class Authentication
{
	private $TOKEN = "";
    private $EMAIL = "";
    private $PASSWORD = "";
	
	private $FIRST_NAME = "";
	private $LAST_NAME = "";
	private $DATE_OF_BIRTH = "";
	private $ID_SCHOOL = "";
	private $ID_SCHOOL2 = "";
 
    private $DB_CONNECTION;
    private $servername = "localhost";
    private $username = "root";
    private $password = "root";
    private $dbname = "uar";
 
    function __construct()
    {
        $this->DB_CONNECTION = mysqli_connect($this->servername, $this->username,
            $this->password, $this->dbname);
    }
	
	public function prepareEmail($dataEmail)
	{
		if (array_key_exists('email', $dataEmail))
            $this->EMAIL = $dataEmail['email'];
	}
 
    public function prepare($data)
    {
        if (array_key_exists('email', $data))
            $this->EMAIL = $data['email'];
 
        if (array_key_exists('password', $data))
            $this->PASSWORD = $data['password'];
 
    }
	
	public function prepareToken ($dataToken)
	{
		if (array_key_exists('token',$dataToken))
			$this->TOKEN = $dataToken['token'];
	}
	
	function isTokenNotExisted() {
        $sql = "SELECT `token` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) == 0) {
            return true;
        }else {
            return false;
        }
    }
 
    function isTokenExisted() {
        $sql = "SELECT `token` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) > 0) {
            return true;
        }else {
            return false;
        }
    }
	
	function returnFirstNameForEditAfterCheckingToken() {
		$sql = "SELECT `first_name` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        $row = mysqli_fetch_array($result);
            return $row['first_name'];
	}
		
	function returnLastNameForEditAfterCheckingToken() {
		$sql = "SELECT `last_name` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        $row = mysqli_fetch_array($result);
            return $row['last_name'];
	}
	
	function returnDateOfBirthForEditAfterCheckingToken() {
		$sql = "SELECT `date_of_birth` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        $row = mysqli_fetch_array($result);
            return $row['date_of_birth'];
	}
	
	function returnIdSchoolForEditAfterCheckingToken() {
		$sql = "SELECT `id_school` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        $row = mysqli_fetch_array($result);
            return $row['id_school'];
	}
	
	function returnIdSchool2ForEditAfterCheckingToken() {
		$sql = "SELECT `id_school2` FROM `users` WHERE token = '". $this->TOKEN."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        $row = mysqli_fetch_array($result);
            return $row['id_school2'];
	}

	function returnTokenForLogin() {
		$sql = "SELECT `token` FROM `users` WHERE email = '". $this->EMAIL."'
         AND password = '".$this->PASSWORD."'";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) > 0) {
            return $this->EMAIL.$this->PASSWORD;
        }else {
            return "nothing";
        }
	}	
	
	function returnEmailForEdit() {
		$sql = "SELECT `email` FROM `users` WHERE token = '". $this->TOKEN."'";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) > 0) {
            return $this->EMAIL;
        }else {
            return "nothing";
        }
	}	
	
 
	function isUserNotExisted() {
        $sql = "SELECT `email` FROM `users` WHERE email = '". $this->EMAIL."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) == 0) {
            return true;
        }else {
            return false;
        }
    }
 
    function isUserExisted() {
        $sql = "SELECT `email` FROM `users` WHERE email = '". $this->EMAIL."' ";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) > 0) {
            return true;
        }else {
            return false;
        }
    }
 
    function isUserValidToLogin() {
        $sql = "SELECT `email` FROM `users` WHERE email = '". $this->EMAIL."'
         AND password = '".$this->PASSWORD."'";
 
        $result = mysqli_query($this->DB_CONNECTION, $sql);
 
        if(mysqli_num_rows($result) > 0) {
            return true;
        }else {
            return false;
        }
    }
}
?>