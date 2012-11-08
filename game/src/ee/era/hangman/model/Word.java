package ee.era.hangman.model;

public class Word {
  private final String topic;
  private final String word;

  public Word(String topic, String word) {
    this.topic = topic;
    this.word = word;
  }

  public String getTopic() {
    return topic;
  }

  public String getWord() {
    return word;
  }
}
