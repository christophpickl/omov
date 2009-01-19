<?php


require_once('request/AbstractRequest.class.php');

abstract class AbstractActionRequest extends AbstractRequest {
	
	
	protected abstract function internalActionExecute($requestXml);
	
	protected final function internalExecute($requestXml) {
		
		try {
			$this->internalActionExecute($requestXml);
			
			$error_message = '';
			$error_code = 0;
		} catch(Exception $e) {
			// TODO somehow log exception
			
			$error_message = $e->getMessage();
			$error_code = -1;
		}
		
		$this->outErrorCodeResult($error_code, $error_message);
	}
	
	private function outErrorCodeResult($error_code=0, $error_message = '') {
		$output = '<Result>';
		
		$output .= '<ErrorCode>' . $error_code . '</ErrorCode>';
		$output .= '<ErrorMessage>' . wrapCData($error_message) . '</ErrorMessage>';
		$output .= '</Result>';
		
		echo $output;
	}
}

?>