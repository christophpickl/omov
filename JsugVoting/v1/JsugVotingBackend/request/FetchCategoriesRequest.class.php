<?php

require_once('request/AbstractRequest.class.php');

define('_SQL_FETCH_CATEGORIES', 'SELECT category_id, title FROM ' . TBL_CATEGORY);


class FetchCategoriesRequest extends AbstractRequest {
	
	private $_indent = 0;
	private $_out = '';
	
	/**
	 * Enter description here...
	 *
	 * @param SimpleXML $requestXml not used by fetches
	 */
	protected final function internalExecute($requestXml) {
		$this->out('<Categories>');
		
		foreach($this->dbQuery(_SQL_FETCH_CATEGORIES) as $row) {
			$category_id = $row['category_id'];
			$title = $row['title'];
			
			$this->out('<Category>');
			$this->out('<CategoryId>' . $category_id . '</CategoryId>');
			$this->out('<Title>' . wrapCData($title) . '</Title>');
			$this->out('</Category>');
		}

		$this->out('</Categories>');
		
		echo $this->_out;
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