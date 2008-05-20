<?php

--- see OurMovies Project directory 'SourceForgeData' for login data ---
define('DB_USER', '');
define('DB_PASS', '');
define('DB_HOST', '');
define('DB_NAME', '');


$correctChoices = Array();
array_push($correctChoices, 'Definetly');
array_push($correctChoices, 'For Shure');
array_push($correctChoices, 'I am');
array_push($correctChoices, 'Yes sir');
array_push($correctChoices, 'Papa said so');


$incorrectChoices = Array();
array_push($incorrectChoices, 'Not Shure');
array_push($incorrectChoices, 'I am Robot');
array_push($incorrectChoices, 'No sir');
array_push($incorrectChoices, 'Nope');
array_push($incorrectChoices, 'Not true');
array_push($incorrectChoices, 'This is not true');
array_push($incorrectChoices, 'I cant read');
array_push($incorrectChoices, 'Dont speak english');
array_push($incorrectChoices, 'Dont understand');
array_push($incorrectChoices, 'What?');
array_push($incorrectChoices, 'Maybe');
array_push($incorrectChoices, 'Cant remember');

define('CORRECT_LENGTH', sizeof($correctChoices));
define('INCORRECT_LENGTH', sizeof($incorrectChoices));
define('DISPLAYED_CHOICES', 3);

function isCorrectChoice($choice) {
	global $correctChoices;
	return in_array($choice, $correctChoices);
}

function printHumanChoices() {
	global $correctChoices, $incorrectChoices;
	
	$usedChoices = Array();
	$indexCorrect = rand(0, DISPLAYED_CHOICES-1);
	
	while(sizeof($usedChoices) <= DISPLAYED_CHOICES) {
		if(sizeof($usedChoices) == $indexCorrect) {
			array_push($usedChoices, $correctChoices[rand(0, CORRECT_LENGTH-1)]);
		} else {
			$randIndexIncorrect = rand(0, INCORRECT_LENGTH-1);
			$incorrectChoice = $incorrectChoices[$randIndexIncorrect];
			if(in_array($incorrectChoice, $usedChoices) == false) {
				array_push($usedChoices, $incorrectChoice);
			}
		}
	}
	
	for($i = 0; $i < DISPLAYED_CHOICES; $i++) {
		echo '<input type="radio" name="grpHuman" value="'.$usedChoices[$i].'" /> '.$usedChoices[$i]. ' ';
	}
}

?>
