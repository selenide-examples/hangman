hangman
=======

The hangman game written in Java

An example of Java project using UNIT-tests and UI tests.

# Commands:
To compile & run unit-tests:

* `./gradlew test`

To run UI Tests (in FireFox):
  
* `./gradlew uitest_firefox`

To run UI Tests (in Chrome):
  
* `./gradlew uitest_chrome`

To run UI Tests (in Microsoft Edge):
  
* `./gradlew uitest_edge`



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


# Thanks

Many thanks to these incredible tools that help us creating open-source software:

![Intellij IDEA](http://www.jetbrains.com/idea/docs/logo_intellij_idea.png) ![YourKit Java profiler](http://selenide.org/images/yourkit.png)

# License
Hangman is open-source project and distributed under [MIT](http://choosealicense.com/licenses/mit/) license
