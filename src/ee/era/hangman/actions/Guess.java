package ee.era.hangman.actions;

import ee.era.hangman.Request;
import ee.era.hangman.Response;
import ee.era.hangman.model.Hangman;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class Guess {

  public Response guessLetter(Request request) throws IOException {
    Hangman game = request.getSession().getAttribute("hangman");
    char letter = request.param("letter").charAt(0);
    boolean guessed = game.guessLetter(letter);

    Map<String, Serializable> response = Map.of(
      "word", game.getWord(),
      "guessed", guessed,
      "gameOver", game.isLost() || game.isWon(),
      "failures", game.getErrors()
    );
    return Response.json(200, response);
  }
}
