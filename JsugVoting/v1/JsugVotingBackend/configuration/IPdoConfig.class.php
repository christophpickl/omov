<?php

interface IPdoConfig {
	
	/**
	 * @return 
	 */
	public function newPDO();
	
	/**
	 * @return 
	 */
	public function getTableNames();
	
	/**
	 * @return 
	 */
	public function getCreateStatement($tableName);
	
}

?>