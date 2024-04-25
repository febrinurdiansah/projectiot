<?php
error_reporting(E_ERROR | E_PARSE);
$host = "localhost";
$user = "root";
$pass = "";
$db = "esp32";

define('DBPATH', $host);
define('DBUSER', $user);
define('DBPASS', $pass);
define('DBNAME', $db);

$mysqli = new mysqli($host, $user, $pass, $db);

// Check connection
if ($mysqli->connect_errno) {
	echo "Failed to connect to MySQL: " . $mysqli->connect_error;
	exit();
}

$data = json_decode(file_get_contents('php://input'), true);

switch ($_GET['data']) {

	default:
		echo "<center><h1>NOT FOUND</h1></center>";
		break;
	case 'insert':
		header('Content-Type: application/json');
		$temp = $data['temp'];
		$hum = $data['hum'];
		if ($temp != '' && $hum != '') {
			$sql = 'INSERT INTO `sensor` (`temp`, `hum`) VALUES ("' . $temp . '", "' . $hum . '");';
			$result = mysqli_query($mysqli, $sql);
			if ($result) {
				echo '{"status":"success","message":"Data Added"}';
				$sql = 'UPDATE `realtime` SET `temp`="' . $temp . '", `hum`="' . $hum . '" WHERE `id`=1';
				$result = mysqli_query($mysqli, $sql);
				if (!$result) {
					echo '{"status":"failed","message":' . mysqli_error($mysqli) . '}';
					break;
				}
			} else {
				echo '{"status":"failed","message":' . mysqli_error($mysqli) . '}';
			}
		} else {
			echo '{"status":"failed","message":"Data Input is not complete"}';
		}
		break;

	case 'getall':
		header('Content-Type: application/json');
		$sql = 'SELECT * FROM `sensor`';
		$result = mysqli_query($mysqli, $sql);
		if ($result) {
			$emparray = array();
			while ($row = mysqli_fetch_assoc($result)) {
				$emparray['data'][] = $row;
			}
			echo  json_encode($emparray);
		} else {
			echo '{"status":"failed","message":' . mysqli_error($mysqli) . '}';
		}
		break;

	case 'realtime':
		header('Content-Type: application/json');
		$sql = 'SELECT * FROM `realtime`';
		$result = mysqli_query($mysqli, $sql);
		if ($result) {
			$emparray = array();
			while ($row = mysqli_fetch_assoc($result)) {
				$emparray['data'][] = $row;
			}
			echo  json_encode($emparray);
		} else {
			echo '{"status":"failed","message":' . mysqli_error($mysqli) . '}';
		}
		break;
}
