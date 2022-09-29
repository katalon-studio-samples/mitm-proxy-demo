import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.Proxy
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chrome.ChromeDriver

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

// start the proxy
BrowserMobProxy proxy = new BrowserMobProxyServer();
proxy.start(0);

// get the Selenium proxy object
Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

// configure it as a desired capability
DesiredCapabilities capabilities = new DesiredCapabilities();
capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

ChromeOptions options = new ChromeOptions();
options.setAcceptInsecureCerts(true)

capabilities.setCapability(ChromeOptions.CAPABILITY, options);

System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeDriverPath())

// start the browser up
ChromeDriver driver = new ChromeDriver(capabilities);

DriverFactory.changeWebDriver(driver)

WebUI.navigateToUrl('https://google.com')
