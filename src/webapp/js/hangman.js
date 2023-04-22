(async () => {
  let isPlaying = false;
  let failures = 0;
  let alphabet;
  let topic;
  let wordInWork;
  
  const elements = {
    title: document.getElementById('title'),
    topicLabel: document.getElementById('topic-label'),
    wordLabel: document.getElementById('word-label'),
    guessLabel: document.getElementById('guess-label'),
    playAgainLabel: document.getElementById('label-play-again'),
    languageSwitch: document.getElementById('language-switch'),
    buttonStartGame: document.getElementById('startGame'),
    alphabet: document.getElementById('alphabet')
  }
  
  async function startGame() {
    const r = await (await fetch("/game")).json();
    alphabet = r.alphabet;
    topic = r.topic;
    wordInWork = r.word;
  
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
    const wordContainer = $("#wordInWork");
    wordContainer.empty();
  
    for (let i=0; i<wordInWork.length; i++) {
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
    const alphabetContainer = $(elements.alphabet);
    alphabetContainer.empty();
  
    alphabetContainer.append("<table><tr>");
  
    for (let i=0; i<alphabet.length; i++) {
      const letterContainer = $("<td></td>");
      letterContainer.text(alphabet.charAt(i));
      letterContainer.attr("letter", alphabet.charAt(i));
      letterContainer.addClass("letter");
      letterContainer.appendTo(alphabetContainer);
  
      if (i % 11 === 10) {
        $("</tr><tr>").appendTo(alphabetContainer);
      }
    }
  
    alphabetContainer.append("</tr></table>");
  
    $(".letter")
      .mouseover(function() {$(this).addClass('buttonover');})
      .mouseout(function() {$(this).removeClass('buttonover');})
      .click(function() {guessLetter(this)});
    
    elements.alphabet.style.visibility = 'visible';
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
  
  function bindHandlers() {
    elements.buttonStartGame.addEventListener('click', startGame)
    elements.buttonStartGame.style.display = 'block'

    Array.prototype.forEach.call(document.getElementsByClassName('switch-language'), (e) => {
      e.addEventListener('click', () => init(e.getAttribute('data-language')))
    })
    elements.languageSwitch.style.visibility = 'visible'
  }

  async function init(language) {
    elements.languageSwitch.style.visibility = 'hidden';
    elements.alphabet.style.visibility = 'hidden';
    elements.title.textContent = ''
    elements.topicLabel.textContent = ''
    elements.wordLabel.textContent = ''
    elements.guessLabel.textContent = ''
    elements.playAgainLabel.textContent = ''

    const url = language ? `/init?language=${language}` : '/init';
    const r = await (await fetch(url)).json();
    document.title = r.i18n['title']
    elements.title.textContent = r.i18n['title']
    elements.topicLabel.textContent = r.i18n['topic']
    elements.wordLabel.textContent = r.i18n['word']
    elements.guessLabel.textContent = r.i18n['guess-a-letter']
    elements.playAgainLabel.textContent = r.i18n['play-again']
    elements.languageSwitch.style.visibility = 'visible'

    await startGame();
  }
  await init();
  bindHandlers();

}) ()

