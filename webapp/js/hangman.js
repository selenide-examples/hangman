var isPlaying = false;
var failures = 0;

function startGame() {
	failures = 0;
	showFailures(0);
	showAlphabet();
	showWordInWork();
	$(".restartGame").hide();
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

	for (var i=0; i<wordInWork.length; i++) {
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
  $(".restartGame").show();
  isPlaying = false;
}

function showAlphabet() {
	var alphabetContainer = $("#alphabet");
	alphabetContainer.empty();

	alphabetContainer.append("<table><tr>");

	for (var i=0; i<alphabet.length; i++) {
		var letterContainer = $("<td></td>");
		letterContainer.text(alphabet.charAt(i));
    letterContainer.attr("letter", alphabet.charAt(i));
    letterContainer.addClass("letter");
		letterContainer.appendTo(alphabetContainer);

		if (i % 11 === 10) {
		  $("</tr><tr>").appendTo(alphabetContainer);
		}
	}

	alphabetContainer.append("</tr></table>");
	alphabetContainer.append($("#startGame").clone().removeAttr('id'));

	$(".letter")
    .mouseover(function() {$(this).addClass('buttonover');})
	  .mouseout(function() {$(this).removeClass('buttonover');})
	  .click(function() {guessLetter(this)});
}

function guessLetter(letterContainer) {
  if (!isPlaying) {
    return;
  }

	letterContainer = $(letterContainer);
	letterContainer.unbind("click");

  $.ajax({
    url: 'guess',
    type: 'POST',
    data: {'letter': letterContainer.text()},
    async: false,
    dataType: 'json',
    success: function (result) {
      wordInWork = result.word;
      showWordInWork();

      failures = result.failures;
      showFailures(failures);

      if (result.guessed === true) {
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
