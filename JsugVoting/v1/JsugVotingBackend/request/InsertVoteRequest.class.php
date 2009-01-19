<?php

require_once('request/AbstractActionRequest.class.php');

DEFINE('SQL_INSERT_VOTE', 'INSERT INTO ' . TBL_VOTE . ' (user_id, topic_id) VALUES (:user_id, :topic_id)');

class InsertVoteRequest extends AbstractActionRequest {
	
	protected final function internalActionExecute($requestXml) {
		if($requestXml == null) {
			#throw new Exception("Insufficient post data!");
			
			#$xml_data = '<Request><Vote><UserId>1</UserId><TopicId>1</TopicId></Vote></Request>';
			$xml_data = '<Request><Vote><UserId>1</UserId><TopicId>5</TopicId></Vote></Request>';
			$requestXml = simplexml_load_string($xml_data);
		}
		
		$params = array(
			'user_id' => $requestXml->Vote->UserId,
			'topic_id' => $requestXml->Vote->TopicId
		);
		
		$stmt = $this->prepareStatement(SQL_INSERT_VOTE);
		$stmt->bindParam(':user_id', $params['user_id'], PDO::PARAM_INT);
		$stmt->bindParam(':topic_id', $params['topic_id'], PDO::PARAM_INT);
		if($stmt->execute() === false) $this->pdoDie();
	}
	
}

?>