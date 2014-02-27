package ee.era.hangman.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class Words {
  @Inject @Named("dictionary")
  private DataSource dataSource;

  public String getAlphabet(String language) {
    return getLanguages().get(language).getAlphabet();
  }

  public Word getRandomWord(String language) {
//    showTables();
//    showColumns("lang_words");
    List<Word> dictionary = getDictionary(language);
    return chooseRandomElement(dictionary);
  }

  private void showTables() {
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("Read database " + connection.getMetaData().getURL());

      try (PreparedStatement statement = connection.prepareStatement("SHOW TABLES")) {
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  private void showColumns(String tableName) {
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("Read database " + connection.getMetaData().getURL());

      try (PreparedStatement statement = connection.prepareStatement("SHOW COLUMNS FROM " + tableName)) {
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + ": " + resultSet.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  List<Word> getDictionary(String language) {
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement("select topic, word from lang_words where lang = ?")) {
        statement.setString(1, language);
        try (ResultSet resultSet = statement.executeQuery()) {
          List<Word> words = new ArrayList<>();
          while (resultSet.next()) {
            words.add(new Word(resultSet.getString(1), resultSet.getString(2)));
          }

          return words;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  Map<String, Language> getLanguages() {
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement("select lang, alphabet from languages")) {
        try (ResultSet resultSet = statement.executeQuery()) {
          Map<String, Language> languages = new HashMap<>();
          while (resultSet.next()) {
            Language language = new Language(resultSet.getString(1), resultSet.getString(2));
            languages.put(language.getName(), language);
          }
          return languages;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T chooseRandomElement(List<T> fromList) {
    int index = (int) (Math.random() * fromList.size());
    return fromList.get(index);
  }
}
