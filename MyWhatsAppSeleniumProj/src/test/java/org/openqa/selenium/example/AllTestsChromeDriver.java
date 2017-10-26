package org.openqa.selenium.example;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class AllTestsChromeDriver extends TestCase {

  private static ChromeDriverService service;
  private WebDriver driver;

  @BeforeClass
  public static void createAndStartService() throws IOException {
    System.setProperty("webdriver.chrome.driver", "C:/Users/Javier/workspace_mars/MySelenium20Proj/chromedriver.exe");
    service = new ChromeDriverService.Builder()
        .usingDriverExecutable(new File("C:/Users/Javier/workspace_mars/MySelenium20Proj/chromedriver.exe"))
        .usingAnyFreePort()
        .build();
    service.start();
  }

  @AfterClass
  public static void createAndStopService() {
    service.stop();
  }

  @Before
  public void createDriver() {
    driver = new RemoteWebDriver(service.getUrl(),
        DesiredCapabilities.chrome());
  }

  @After
  public void quitDriver() {
    driver.quit();
  }

  @Test
  public void testGoogleSearch() {
    driver.get("http://www.google.com");
    WebElement searchBox = driver.findElement(By.name("q"));
    searchBox.sendKeys("webdriver");
    //driver.quit();
    assertEquals("Google", driver.getTitle());
  }
}