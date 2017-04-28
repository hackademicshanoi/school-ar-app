<?php

//header('Content-type : bitmap; charset=utf-8');
 
 if(isset($_POST["encoded_string"])){
 	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	$token = $_POST["token"];
	
	$decoded_string = base64_decode($encoded_string);
	
	$path = 'images/'.$image_name;
	
	$file = fopen($path,'wb');
	
	$is_written = fwrite($file, $decoded_string);
	fclose($file);
	if($is_written > 0) {
		$host = "http://192.168.100.163:81/login/user/images/";
		$connection = mysqli_connect('localhost', 'root','','uar');
		$query = "UPDATE `users` SET `profile_picture`='".$host."".$image_name."' WHERE `token`= '".$token ."'";
		
		$result = mysqli_query($connection, $query) ;
		if($result){
		
			$json['success'] = 1;
			$json['message'] = 'Sucess';

			echo json_encode($json);
		}else{
			$json['success'] = 0;
			$json['message'] = 'Wrong';
			echo json_encode($json);
		}
		
		mysqli_close($connection);
	}
 }else echo "nothing";
?>
