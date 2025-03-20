package ee.era.hangman;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Objects.requireNonNull;

public class Session {
  private final String id;
  private LocalDateTime lastAccessedAt = now();
  private final Map<String, Object> attributes = new HashMap<>();

  public Session(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

  public boolean isExpired() {
    return lastAccessedAt.isBefore(now().minus(10, MINUTES));
  }

  public void updateTimestamp() {
    this.lastAccessedAt = now();
  }

  @SuppressWarnings("unchecked")
  public <T> T getAttribute(String attributeName) {
    return (T) attributes.get(attributeName);
  }

  public void setAttribute(String attributeName, Object attributeValue) {
    attributes.put(attributeName, attributeValue);
  }

  @Nullable
  public String getLanguage() {
    return getAttribute("language");
  }

  public void setLanguage(String language) {
    setAttribute("language", requireNonNull(language));
  }

}
