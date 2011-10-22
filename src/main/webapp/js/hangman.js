
var wordInWork;
var alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
var isPlaying = false;
var failures = 0;

function startGame() {
	wordInWork = word.replace(/./g, "_");
	failures = 0;
	showFailures(0);
	showAlphabet();
	showWordInWork();
	$("#startGame").hide();
	$("#gameWin").hide();
	$("#gameLost").hide();
	isPlaying = true;
}

function isGameLost() {
  return failures > 5;
}

function showWordInWork() {
	$("#topic").html(topic);
	var wordContainer = $("#wordInWork");
	wordContainer.empty();

	for (i=0; i<wordInWork.length; i++) {
		$("<span>" + wordInWork.charAt(i) + "</span>").appendTo(wordContainer);
	}
}

function showFailures(failures) {
  switch (failures) {
    case 0:
      $("#hangmanImageContainer .hangman").hide();
      $("#hangmanImageContainer #chair").show();
      break;
    case 1:
      $("#hangmanImageContainer #hangman1").show();
      break;
    case 2:
      $("#hangmanImageContainer #hangman2").show();
      break;
    case 3:
      $("#hangmanImageContainer #hangman3").show();
      break;
    case 4:
      $("#hangmanImageContainer #hangman4").show();
      break;
    case 5:
      $("#hangmanImageContainer #hangman5").show();
      break;
  }
}

function gameOver() {
  wordInWork = word;
  showWordInWork();

  $("#hangmanImageContainer #chair").hide();
  $("#hangmanImageContainer .body").hide();
  if (isGameLost()) {
    $("#gameLost").show();
  } else {
    $("#gameWin").show();
  }
  $("#startGame").show();
  isPlaying = false;
}

function showAlphabet() {
	var alphabetContainer = $("#alphabet");
	alphabetContainer.empty();

	alphabetContainer.append("<table><tr>");
	
	for (i=0; i<alphabet.length; i++) {
		var letterContainer = $("<td></td>");
		letterContainer.text(alphabet.charAt(i));
		letterContainer.attr("letter", alphabet.charAt(i));
		letterContainer.appendTo(alphabetContainer);

		if (i % 11 == 10) {
		  $("</tr><tr>").appendTo(alphabetContainer);
		}
	}

	alphabetContainer.append("</tr></table>");

	$("#alphabet td").attr("onmouseover", "$(this).addClass('buttonover')");
	$("#alphabet td").attr("onmouseout", "$(this).removeClass('buttonover')");
	$("#alphabet td").click(function() {guessLetter($(this).text());});
}

function setCharAt(str,index,chr) {
	if (index > str.length-1) return str;
	return str.substr(0,index) + chr + str.substr(index+1);
}

function guessLetter(letter) {
  if (!isPlaying) {
    return;
  }

	var letterGuessed = false;
	for (i=0; i<word.length; i++) {
		if (word.charAt(i).toLowerCase() == letter.toLowerCase()) {
			letterGuessed = true;
			wordInWork = setCharAt(wordInWork, i, word.charAt(i));
		}
	}

	var letterContainer = $("*[letter=" + letter + "]");
	letterContainer.unbind("click");
	if (letterGuessed == true) {
		letterContainer.addClass("used");
		showWordInWork();
		if (wordInWork.indexOf("_") == -1) {
			gameOver();
		}
	}
	else {
		letterContainer.addClass("nonused");
		failures++;
		showFailures(failures);
		if (isGameLost()) {
			gameOver();
		}
	}
}

$(function() {
	startGame();
});
