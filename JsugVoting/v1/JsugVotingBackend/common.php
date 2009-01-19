<?php

define('TBL_USER', 'user');
define('TBL_CATEGORY', 'category');
define('TBL_TOPIC', 'topic');
define('TBL_OFFERED_TOPIC', 'offered_topic');
define('TBL_NEEDED_TOPIC', 'needed_topic');
define('TBL_VOTE', 'vote');


require_once('configuration/LocalPdoConfig.class.php');
#require_once('configuration/XXXPdoConfig.php');

$_config = new LocalPdoConfig();

function errorHandle(&$db) {
	echo "ERROR!!!\n";
	var_dump($db->errorInfo());
	$db = null;
	exit;
}


?>