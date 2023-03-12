package uitest.selenium_page_objects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

class SeleniumFactory {
  static WebDriver createDriver() {
    String browser = System.getProperty("selenide.browser", "chrome");
    switch (browser) {
      case "chrome": return initChromeDriver();
      case "firefox": return initFirefoxDriver();
      case "edge": return initEdgeDriver();
      default: throw new IllegalArgumentException("Unknown selenide.browser='" + browser + "'");
    }
  }

  private static ChromeDriver initChromeDriver() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    setChromiumHeadless(options);
    options.addArguments("--remote-allow-origins=*");
    return new ChromeDriver(options);
  }

  private static FirefoxDriver initFirefoxDriver() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions options = new FirefoxOptions();
    if ("true".equals(System.getProperty("selenide.headless"))) {
      options.addArguments("-headless");
    }
    return new FirefoxDriver(options);
  }

  private static EdgeDriver initEdgeDriver() {
    WebDriverManager.edgedriver().setup();
    EdgeOptions options = new EdgeOptions();
    setChromiumHeadless(options);
    options.addArguments("--remote-allow-origins=*");
    return new EdgeDriver();
  }

  private static void setChromiumHeadless(ChromiumOptions<?> options) {
    if ("true".equals(System.getProperty("selenide.headless"))) {
      options.addArguments("--headless=new");
    }
  }
}
