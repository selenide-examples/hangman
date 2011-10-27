package ee.era.hangman.model;

import java.util.List;

import static java.util.Arrays.asList;

public class TopicWords {
  private final String topic;
  private final List<String> words;

  public TopicWords(String topic, String... words) {
    this.topic = topic;
    this.words = asList(words);
  }

  public String getTopic() {
    return topic;
  }

  public List<String> getWords() {
    return words;
  }
}
