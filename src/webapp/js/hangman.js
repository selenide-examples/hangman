(async () => {
  const SECONDS_PER_LETTER = 10;
  const TIMER_BASE_SECONDS = 5;

  let isPlaying = false;
  let failures = 0;
  let maxErrors = 6;
  let hintUnlockAt = 3;
  let alphabet;
  let topic;
  let wordInWork;
  let timedMode = false;
  let timeLeft = 0;
  let timerId = null;
  let hintLockedTemplate = '';

  const elements = {
    title: document.getElementById('title'),
    topic: document.getElementById('topic'),
    hintLabel: document.getElementById('hint-label'),
    hintButton: document.getElementById('hintButton'),
    hintLocked: document.getElementById('hintLocked'),
    wordLabel: document.getElementById('word-label'),
    guessLabel: document.getElementById('guess-label'),
    playAgainLabel: document.getElementById('label-play-again'),
    languageSwitch: document.getElementById('language-switch'),
    buttonStartGame: document.getElementById('startGame'),
    alphabet: document.getElementById('alphabet'),
    modeSelect: document.getElementById('modeSelect'),
    modeLabel: document.getElementById('mode-label'),
    modeTimed: document.getElementById('modeTimed'),
    modeUntimed: document.getElementById('modeUntimed'),
    gameArea: document.getElementById('gameArea'),
    timer: document.getElementById('timer'),
    howToPlay: document.getElementById('howToPlay'),
    howToPlayModal: document.getElementById('howToPlayModal'),
    howToPlayClose: document.getElementById('howToPlayClose'),
    howToTitle: document.getElementById('howto-title')
  }

  function showModeSelect() {
    stopTimer();
    isPlaying = false;
    elements.gameArea.style.display = 'none';
    elements.modeSelect.style.display = 'block';
    $(".restartGame").hide();
    $("#gameWin").hide();
    $("#gameLost").hide();
    showFailures(0);
  }

  async function startGame(isTimed) {
    timedMode = isTimed;
    elements.modeSelect.style.display = 'none';
    elements.gameArea.style.display = 'block';

    const r = await (await fetch("/game")).json();
    alphabet = r.alphabet;
    topic = r.topic;
    wordInWork = r.word;
    maxErrors = r.maxErrors;
    hintUnlockAt = Math.floor(maxErrors / 2);

    failures = 0;
    showFailures(0);
    showAlphabet();
    showWordInWork();
    resetHint();
    $(".restartGame").hide();
    $("#gameWin").hide();
    $("#gameLost").hide();
    isPlaying = true;

    if (timedMode) {
      startTimer(wordInWork.length * SECONDS_PER_LETTER + TIMER_BASE_SECONDS);
    } else {
      elements.timer.style.display = 'none';
    }
  }

  function startTimer(seconds) {
    stopTimer();
    timeLeft = seconds;
    elements.timer.style.display = 'inline-block';
    renderTimer();
    timerId = setInterval(() => {
      timeLeft--;
      renderTimer();
      if (timeLeft <= 0) {
        stopTimer();
        failures = maxErrors;
        gameOver();
      }
    }, 1000);
  }

  function stopTimer() {
    if (timerId) {
      clearInterval(timerId);
      timerId = null;
    }
  }

  function renderTimer() {
    elements.timer.textContent = '⏱ ' + timeLeft + 's';
    elements.timer.classList.toggle('timer-warning', timeLeft <= 10);
  }

  function resetHint() {
    elements.topic.textContent = topic;
    elements.topic.style.visibility = 'hidden';
    elements.hintButton.disabled = true;
    elements.hintButton.style.display = 'inline-block';
    updateHintLock();
  }

  function updateHintLock() {
    if (elements.hintButton.style.display === 'none') {
      elements.hintLocked.textContent = '';
    } else if (failures >= hintUnlockAt) {
      elements.hintButton.disabled = false;
      elements.hintLocked.textContent = '';
    } else {
      elements.hintLocked.textContent = hintLockedTemplate.replace('{0}', hintUnlockAt - failures);
    }
  }

  function revealHint() {
    if (!isPlaying || elements.hintButton.disabled) return;
    elements.topic.style.visibility = 'visible';
    elements.hintButton.style.display = 'none';
    elements.hintLocked.textContent = '';
  }

  function isGameLost() {
    return failures >= maxErrors;
  }

  function showWordInWork() {
    const wordContainer = $("#wordInWork");
    wordContainer.empty();

    for (let i=0; i<wordInWork.length; i++) {
      $("<span>" + wordInWork.charAt(i) + "</span>").appendTo(wordContainer);
    }
  }

  const show = (id, on) => document.getElementById(id).style.display = on ? '' : 'none';

  // returns the proportional threshold for a stage (1-6) given maxErrors
  function stageAt(stage) {
    return Math.ceil(stage * maxErrors / 6);
  }

  function showFailures(failures) {
    // always (re)show the SVG in case it was hidden after a previous game over
    document.getElementById('hangmanSvg').style.display = '';
    show('figure', true);

    // gallows pieces on proportional thresholds
    show('gallows1', failures >= stageAt(1));
    show('gallows2', failures >= stageAt(2));
    show('gallows3', failures >= stageAt(3));
    show('noose',    failures >= stageAt(4));
    // chair gets kicked away near the end
    $("#hangmanImageContainer #chair").toggle(failures < stageAt(5));
    // arms raised in panic near the end
    document.getElementById('armLeft').setAttribute('y2',  failures >= stageAt(5) ? 160 : 220);
    document.getElementById('armRight').setAttribute('y2', failures >= stageAt(5) ? 160 : 220);

    // emotional state based on how far through allowed errors
    const pct = maxErrors > 0 ? failures / maxErrors : 0;
    const dead = failures >= maxErrors;
    show('eyesNormal', pct < 0.5 && !dead);
    show('eyesWide',   pct >= 0.5 && !dead);
    show('eyesDead',   dead);
    show('brows',      pct >= 0.33 && !dead);
    show('sweat',      pct >= 0.67 && !dead);
    show('mouthHappy',   pct < 0.17);
    show('mouthNeutral', pct >= 0.17 && pct < 0.5);
    show('mouthWorried', (pct >= 0.5 && pct < 0.83) && !dead);
    show('mouthScared',  pct >= 0.83 && !dead);
    // dead: reuse worried frown
    if (dead) show('mouthWorried', true);
  }

  function gameOver() {
    stopTimer();
    showWordInWork();
    elements.topic.style.visibility = 'visible';
    elements.hintButton.style.display = 'none';
    elements.hintLocked.textContent = '';

    if (isGameLost()) {
      showFailures(maxErrors);
      $("#gameLost").show();
    } else {
      $("#gameWin").show();
    }
    // hide the entire SVG so no gallows lines leak behind the result image
    document.getElementById('hangmanSvg').style.display = 'none';
    $("#hangmanImageContainer #chair").hide();
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

        updateHintLock();

        if (result.gameOver) {
          gameOver();
        }
      }
    });
  }

  function toggleHowToPlay(forceState) {
    const open = forceState !== undefined ? forceState
      : elements.howToPlayModal.style.display === 'none';
    elements.howToPlayModal.style.display = open ? 'flex' : 'none';
  }

  function onKeyDown(e) {
    if (e.key === '?') { toggleHowToPlay(); return; }
    if (e.key === 'Escape') { toggleHowToPlay(false); return; }
    if (elements.howToPlayModal.style.display !== 'none') return;

    if (elements.modeSelect.style.display !== 'none') {
      if (e.key === 't' || e.key === 'T') { startGame(true); e.preventDefault(); }
      if (e.key === 'u' || e.key === 'U') { startGame(false); e.preventDefault(); }
      return;
    }

    if (!isPlaying) {
      if (e.key === 'Enter' || e.key === ' ') { showModeSelect(); e.preventDefault(); }
      return;
    }

    if (e.altKey && e.code === 'KeyH') {
      revealHint();
      e.preventDefault();
      return;
    }

    if (e.key.length === 1 && !e.ctrlKey && !e.metaKey && !e.altKey) {
      const cell = elements.alphabet.querySelector('td[letter="' + e.key.toUpperCase() + '"]');
      if (cell && !cell.classList.contains('used') && !cell.classList.contains('nonused')) {
        guessLetter(cell);
      }
    }
  }

  function bindHandlers() {
    elements.buttonStartGame.addEventListener('click', showModeSelect)

    elements.modeTimed.addEventListener('click', () => startGame(true))
    elements.modeUntimed.addEventListener('click', () => startGame(false))
    elements.hintButton.addEventListener('click', revealHint)
    elements.howToPlay.addEventListener('click', () => toggleHowToPlay(true))
    elements.howToPlayClose.addEventListener('click', () => toggleHowToPlay(false))
    elements.howToPlayModal.addEventListener('click', (e) => {
      if (e.target === elements.howToPlayModal) toggleHowToPlay(false);
    })
    document.addEventListener('keydown', onKeyDown)

    Array.prototype.forEach.call(document.getElementsByClassName('switch-language'), (e) => {
      e.addEventListener('click', () => init(e.getAttribute('data-language')))
    })
  }

  async function init(language) {
    stopTimer();
    elements.modeSelect.style.display = 'none';
    elements.gameArea.style.display = 'none';
    elements.languageSwitch.style.visibility = 'hidden';
    elements.alphabet.style.visibility = 'hidden';
    elements.title.textContent = ''
    elements.topic.textContent = ''
    elements.topic.style.visibility = 'hidden';
    elements.hintLabel.textContent = ''
    elements.wordLabel.textContent = ''
    elements.guessLabel.textContent = ''
    elements.playAgainLabel.textContent = ''

    const url = language ? `/init?language=${language}` : '/init';
    const r = await (await fetch(url)).json();
    document.title = r.i18n['title']
    elements.title.textContent = r.i18n['title']
    elements.hintLabel.textContent = r.i18n['hint']
    elements.hintButton.textContent = r.i18n['show-hint']
    hintLockedTemplate = r.i18n['hint-locked']
    elements.wordLabel.textContent = r.i18n['word']
    elements.guessLabel.textContent = r.i18n['guess-a-letter']
    elements.playAgainLabel.textContent = r.i18n['play-again']
    elements.modeLabel.textContent = r.i18n['choose-mode']
    elements.modeTimed.textContent = r.i18n['mode-timed']
    elements.modeUntimed.textContent = r.i18n['mode-untimed']
    elements.howToPlay.textContent = r.i18n['how-to-play']
    elements.howToTitle.textContent = r.i18n['how-to-play']
    elements.howToPlayClose.textContent = r.i18n['close']
    elements.languageSwitch.style.visibility = 'visible'

    showModeSelect();
  }
  bindHandlers();
  await init();

}) ()

