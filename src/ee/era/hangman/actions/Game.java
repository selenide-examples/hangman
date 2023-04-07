package ee.era.hangman.actions;

import ee.era.hangman.Request;
import ee.era.hangman.Response;
import ee.era.hangman.Session;
import ee.era.hangman.model.Hangman;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.WordsService;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

import static ee.era.hangman.Response.json;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class Game {
  private final WordsService wordsService;

  public Game(WordsService wordsService) {
    this.wordsService = wordsService;
  }

  public Response startGame(Request request) {
    Session session = request.getSession();
    String language = requireNonNull(session.getLanguage());
    Word randomWord = wordsService.getRandomWord(language);
    Hangman game = new Hangman(randomWord.getWord());
    session.setAttribute("hangman", game);

    Map<String, Object> response = Map.of(
      "alphabet", wordsService.getAlphabet(language),
      "language", language,
      "word", game.getWord(),
      "topic", randomWord.getTopic()
    );
    return json(200, response);
  }
}
