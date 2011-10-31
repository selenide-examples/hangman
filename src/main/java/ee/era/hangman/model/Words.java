package ee.era.hangman.model;

import java.util.List;

import static java.util.Arrays.asList;

public class Words {
  private final List<TopicWords> dictionary;

  public Words() {
    this(asList(
        new TopicWords("дом", "гвоздь", "унитаз", "чайник", "табурет", "стул", "ложка", "чашка", "пиала", "мухобойка", "компьютер", "люстра", "секция", "мебель", "балкон", "подвал"),
        new TopicWords("флора", "гвоздика", "куст", "флорист"),
        new TopicWords("фауна", "верблюд", "лис", "селёдка", "паук", "олень", "муха", "лошадь", "ягнёнок", "динозавр", "креветка"),
        new TopicWords("еда", "молоко", "холодец", "мороженое", "уксус", "кетчуп", "гамбургер", "какао"),
        new TopicWords("тело человека", "прыщ", "кровь", "козявка", "ноздря", "ягодицы", "мозжечок", "мозг", "ноготь", "грудь", "висок", "перхоть"),
        new TopicWords("веселье", "цирк")
    ));
  }

  protected Words(List<TopicWords> dictionary) {
    this.dictionary = dictionary;
  }

  public Word getRandomWord() {
    TopicWords topic = chooseRandomElement(dictionary);

    return new Word(topic.getTopic(), chooseRandomElement(topic.getWords()));
  }

  private <T> T chooseRandomElement(List<T> fromList) {
    int index = (int) (Math.random() * fromList.size());
    return fromList.get(index);
  }
}
