<?php

	$db = new mysqli("localhost", "root", "kedd2028", "userdata");

	$sql = "select * from userinfo";

	$result = $db->query($sql);

	$total_record = $result->num_rows;

	for($i = 0; $i < $total_record; $i++) {
		$row = mysqli_fetch_array($result);
		echo $row[ 0 ]."/";
		echo $row[ 1 ]."/";
	}
?>