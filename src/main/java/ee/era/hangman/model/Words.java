package ee.era.hangman.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Words {
  private final Map<String, Language> languages;

  public Words() {
    this(
        new Language("ru", "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ", asList(
            new TopicWords("дом", "гвоздь", "унитаз", "чайник", "табурет", "стул", "ложка", "чашка", "пиала", "мухобойка", "компьютер", "люстра", "секция", "мебель", "балкон", "подвал"),
            new TopicWords("флора", "гвоздика", "куст", "флорист", "плющ", "мухоловка", "баобаб"),
            new TopicWords("фауна", "верблюд", "лис", "селёдка", "паук", "олень", "муха", "лошадь", "ягнёнок", "динозавр", "креветка"),
            new TopicWords("еда", "молоко", "холодец", "мороженое", "уксус", "кетчуп", "гамбургер", "какао", "солянка"),
            new TopicWords("тело человека", "прыщ", "кровь", "козявка", "ноздря", "ягодицы", "мозжечок", "мозг", "ноготь", "грудь", "висок", "перхоть", "хрящ", "клык", "щёки", "лицо"),
            new TopicWords("веселье", "цирк")
        )),
        new Language("et", "ABDEFGHIJKLMNOPRSŠZŽTUVÕÄÖÜ", asList(
            new TopicWords("kodu", "vood", "laud", "tool", "kapp", "hiir", "kušett", "rõdu", "aken", "aknalaud"),
            new TopicWords("söök", "juust", "vorst", "või", "võileib", "burger", "viiner", "puder", "seljanka", "borš", "šnitsel", "piim", "kohupiim", "seen", "pitsa"),
            new TopicWords("fauna", "krooks", "kikerikii", "kukeleegu", "muu", "huige", "küünis")
        )),
        new Language("en", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", asList(
            new TopicWords("house", "bedroom")
        ))
    );
  }

  protected Words(Language... languages) {
    this.languages = new HashMap<String, Language>(languages.length);
    for (Language language : languages) {
      this.languages.put(language.getName(), language);
    }
  }

  public String getAlphabet(String language) {
    return languages.get(language).getAlphabet();
  }

  public Word getRandomWord(String language) {
    TopicWords topic = chooseRandomElement(languages.get(language).getDictionary());

    return new Word(topic.getTopic(), chooseRandomElement(topic.getWords()));
  }

  private <T> T chooseRandomElement(List<T> fromList) {
    int index = (int) (Math.random() * fromList.size());
    return fromList.get(index);
  }
}
