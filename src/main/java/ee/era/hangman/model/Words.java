package ee.era.hangman.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Words {
  protected final Map<String, List<String>> topics = new HashMap<String, List<String>>();

  public Words() {
    topics.put("дом", asList("гвоздь", "унитаз", "чайник", "табурет", "стул", "ложка", "чашка", "пиала", "мухобойка", "компьютер", "люстра", "секция", "мебель", "балкон", "подвал"));
    topics.put("флора", asList("гвоздика", "куст", "флорист"));
    topics.put("фауна", asList("верблюд", "лис", "селёдка", "паук", "олень", "муха", "лошадь", "ягнёнок", "динозавр", "креветка"));
    topics.put("еда", asList("молоко", "холодец", "мороженое", "уксус", "кетчуп", "гамбургер", "какао"));
    topics.put("тело человека", asList("прыщ", "кровь", "козявка", "ноздря", "ягодицы", "мозжечок", "мозг", "ноготь"));
  }

  public Word getRandomWord() {
    String topic = chooseRandomKey(topics);
    List<String> topicWords = topics.get(topic);

    return new Word(topic, chooseRandomElement(topicWords));
  }

  private <K, V> K chooseRandomKey(Map<K, V> fromMap) {
    int keyNumber = (int) (Math.random() * fromMap.keySet().size());
    int i = 0;
    for (K key : fromMap.keySet()) {
      if (i++ == keyNumber) {
        return key;
      }
    }

    throw new RuntimeException("Invalid random key number: " + keyNumber + ", though map has " + fromMap.keySet().size() + " keys");
  }

  private <T> T chooseRandomElement(List<T> fromList) {
    int index = (int) (Math.random() * fromList.size());
    return fromList.get(index);
  }
}
