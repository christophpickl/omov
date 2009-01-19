<?php

require_once('request/AbstractRequest.class.php');

define('_SQL_FETCH_OFFERED_TOPICS', <<<EOD

SELECT
  ot.offered_topic_id AS offered_topic_id,
  t.topic_id AS topic_id,
  t.title AS title,
  COUNT(v.topic_id) AS vote_cnt,
  (:user_id IN (SELECT vv.user_id FROM vote AS vv WHERE vv.topic_id=t.topic_id)) AS yet_voted,
  
  ot.user_id AS user_id
FROM
  topic t
INNER JOIN
  offered_topic AS ot ON t.topic_id=ot.topic_id
LEFT OUTER JOIN
  vote v ON t.topic_id=v.topic_id
GROUP BY
  t.topic_id

EOD
);
define('_SQL_FETCH_NEEDED_TOPICS', <<<EOD

SELECT
  nt.needed_topic_id AS needed_topic_id,
  t.topic_id AS topic_id,
  t.title AS title,
  COUNT(v.topic_id) AS vote_cnt,
  (:user_id IN (SELECT vv.user_id FROM vote AS vv WHERE vv.topic_id=t.topic_id)) AS yet_voted,
  
  nt.category_id AS category_id
FROM
  topic t
INNER JOIN
  needed_topic AS nt ON t.topic_id=nt.topic_id
LEFT OUTER JOIN
  vote v ON t.topic_id=v.topic_id
GROUP BY
  t.topic_id

EOD
);


class FetchTopicsRequest extends AbstractRequest {
	
	private $_indent = 0;
	private $_out = '';
	
	protected function internalExecute($requestXml) {
		if($requestXml == null) {
			#$requestXml = simplexml_load_string('<Request><User><UserId>1</UserId></User></Request>');
			throw new Exception("Insufficient post data!");
		}
		
		$this->out('<Topics>');
		$this->fetchOfferedNeededTopics($requestXml, _SQL_FETCH_OFFERED_TOPICS, true, 'OfferedTopic',  'offered_topic_id');
		$this->fetchOfferedNeededTopics($requestXml, _SQL_FETCH_NEEDED_TOPICS, false, 'NeededTopic', 'needed_topic_id');
		$this->out('</Topics>');
		
		echo $this->_out;
	}
	
	private function fetchOfferedNeededTopics($requestXml, $sql, $isOffered, $tagName, $subTopicIdName) {
		$this->out('<'.$tagName.'s>');
		
		$user_id = $requestXml->User->UserId;
		
		$stmt = $this->prepareStatement($sql);
		$stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
		if($stmt->execute() === false) $this->pdoDie();
		
		foreach($stmt->fetchAll() as $row) {
			$sub_topic_id = $row[$subTopicIdName];
			$topic_id = $row['topic_id'];
			$title = $row['title'];
			$vote_cnt = $row['vote_cnt'];
			$yet_voted = $row['yet_voted'];
			
			$this->out('<'.$tagName.' '.$tagName.'Id="' . $sub_topic_id . '" TopicId="' . $topic_id . '">');
			$this->out('<Title>' . wrapCData($title) . '</Title>');
			
			$this->out('<Vote>');
			$this->out('<VoteCnt>' . $vote_cnt . '</VoteCnt>');
			$this->out('<YetVoted>' . $yet_voted . '</YetVoted>');
			$this->out('</Vote>');
			
			if($isOffered) {
				$user_id = $row['user_id'];
				$this->out('<User><UserId>' . $user_id . '</UserId></User>');
			} else { // is needed
				$category_id = $row['category_id'];
				$this->out('<CategoryId>' . $category_id . '</CategoryId>');
			}
			
			$this->out('</'.$tagName.'>');
		}
		$this->out('</'.$tagName.'s>');
	}
	
	private function out($text) {
		$this->_out .= $text . "\n";
	}
	
	/*
	private function out($text) {
		$isTag = (substr($text, 0, 1) == '<') && (substr($text, 0, 9) != '<![CDATA[');
		$isClosingTag = $isTag && (substr($text, 0, 2) == '</');
		
		$tabs = "";
		for($i=0; $i < $this->_indent; $i++) $tabs .= "\t";
		$this->_out .= $tabs;
		
		$this->_out .= $text . "\n";
		
		if($isTag && !$isClosingTag) {
			$this->_indent++;
		} else {
			$this->_indent--;
		}
	}*/
	
}

?>