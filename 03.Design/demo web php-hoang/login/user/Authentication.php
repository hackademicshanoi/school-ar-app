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