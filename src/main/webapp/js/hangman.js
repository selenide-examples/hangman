var isPlaying = false;
var failures = 0;

function startGame() {
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

function guessLetter(letter) {
  if (!isPlaying) {
    return;
  }

	var letterContainer = $("*[letter=" + letter + "]");
	letterContainer.unbind("click");

  $.ajax({
    url: "guess?letter=" + letter,
    async: false,
    dataType: 'json',
    success: function (result) {
      console.log(result);

      wordInWork = result.wordInWork;
      showWordInWork();

      failures = result.failures;
      showFailures(failures);

      if (result.guessed == true) {
        letterContainer.addClass("used");
      } else {
        letterContainer.addClass("nonused");
      }

      if (result.gameOver) {
        gameOver();
      }
    }
  });
}

$(function() {
	startGame();
});
