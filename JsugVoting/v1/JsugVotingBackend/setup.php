<?php
//header('Content-Type: text/text');

require_once('common.php');


try {
	echo "Connecting to database...\n\n";
	$db = $_config->newPDO();
	
	foreach($_config->getTableNames() as $tableName) {
		$tableCreateStatement = $_config->getCreateStatement($tableName);
		echo "Creating table [$tableName] by statement:\n  $tableCreateStatement\n\n";
		$res = $db->exec($tableCreateStatement);
		if($res === false) {
			errorHandle($db);
		}
	}
	
	// prepare categories
	$sqlCountCategories = 'SELECT count(*) AS cnt FROM ' . TBL_CATEGORY;
	$stmt = $db->query($sqlCountCategories) or errorHandle($db);
	$rows = $stmt->fetchAll();
	$cnt = $rows[0]['cnt'];
	
	if($cnt == 0) {
		$db->exec("INSERT INTO user (username) VALUES ('Christoph Pickl')") or errorHandle($db);
		$db->exec("INSERT INTO user (username) VALUES ('Florian Motlik')") or errorHandle($db);
		$db->exec("INSERT INTO user (username) VALUES ('Anna Nym')") or errorHandle($db);
		
		$db->exec("INSERT INTO category (title) VALUES ('Tools, Frameworks')") or errorHandle($db);
		$db->exec("INSERT INTO category (title) VALUES ('Java & Co')") or errorHandle($db);
		$db->exec("INSERT INTO category (title) VALUES ('Languages')") or errorHandle($db);
		$db->exec("INSERT INTO category (title) VALUES ('Web')") or errorHandle($db);
		$db->exec("INSERT INTO category (title) VALUES ('Misc')") or errorHandle($db);
		
		$db->exec("INSERT INTO topic (title) VALUES ('JavaSuper')") or errorHandle($db); # topic_id 1
		$db->exec("INSERT INTO offered_topic (topic_id, user_id) VALUES (1, 1)") or errorHandle($db);
		
		$db->exec("INSERT INTO topic (title) VALUES ('Baaad')") or errorHandle($db); # topic_id 2
		$db->exec("INSERT INTO offered_topic (topic_id, user_id) VALUES (2, 1)") or errorHandle($db);
		
		$db->exec("INSERT INTO topic (title) VALUES ('JavaFX')") or errorHandle($db); # topic_id 3
		$db->exec("INSERT INTO needed_topic (topic_id, category_id) VALUES (3, 3)") or errorHandle($db);
		
		$db->exec("INSERT INTO vote (user_id, topic_id) VALUES (1, 1)") or errorHandle($db);
		$db->exec("INSERT INTO vote (user_id, topic_id) VALUES (2, 1)") or errorHandle($db);
		$db->exec("INSERT INTO vote (user_id, topic_id) VALUES (3, 1)") or errorHandle($db);
	}
	
	$db = null;
} catch(PDOException $e) {
	echo $e->getMessage();
}

echo "\n\n!FINISHED SUCCESSFULLY!";

?>
