
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

<h1 align="center">OurMovies</h1>
<div align="center">
	<img src="images/omov_logo.png" width="200" height="193" alt="omov_logo" title="OurMovies Logo" />
</div>
<h4 align="center">Yet another tool to manage all your movie files stored on harddisk.</h4>

<table align="center" border="1" cellpadding="6" cellspacing="1">
	<thead>
	<tr>
		<th>Table of contents</th>
		<th>News</th>
		<th>Contact</th>
	</tr>
	</thead>
	<tbody>
	<tr valign="top">
		<td valign="middle">
			<ul style="margin-top:0px;margin-bottom:0px;">
				<li><a href="#about" title="Go to About">About</a></li>
				<li><a href="#downloads" title="Go to Downloads">Downloads</a></li>
				<li><a href="#screenshots" title="Go to Screenshots">Screenshots</a></li>
				<li><a href="#troubleshooting" title="Go to Troubleshooting">Troubleshooting</a></li>
				<li><a href="#changelog" title="Go to Changelog">Changelog</a></li>
				<li><a href="https://sourceforge.net/forum/?group_id=219118" title="Visit the Forum" style="font-weight:bold;">Forum</a></li>
			</ul>
		</td>
		<td>
			<p style="margin-top:0px;">
				<i>May 19th, 2008</i><br />
				<br />
				Just released another <b>beta version 0.5</b>,<br />
				which fixes some major bugs and seems<br />
				to work stable.	Now it is even possible to<br />
				enjoy movies right from omov with the<br />
				new <b>built-in video player</b>.
			</p>
		</td>
		<td>
			<?php require_once('includes.php'); ?>
			<form method="post" action="send_message.php" style="margin-bottom:0px;">
				Are you human?<br />
				<?php printHumanChoices(); ?>
				<br />
				<input type="text" name="mail" maxlength="80" style="width:250px;" /> Mail<br />
				<textarea name="text" style="width:250px;height:40px;margin-top:2px;"></textarea> Text<br />
				<input type="submit" name="btnSend" value="Send Message" />
			</form>
		</td>
	</tr>
	</tbody>
</table>

<p align="center">
	<a href="mailto:christoph_pickl users.sourceforge.net" title="Write an email to the project admin">
		<img src="images/mail_address.gif" width="278" height="13" alt="mail contact" title="Write the project's admin an email" border="0"/>
	</a>
</p>
<p align="center">
	<a href="https://sourceforge.net/projects/omov/" target="_blank">
		<img src="images/sourceforge_logo.jpg" width="106" height="30" alt="sourceforge" title="Visit the project's sourceforge site" border="0"/>
	</a>
	&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="http://omov.sourceforge.net/mantis/" target="_blank">
		<img src="images/mantis_logo.gif" width="88" height="35" alt="mantis" title="Enter the project's bug tracking system" border="0" />
	</a>
</p>



<hr />
<a name="about" /><h3>About</h3>
<!-- ******************************************************** -->

<h4>Overview</h4>
	<p>
		OurMovies single purpose is managing your movies -easy, isn't it, huh?!<br />
		It does so by providing a simple table where a single row represents your movie. Because a movie can consist of 
		multiple files, omov assumes your movies are all stored in their own folders. <span style="text-decoration:line-through">So it just let's you manage them; 
		<i>no playing</i> at all, that's out of scope.</span><br />
		If you have a current <a href="http://www.apple.com/quicktime/" target="_blank">QuickTime</a> installed, 
		you even can watch movies right from the application (proper codec required).
	</p>

<h4>Features</h4>
	<ul>
		<li>Automatically fetches metadata from the web</li>
		<li>Manages covers (can be also fetched from web)</li>
		<li>Export as HTML report or make a full backup</li>
		<li><i>SmartCopy</i> which allows automatic copying of certain movies (uses HTML report's generated output)</li>
		<li><i>SmartFolder</i> which gives you the possibility of easily storing a bunch of search criteria</li>
		<li>Scans a given directory for (recently added) movies and imports them</li>
	</ul>

<h4>Data versions</h4>
	<p>
		OurMovies uses different data sources which all have different versions and are all incompatible to each other. 
		There are tree different sources existing:
	</p>
	<ul>
		<li><i>Core Movie Source</i>: Storage for movies attributes. (using an embedded object-oriented database)</li>
		<li><i>Core SmartFolder Source</i>: Storage for smartfolder criteria/attributes. (also using an embedded object-oriented database)</li>
		<li><i>Preferences Source</i>: Data visible in the preferences window and some convenient values storing recent folder paths; e.g. for recent scan root, export path, etc. (Stored through Java's own preferences API)</li>
	</ul>
	<p>
		If one of the core sources versions does not match (by comparing the data source with the expected application version) an error dialog will be displayed and OurMovies quit immediately. If the preferences source is incompatible an appropriate converter will try to convert the stored data into the new version by using some default values.
	</p>
	<p>
		For solutions about version mismatches see the <a href="#troubleshooting">troubleshooting section</a> below.
	</p>


<hr />
<a name="downloads" /><h3>Downloads (Beta)</h3>
<!-- ******************************************************** -->

<ul>
	<li>
		<img src="images/windows_logo.png" alt="windows" title="Microsoft Windows" width="16" height="16" />
		Microsoft Windows:
		<a href="http://downloads.sourceforge.net/omov/OurMovies-0.5.msi?use_mirror=osdn" target="_self">OurMovies-0.5.msi</a> (8 MB)
	</li>
	<li>
		<img src="images/apple_logo.png" alt="mac os x" title="Apple Mac OS X" width="16" height="16" />
		Apple Mac OS X: 
		<a href="http://downloads.sourceforge.net/omov/OurMovies-0.5.dmg?use_mirror=osdn" target="_self">OurMovies-0.5.dmg</a> (14 MB)
	</li>
	<li>
		<!-- <img src="" alt="" title="" width="16" height="16" /> -->
		Other operating systems: 
		<a href="http://downloads.sourceforge.net/omov/OurMovies-0.5.jar?use_mirror=osdn" target="_self">OurMovies-0.5.jar</a> (8 MB)
		</li>
</ul>

Or see a full list of <a href="https://sourceforge.net/project/showfiles.php?group_id=219118" target="_self">all releases</a> at sourceforge.

<hr />
<a name="screenshots" /><h3>Screenshots</h3>
<!-- ******************************************************** -->

<ul>

	<li>Main Screen:<br />
	<a href="images/screenshots/main_screen-BIG.png" target="_blank">
		<img src="images/screenshots/main_screen-LIL.png" width="269" height="143" alt="screenshot mainwindow" title="The Main Window" border="0" /></a>
	<br /><br /></li>
	
	<li>Movie Detail Window:<br />
	<a href="images/screenshots/moviedetail_1info-BIG.png" target="_blank">
		<img src="images/screenshots/moviedetail_1info-LIL.png" width="130" height="120" alt="screenshot moviedetail info" title="Movie Details - Tab Info" border="0" /></a>

	<a href="images/screenshots/moviedetail_2details-BIG.png" target="_blank">
		<img src="images/screenshots/moviedetail_2details-LIL.png" width="130" height="120" alt="screenshot moviedetail details" title="Movie Details - Tab Details" border="0" /></a>

	<a href="images/screenshots/moviedetail_3notes-BIG.png" target="_blank">
		<img src="images/screenshots/moviedetail_3notes-LIL.png" width="130" height="120" alt="screenshot moviedetail notes" title="Movie Details - Tab Notes" border="0" /></a>
	<br /><br /></li>
	
	<li>SmartFolder:<br />
	<a href="images/screenshots/smartfolders_1manage-BIG.png" target="_blank">
		<img src="images/screenshots/smartfolders_1manage-LIL.png" width="79" height="49" alt="screenshot smartfolder overview" title="Overview of defined SmartFolders" border="0" 
		     style="margin-bottom:11px;" /></a>
	<a href="images/screenshots/smartfolders_2edit-BIG.png" target="_blank">
		<img src="images/screenshots/smartfolders_2edit-LIL.png" width="198" height="119" alt="screenshot smartfolder definition" title="SmartFolder Manage Dialog" border="0" /></a>
	<br /><br /></li>
	
	<li>Scanning a Repository:<br />
	<a href="images/screenshots/scanning_1-BIG.png" target="_blank">
		<img src="images/screenshots/scanning_1-LIL.png" width="190" height="133" alt="screenshot scan progress" title="Scan in progress" border="0" /></a>
	<a href="images/screenshots/scanning_2-BIG.png" target="_blank">
		<img src="images/screenshots/scanning_2-LIL.png" width="190" height="133" alt="screenshot scan finished" title="Scan finished" border="0" /></a>
	<br /><br /></li>
	
	<li><span style="font-family:impact,arial;">!new!</span> QuickView:<br />
	<a href="images/screenshots/quickview-BIG.png" target="_blank">
		<img src="images/screenshots/quickview-LIL.png" width="226" height="124" alt="screenshot quickview" title="QuickView in action" border="0" 
		     style="margin-top:4px;" /></a>
		     </li>
	
</ul>


<hr />
<a name="troubleshooting" /><h3>Troubleshooting</h3>
<!-- ******************************************************** -->

<ul>
	<li><div style="font-weight:bold;margin-bottom:5px;">Getting an &quot;<i>The File <code>MSVCR71.dll</code> could not be found</i>&quot; error on Windows XP?</div>
		This library is necessary because of the java-exe wrapper <a href="http://jsmooth.sourceforge.net/" target="_blank">JSmooth</a>.<br />
		Please <a href="msvcr71.dll">download the <code>MSCVR71.dll</code></a> file and put it in the same directory as the <code>OurMovies.exe</code> is.<br />
		<br />
	</li>
	<li><div style="font-weight:bold;margin-bottom:5px;">Getting <i>VersionMismatch</i> errors at startup?</div>
		This can happen if one or more data sources are incompatible with each other.<br />
		In case the <b>core sources</b> mismatched, you can either 1) choose a converter -if available- to upgrade these sources 2) get an older application version; see compatibility table below 3) or simply reset the old sources.<br />
		In case the <b>preference source</b> mismatched, a converter will automatically update this source and display an info box.<br />
		<br />
		<center>
		<table border="1">
			<tr>         <th>OurMovies</th>                         <th>Core Movie</th>  <th>Core SmartFolder</th>                        <th>Preferences</th></tr>
			<tr align="center"><td>0.1</td>                                  <td>1</td>                 <td>1</td>                                  <td>1</td></tr>
			<tr align="center"><td>0.2</td><td style="background-color:#CCCCCC;">2</td>                 <td>1</td>                                  <td>1</td></tr>
			<tr align="center"><td>0.3</td><td style="background-color:#CCCCCC;">3</td>                 <td>1</td><td style="background-color:#CCCCCC;">2</td></tr>
			<tr align="center"><td>0.4</td>                                  <td>3</td>                 <td>1</td>                                  <td>2</td></tr>
			<tr align="center"><td>0.5</td>                                  <td>3</td>                 <td>1</td><td style="background-color:#CCCCCC;">3</td></tr>
<!--		<tr align="center"><td>0.6</td>                                  <td>?</td>                 <td>?</td>                                  <td>?</td></tr>-->
		</table>
		Version compatibility table
		</center>
	</li>
</ul>


<hr />
<a name="changelog" /><h3>Changelog</h3>
<!-- ******************************************************** -->

<div style="width:100%;overflow:auto;"><pre style="margin-top:0px">
Version 0.5 (Beta)
===============
- 0000012: [Bug] vlc integration (Play in VLC) does not work properly for OS X
- 0000043: [Additional Functionality] simple rescan button to check movie folder and its content for movie files (also for multiple movies)
- 0000049: [Metadata Fetching] if fetching metadata (which takes a few moments) show modal dialog + start thread
- 0000008: [Additional Functionality] outsource metadata fetching in own component
- 0000044: [Additional Functionality] experimental quicktime integration for built-in video player

Version 0.4 (Beta)
===============
- 0000039: [Additional Functionality] file association of *.omo files with OurMovies application (mac osx)
- 0000048: [Metadata Fetching] fetching metadata for certain entries fails/crashes
- 0000033: [GUI Look] properly handle text with overlength
- 0000051: [GUI Handling] context menu does not pop up in movie table (windows only)

Version 0.3 (Beta)
===============
- 0000002: [Additional Functionality] filesystem check which verfies that movie folders are still existing
- 0000020: [Additional Functionality] create native microsoft windows installer
- 0000004: [GUI Handling] single click on CoverSelector should popup a filechooser
- 0000009: [Additional Functionality] in case of editing multiple movies, preselect lefthand checkbox of each field which is equal for all movies
- 0000007: [Additional Functionality] display cover images in MainWindow table as additional column
- 0000018: [Additional Functionality] add version check at startup
- 0000019: [Additional Functionality] import a previously exported backup (*.omo) file

Version 0.2 (Alpha)
===============
- 0000003: [Additional Functionality] implement SmartCopy feature
- 0000001: [Additional Functionality] export whole data (movie fields, covers) as single zip file
- 0000011: [Additional Functionality] notify user if original movie title (by folder name) differs from metadata fetched movie title
- 0000015: [Other] create native executable for windows
- 0000016: [Other] create native executable for macosx
- 0000010: [Additional Functionality] make more columns selectable when exporting as HTML
- 0000005: [Additional Functionality] all necessary movie attributes should be available for SmartFolder search
- 0000013: [GUI Look] movie info dialog's width can be oversized
- 0000017: [Other] use OSXAdapter to create a nice mac look&amp;feel
</pre></div>


<hr />

<p>
    <a href="http://validator.w3.org/check?uri=referer">
    	<img src="http://www.w3.org/Icons/valid-xhtml10-blue" alt="Valid XHTML 1.0 Transitional" height="31" width="88" border="0" />
    </a>
 </p>

<p><small>Last modified 2008-May-19</small></p>
  
</body>
</html>