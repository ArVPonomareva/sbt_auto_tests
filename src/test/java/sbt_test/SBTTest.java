package sbt_test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.yandex.qatools.allure.annotations.Title;

@Title("SBT Test suite")
public class SBTTest {
	
	static WebDriver driver = new FirefoxDriver();
	static String UrlValue;
	static String NumberValue;
	
	// Preconditions
	@BeforeClass
	 public static void readXml() throws ParserConfigurationException, SAXException, IOException{
		 //Read xml file
		 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		 Document doc = docBuilder.parse ("src\\test\\resources\\param.xml");
		// normalize text representation
		    doc.getDocumentElement().normalize();
		    //Read Url
		    NodeList UrlList = doc.getElementsByTagName("url");
		    Node Url = UrlList.item(0);
		    UrlValue = Url.getTextContent();
		    //Read Number
		    NodeList NumberList = doc.getElementsByTagName("number");
		    Node Number = NumberList.item(0);
		    NumberValue = Number.getTextContent();
		    startWebDriver();
    }
	 
		 public static void startWebDriver(){		
		 //Open start page
		 driver.navigate().to(UrlValue);
		 openCalculateVidget();
	 }
	
	 public static void openCalculateVidget(){
		// Open vidget
		 WebDriverWait wait = new WebDriverWait(driver, 15);
		 WebElement OptionsButton = driver.findElement(By.className("hide-i"));
		 OptionsButton.click();
		 WebElement InstrumentButton = driver.findElement(By.className("catalog-w"));
		 InstrumentButton.click();
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sbrf-widget-catalog-widgets")));
		 wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@data-item-name='SBRF_CurrencyConv_V1']/a"))).click();
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("currency-converter-widget")));
	}	
	 
	// Postconditions
	@AfterClass
	public static void closeWebDriver(){
		if (driver != null) {			
		// Close browser
		driver.close();
		driver.quit();
		}
	 }
	
	@Title("Minimize and maximize widget")
	@Test
	 public void MinMax(){
		
		  // First Test. Minimize and maximize widget
		  // Open options
		  WebDriverWait wait = new WebDriverWait(driver, 15);
		  WebElement OptionsButton = wait.until(ExpectedConditions
				  .elementToBeClickable(By.className("hide-i")));
		  OptionsButton.click();
		  // Minimize widget
		  WebElement MinimizeButton = driver.findElement(By.className("minimize-w"));
		  wait.until(ExpectedConditions
				  .elementToBeClickable(By.className("minimize-w")));
		  MinimizeButton.click();
		  // Maximize widget
		  WebElement ExchangeRateString = driver.findElement(By.id("from"));		  
		  Assert.assertTrue("Виджет не свернулся", !(ExchangeRateString.isDisplayed()));
		  MinimizeButton.click();
		  Assert.assertTrue("Виджет не развернулся",ExchangeRateString.isDisplayed());
		  
	 }	
	
	
	@Title("Check exchange rate functional")
	@Test
	public void ExchRate(){

		  // Second Test. Check exchange rate functional
		  WebElement ExchangeRateString = driver.findElement(By.id("from"));
		  // Enter data
		  ExchangeRateString.sendKeys(NumberValue);
		  WebElement ExchangeRate = driver.findElement(By.xpath(".//*/div/div/div[1]/div/div[2]/div/div/div[2]/span[5]"));
		  WebElement ExchangeRateResult = driver.findElement(By.xpath("//*[@id='to']"));
		  Float summary = Math.round(Float.valueOf(ExchangeRateResult.getAttribute("value"))/Float.valueOf(NumberValue)*10000)/10000.0f;
		  Assert.assertEquals("Курс посчитан не правильно", summary.toString(), ExchangeRate.getText());
	 }
	
	@Title("Check today date")
	@Test
    public void CheckDate(){

		  // Third Test. Check today date
		  WebElement ConvertionDate = driver.findElement(By.className("currency-converter-date"));
		  DateFormat dateFormat = new SimpleDateFormat("dd MMMMMMMM YYYY");
		  Date date = new Date();
		  // Find today date
		  String formatdate= dateFormat.format(date);
		  Assert.assertTrue("Дата на виджете не совпадает с текущей",formatdate.equals(ConvertionDate.getText()));
	 }
	
}
