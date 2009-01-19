<?php

require_once('configuration/IPdoConfig.class.php');

abstract class AbstractPdoConfig implements IPdoConfig {
	private static $tables = array(TBL_USER, TBL_CATEGORY, TBL_TOPIC, TBL_OFFERED_TOPIC, TBL_NEEDED_TOPIC, TBL_VOTE);
	public function getTableNames() {
		return AbstractPdoConfig::$tables;
	}
}

?>