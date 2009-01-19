<?php

require_once('configuration/AbstractPdoConfig.class.php');

class LocalPdoConfig extends AbstractPdoConfig {
	
	private $createTableStatements;
	
	public function LocalPdoConfig() {
		$this->createTableStatements = array(
			TBL_USER => 'CREATE TABLE IF NOT EXISTS ' . TBL_USER . ' ( ' .
				'user_id INTEGER PRIMARY KEY AUTOINCREMENT, ' .
				'username VARCHAR(80) NOT NULL' .
			')',
			TBL_CATEGORY => 'CREATE TABLE IF NOT EXISTS ' . TBL_CATEGORY . ' ( ' .
				'category_id INTEGER PRIMARY KEY AUTOINCREMENT, ' .
				'title VARCHAR(255) NOT NULL' .
			')',
			TBL_TOPIC => 'CREATE TABLE IF NOT EXISTS ' . TBL_TOPIC . ' ( ' .
				'topic_id INTEGER PRIMARY KEY AUTOINCREMENT, ' .
				'title VARCHAR(255) NOT NULL ' .
			')',
			TBL_OFFERED_TOPIC => 'CREATE TABLE IF NOT EXISTS ' . TBL_OFFERED_TOPIC . ' ( ' .
				'offered_topic_id INTEGER PRIMARY KEY AUTOINCREMENT, ' .
				'topic_id INT NOT NULL, ' .
				'user_id INT NOT NULL, ' .
				'FOREIGN KEY (topic_id) REFERENCES ' . TBL_TOPIC . ' (topic_id), ' .
				'FOREIGN KEY (user_id) REFERENCES ' . TBL_USER . ' (user_id) ' .
			')',
			TBL_NEEDED_TOPIC => 'CREATE TABLE IF NOT EXISTS ' . TBL_NEEDED_TOPIC . ' ( ' .
				'needed_topic_id INTEGER PRIMARY KEY AUTOINCREMENT, ' .
				'topic_id INT NOT NULL, ' .
				'category_id INT NOT NULL, ' .
				'FOREIGN KEY (topic_id) REFERENCES ' . TBL_TOPIC . ' (topic_id), ' .
				'FOREIGN KEY (category_id) REFERENCES ' . TBL_CATEGORY . ' (category_id) ' .
			')',
			TBL_VOTE => 'CREATE TABLE IF NOT EXISTS ' . TBL_VOTE . ' ( ' .
				'user_id INT NOT NULL, ' .
				'topic_id INT NOT NULL, ' .
				'PRIMARY KEY (user_id, topic_id),' . 
				'FOREIGN KEY (user_id) REFERENCES ' . TBL_USER . ' (user_id), ' .
				'FOREIGN KEY (topic_id) REFERENCES ' . TBL_TOPIC . ' (topic_id) ' .
			')',
		);	
	}
	
	public function newPDO() {
		$file = __FILE__; // /Users/phudy/PhpDev/JsugVotingBackend/configuration/LocalPdoConfig.class.php
		$idxSecondLastSlash = strrpos($file, "/");
		$file = substr($file, 0, $idxSecondLastSlash);
		$idxSecondLastSlash = strrpos($file, "/");
		$file = substr($file, 0, $idxSecondLastSlash);
		$pathPrefix = $file . "/data/";
		
		return new PDO('sqlite:' . $pathPrefix . 'delme_database.sdb');
	}
	
	public function getCreateStatement($tableName) {
		$res = $this->createTableStatements[$tableName];
		if($res == null) {
			throw new Exception('Invalid table name [' . $tableName . ']!');
		}
		return $res;
	}
	
}

?>