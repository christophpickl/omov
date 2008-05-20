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

if(isset($_POST['btnSend'])) {
	if(isset($_POST['grpHuman']) == false || isCorrectChoice($_POST['grpHuman']) == false) {
		$feedback = 'Sorry, but it seems as you were not human :)';
		
	} else {
		$inpHuman = $_POST['grpHuman'];
		$inpMail = $_POST['mail'];
		$inpText = $_POST['text'];
		$date = date('Y-m-d H:i:s');
		
		$sql = "INSERT INTO messages (mail, text, dateSent) VALUES ('$inpMail', '$inpText', '$date')";
		$db = mysql_connect(DB_HOST, DB_USER, DB_PASS) or die('connect error');
		mysql_select_db(DB_NAME ,$db) or die('db select error');
		mysql_query($sql);

		$feedback = 'Your message was successfully sent - thank you :)';
	}
}
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
   <meta http-equiv="refresh" content="3;url=index.php" />
</head>

<body>

<?php echo $feedback; ?>
<br />
<a href="index.php">back</a>

</body>
</html>