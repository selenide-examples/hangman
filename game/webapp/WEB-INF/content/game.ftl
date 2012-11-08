<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta charset="UTF-8" />
  <meta name="author" content="Andrei Solntsev"/>
  <meta name="copyright" content="© 20011-2012 Andrei Solntsev"/>
  <meta name="description" content="<@s.text name="description"/>"/>
  <meta name="keywords" content="виселица, вешалка, игра, hangman"/>
  <meta HTTP-EQUIV="Content-language" CONTENT="Rus"/>

  <title><@s.text name="title"/></title>
  <script language="JavaScript" src="js/jquery-1.6.4.js"></script>
  <script language="JavaScript" src="js/hangman.js"></script>
  <link rel="stylesheet" type="text/css" href="css/hangman.css"/>
</head>
<body>
<script language="JavaScript">
  var alphabet = "${alphabet}";
  var topic = "${topic}";
  var wordInWork = "${word}";
</script>

<a href="?request_locale=ru">RUS</a>
<a href="?request_locale=et">EST</a>
<a href="?request_locale=en">ENG</a>

<h2><@s.text name="topic"/>:</h2>

<div id="topic"></div>

<h2><@s.text name="word"/>:</h2>

<div id="wordInWork"></div>

<h2><@s.text name="guess-a-letter"/>:</h2>

<div id="alphabet"></div>

<div id="hangmanImageContainer">
  <h1 align="center"><@s.text name="title"/></h1>

  <img id="hangman1" class="hangman" src="img/hangman.png"/>
  <img id="hangman2" class="hangman" src="img/hangman.png"/>
  <img id="hangman3" class="hangman" src="img/hangman.png"/>
  <img id="hangman4" class="hangman body" src="img/hangman.png"/>
  <img id="hangman5" class="hangman body" src="img/hangman.png"/>
  <img id="chair" src="img/chair.jpg"/>
  <img id="gameWin" class='gameOver' src="img/thumbs-up.jpeg" style="width: 400px;"/>
  <img id="gameLost" class='gameOver' src="img/gameOver.png"/>
  <button id="startGame" class="restartGame" onClick="location.href='game';">
    <nobr><@s.text name="play-again"/></nobr>
  </button>
</div>
</body>
</html>
