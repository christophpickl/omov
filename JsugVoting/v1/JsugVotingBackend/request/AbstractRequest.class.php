<?php

function wrapCData($string) {
	return '<![CDATA[' . $string . ']]>';
}

abstract class AbstractRequest {
	
	private $db;
	
	public function AbstractRequest($db) {
		$this->db = $db;
	}
	
	public function execute() {
		
		$requestXml = null;
		if(isset($_POST['xml_data'])) {
			$requestXml = simplexml_load_string($_POST['xml_data']);
		}
		
		$this->internalExecute($requestXml);
	}
	
	protected abstract function internalExecute($requestXml);
	
	protected function dbQuery($sql) {
		$res = $this->db->query($sql);
		
		if($res === false) {
			$this->pdoDie();
		}
		
		return $res->fetchAll();
	}
	
	protected function dbExec($sql) {
		$res = $this->db->exec($sql);
		
		if($res === false) {
			errorHandle($this->db);
		}
		
		return $res;
	}
	
	protected function lastInsertId() {
		return $this->db->lastInsertId();
	}
	
	protected function prepareStatement($sql) {
		return $this->db->prepare($sql);
	}
	
	protected function pdoDie() {
		$arr = $this->db->errorInfo();
		#var_dump($arr);
		
		$msg = "PDO error message not available!";
		if(isset($arr[2])) {
			$msg = $arr[2];
		}
		
		throw new Exception($msg);
	}
}

?>