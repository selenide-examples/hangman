hangman
=======

The hangman game written in Java

An example of Java project using UNIT-tests and UI tests.

# Commands:
To compile & run unit-tests:

* `./gradlew test`

To run UI Tests (in FireFox):
  
* `./gradlew uitest`

To run UI Tests (in FireChrome):
  
* `./gradlew chrome`



# Sources
* `src` - Java sources
* `test/ee/` - unit-tests
* `test/uitest/` - UI tests
* `webapp` - web application resources

# Technology
* web framework - `Struts 2`
* unit-tests - `JUnit`
* UI Tests - `Selenide`
* Database - `H2` (in-memory database, especially useful in tests)
* Database migration - `LiquiBase`
* Dependency injection - `Guice`