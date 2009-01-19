<?php

header('Content-Type: text/text');

/*
PDO:
- http://www.phpro.org/tutorials/Introduction-to-PHP-PDO.html
- http://www.caucho.com/resin/examples/quercus-pdo/index.xtp
*/

require_once('common.php');

$requestMapping = array(
	'fetch_categories' => 'FetchCategoriesRequest',
	'fetch_topics' => 'FetchTopicsRequest',
	'insert_topic' => 'InsertTopicRequest',
	// 'update_topic' => 'UpdateTopicRequest',
	// 'delete_topic' => 'DeleteTopicRequest',
	'insert_vote' => 'InsertVoteRequest'
	// ??? necessary ??? 'delete_vote' => 'DeleteVoteRequest'
);

/*
http://localhost/~phudy/jsugvoting/?request=fetch_topics
*/
if(!isset($_GET['request']) || $_GET['request'] == '') {
	echo "ERROR: No request type provided!";
	exit;
}

$requestType = $_GET['request'];
if(!isset($requestMapping[$requestType])) {
	echo "ERROR: Invalid request type [" . $requestType . "]!";
	exit;
}

$requestClass = $requestMapping[$requestType];
require_once('request/' . $requestClass . '.class.php');

try {
	$db = $_config->newPDO();
	
	$request = new $requestClass($db);
	$request->execute();
	
	$db = null;
} catch(PDOException $e) {
	echo $e->getMessage();
}

?>
