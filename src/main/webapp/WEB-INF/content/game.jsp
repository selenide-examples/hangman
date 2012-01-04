<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<META name="author" content="Andrei Solntsev"/>
		<META name="copyright" content="© 20011 Andrei Solntsev"/>
		<META name="description" content="Игра ВИСЕЛИЦА"/>
		<META name="keywords" content="виселица, вешалка, игра, hangman"/>
		<META HTTP-EQUIV="Content-language" CONTENT="Rus"/>

		<title>Виселица</title>
		<script language="JavaScript" src="js/jquery-1.6.4.js"></script>
		<script language="JavaScript" src="js/hangman.js"></script>
		<link rel="stylesheet" type="text/css" href="css/hangman.css" />
	</head>
	<body>
	  <script language="JavaScript">
	    var alphabet = "${alphabet}";
	    var topic = "${word.topic}";
      var wordInWork = "${wordInWork}";
	  </script>

    <a href="?language=rus">RUS</a>
    <a href="?language=est">EST</a>
    <a href="?language=eng">ENG</a>

		<h2>Тема:</h2>
		<div id="topic"></div>

		<h2>Слово:</h2>
		<div id="wordInWork"></div>

		<h2>Угадайте букву:</h2>
    <div id="alphabet"></div>

    <div id="hangmanImageContainer">
      <h1 align="center">Виселица</h1>

		  <img id="hangman1" class="hangman" src="img/hangman.png"/>
		  <img id="hangman2" class="hangman" src="img/hangman.png"/>
		  <img id="hangman3" class="hangman" src="img/hangman.png"/>
		  <img id="hangman4" class="hangman body" src="img/hangman.png"/>
		  <img id="hangman5" class="hangman body" src="img/hangman.png"/>
		  <img id="chair" src="img/chair.jpg"/>
		  <img id="gameWin" class='gameOver' src="img/thumbs-up.jpeg" style="width: 400px;"/>
		  <img id="gameLost" class='gameOver' src="img/gameOver.png"/>
      <button id="startGame" class="restartGame" onClick="location.href='game';"><nobr>Сыграть ещё</nobr></button>
		</div>

	</body>
</html>
