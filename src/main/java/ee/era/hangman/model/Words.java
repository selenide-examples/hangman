package ee.era.hangman.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Words {
  private static final Map<String, List<String>> topics = new HashMap<String, List<String>>();

  static {
    topics.put("дом", asList("гвоздь", "унитаз", "чайник", "табурет", "стул", "ложка", "чашка", "пиала", "мухобойка", "компьютер", "люстра", "секция", "мебель", "балкон", "подвал"));
    topics.put("флора", asList("гвоздика", "куст", "флорист"));
    topics.put("фауна", asList("верблюд", "лис", "селёдка", "паук", "олень", "муха", "лошадь", "ягнёнок", "динозавр", "креветка"));
    topics.put("еда", asList("молоко", "холодец", "мороженое", "уксус", "кетчуп", "гамбургер", "какао"));
    topics.put("тело человека", asList("прыщ", "кровь", "козявка", "ноздря", "ягодицы", "мозжечок", "мозг", "ноготь"));
  }

  public Word getRandomWord() {
    return new Word("дом", "гвоздь");
  }
}
