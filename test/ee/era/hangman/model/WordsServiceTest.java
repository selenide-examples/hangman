package ee.era.hangman.model;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class WordsServiceTest {
  private final DataSource dataSource = mock();
  private final WordsService wordsService = spy(new WordsService(dataSource));

  @Test
  public void choosesRandomTopicAndWord() {
    doReturn(asList(
        new Word("дом", "гвоздь"), new Word("дом", "унитаз"), new Word("дом", "чайник"),
        new Word("флора", "гвоздика"), new Word("флора", "куст"),
        new Word("фауна", "верблюд")
    )).when(wordsService).getDictionary("ru");

    Map<String, Integer> count = new HashMap<>();
    count.put("гвоздь", 0);
    count.put("унитаз", 0);
    count.put("чайник", 0);
    count.put("гвоздика", 0);
    count.put("куст", 0);
    count.put("верблюд", 0);

    for (int i = 0; i < 2 * 6000; i++) {
      Word randomWord = wordsService.getRandomWord("ru");
      count.put(randomWord.word(), count.get(randomWord.word()) + 1);
    }

    assertThat(count.get("гвоздь")).isGreaterThanOrEqualTo(1000);
    assertThat(count.get("унитаз")).isGreaterThanOrEqualTo(1000);
    assertThat(count.get("чайник")).isGreaterThanOrEqualTo(1000);
    assertThat(count.get("гвоздика")).isGreaterThanOrEqualTo(1000);
    assertThat(count.get("куст")).isGreaterThanOrEqualTo(1000);
    assertThat(count.get("верблюд")).isGreaterThanOrEqualTo(1000);
  }
}
