<?php

$db = new mysqli("localhost", "root", "kedd2028", "userdata");
   
$id = $_POST['id'];
$pw = $_POST['pw'];
$sex = $_POST['sex'];
$age = $_POST['age'];
$phone = $_POST['phone'];
   
$result = mysqli_query($db, "insert into userinfo (ID, PW, SEX, AGE, PHONE) values ('$id', '$pw', '$sex', '$age', '$phone')");
   
   if($result) {
      echo 'success';
   }
   else {
      echo 'failure23';
   }
   
   mysqli_close($db);
?>