<?php

require_once('request/AbstractActionRequest.class.php');

DEFINE('SQL_INSERT_TOPIC', 'INSERT INTO ' . TBL_TOPIC . ' (title) VALUES (:title)');
DEFINE('SQL_OFFERED_TOPIC', 'INSERT INTO ' . TBL_OFFERED_TOPIC . ' (topic_id, user_id) VALUES (:topic_id, :user_id)');
DEFINE('SQL_NEEDED_TOPIC', 'INSERT INTO ' . TBL_NEEDED_TOPIC . ' (topic_id, category_id) VALUES (:topic_id, :category_id)');


class InsertTopicRequest extends AbstractActionRequest {
	
	protected final function internalActionExecute($xmlRequestData) {
		if($xmlRequestData == null) {
			throw new Exception("Insufficient post data!");
			#$xml_data = '<Request><NeededTopic><Title>ObjectiveC</Title><CategoryId>1</CategoryId></NeededTopic></Request>';
			#$xml_data = '<Request><OfferedTopic><Title>Nochmal von zwei</Title><CategoryId>2</CategoryId><User><UserId>1</UserId></User></OfferedTopic></Request>';
			
			#$xmlRequestData = simplexml_load_string($xml_data);
		}
		
		$isValidTopicRequest = true;
		$isOfferedTopic = false;
		if(isset($xmlRequestData->OfferedTopic)) {
			$isOfferedTopic = true;
			
		} else if(isset($xmlRequestData->NeededTopic)) {
			// nothing to do
			
		} else {
			$isValidTopicRequest = false;
		}
		
		if($isValidTopicRequest === false) {
			throw new Exception("Invalid XML request data:\n" . $xmlRequestData);
		}
		
		if($isOfferedTopic) $this->parseOffered($xmlRequestData->OfferedTopic);
		else $this->parseNeeded($xmlRequestData->NeededTopic);
			
	}
	
	private function parseOffered($xml) {
		$lastTopicId = $this->insertTopic($xml);
		
		$params = array(
			'lastTopicId' => $lastTopicId,
			'user_id' => $xml->User->UserId
		);
		
		$stmt = $this->prepareStatement(SQL_OFFERED_TOPIC);
		$stmt->bindParam(':topic_id', $params['lastTopicId'], PDO::PARAM_INT);
		$stmt->bindParam(':user_id', $params['user_id'], PDO::PARAM_INT);
		if($stmt->execute() === false) $this->pdoDie();
	}
	
	private function parseNeeded($xml) {
		$lastTopicId = $this->insertTopic($xml);
		
		$params = array(
			'lastTopicId' => $lastTopicId,
			'category_id' => $xml->CategoryId
		);
		$stmt = $this->prepareStatement(SQL_NEEDED_TOPIC);
		$stmt->bindParam(':topic_id', $params['lastTopicId'], PDO::PARAM_INT);
		$stmt->bindParam(':category_id', $params['category_id'], PDO::PARAM_INT);
		if($stmt->execute() === false) $this->pdoDie();
	}
	
	private function insertTopic($xml) {
		$params = array(
			'title' => $xml->Title
		);
		
		$stmt = $this->prepareStatement(SQL_INSERT_TOPIC);
		$stmt->bindParam(':title', $params['title'], PDO::PARAM_STR, 5);
		$stmt->execute();
		
		return $this->lastInsertId();
	}
}

?>