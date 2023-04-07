hangman
=======

The hangman game written in Java

An example of Java project using UNIT-tests and UI tests.

# Commands:
To compile & run unit-tests:

* `./gradlew test`

To run UI Tests (in FireFox):
  
* `./gradlew uitest_firefox`, or
* `./gradlew uitest_firefox -Dselenide.headless=true`

To run UI Tests (in Chrome):
  
* `./gradlew uitest_chrome`, or
* `./gradlew uitest_chrome -Dselenide.headless=true`

To run UI Tests (in Microsoft Edge):
  
* `./gradlew uitest_edge`, or
* `./gradlew uitest_edge -Dselenide.headless=true`



# Sources
* `src` - Java sources
* `test/ee/` - unit-tests
* `test/uitest/` - UI tests
* `src/webapp` - web application resources

# Technology
* unit-tests - `JUnit`
* UI Tests - `Selenide`
* Database - `H2` (in-memory database, especially useful in tests)
* Database migration - `LiquiBase`


# Thanks

Many thanks to these incredible tools that help us creating open-source software:

![Intellij IDEA](http://www.jetbrains.com/idea/docs/logo_intellij_idea.png) ![YourKit Java profiler](http://selenide.org/images/yourkit.png)

# License
Hangman is open-source project and distributed under [MIT](http://choosealicense.com/licenses/mit/) license
