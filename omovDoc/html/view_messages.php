<?php
require_once('includes.php');

/*

CREATE TABLE messages (
id BIGINT NOT NULL AUTO_INCREMENT,
mail VARCHAR(80),
text MEDIUMTEXT NOT NULL DEFAULT '',
dateSent DATETIME NOT NULL,
PRIMARY KEY (id)
);

*/

$db = mysql_connect(DB_HOST, DB_USER, DB_PASS) or die('connect error');
mysql_select_db(DB_NAME ,$db) or die('db select error');

$sql = "SELECT id, mail, text, dateSent FROM messages";
$res = mysql_query($sql);

?>
<!-- php does not like that ... ?xml version="1.0" encoding="UTF-8"? -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
   <title>OurMovies</title>
   <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
   <meta name="description" content="Yet another movie manager." />
   <meta name="keywords" content="movie, movies, our movies, movie files, manage movies, organize movies, free software, open source" />
   <meta name="author" content="Christoph Pickl" />
</head>
<body>

<table border="1">
<tr>
	<th>ID</th><th>mail</th><th>text</th><th>date sent</th>
</tr>
<?php
while($ar = mysql_fetch_array($res)) {
	echo '<tr><td>'.$ar['id'].'</td><td>'.$ar['mail'].'</td><td>'.$ar['text'].'</td><td>'.$ar['dateSent'].'</td></tr>';
}
?>
</table>

</body>
</html>