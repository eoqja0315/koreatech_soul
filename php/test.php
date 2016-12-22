<?php

	$db = new mysqli("localhost", "root", "kedd2028", "userdata");

	$sql = "select ID from userinfo";

	$result = $db->query($sql);

	$total_record = $result->num_rows;

	for($i = 0; $i < $total_record; $i++) {
		$row = $result->fetch_row();

	echo $row[ 0 ]."/";

	}
?>