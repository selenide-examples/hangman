package ee.era.hangman.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class WordsTest {
  Words words = new Words();

  @Before
  public void mockWords() {
    words.topics.clear();
    words.topics.put("дом", asList("гвоздь", "унитаз", "чайник"));
    words.topics.put("флора", asList("гвоздика", "куст"));
    words.topics.put("фауна", asList("верблюд"));
  }

  @Test
  public void choosesRandomTopicAndWord() {
    Map<String, Integer> count = new HashMap<String, Integer>();
    count.put("гвоздь", 0);
    count.put("унитаз", 0);
    count.put("чайник", 0);
    count.put("гвоздика", 0);
    count.put("куст", 0);
    count.put("верблюд", 0);

    for (int i=0; i<2*6000; i++) {
      Word randomWord = words.getRandomWord();
      count.put(randomWord.getWord(), count.get(randomWord.getWord()) + 1);
    }

    assertTrue(count.get("гвоздь") > 1000);
    assertTrue(count.get("унитаз") > 1000);
    assertTrue(count.get("чайник") > 1000);
    assertTrue(count.get("гвоздика") > 1000);
    assertTrue(count.get("куст") > 1000);
    assertTrue(count.get("верблюд") > 1000);
  }
}
