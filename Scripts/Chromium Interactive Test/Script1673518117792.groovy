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

import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType as WebUIDriverType
import org.openqa.selenium.Proxy as Proxy
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.remote.DesiredCapabilities as DesiredCapabilities
import org.openqa.selenium.remote.CapabilityType as CapabilityType
import org.openqa.selenium.chrome.ChromeOptions as ChromeOptions
import org.openqa.selenium.chrome.ChromeDriver as ChromeDriver
import org.openqa.selenium.firefox.FirefoxOptions as FirefoxOptions
import org.openqa.selenium.firefox.FirefoxDriver as FirefoxDriver
import org.openqa.selenium.safari.SafariOptions as SafariOptions
import org.openqa.selenium.safari.SafariDriver as SafariDriver
import org.openqa.selenium.edge.EdgeOptions as EdgeOptions
import org.openqa.selenium.edge.EdgeDriver as EdgeDriver
import net.lightbody.bmp.BrowserMobProxy as BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer as BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil as ClientUtil
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver as AdvancedHostResolver
import net.lightbody.bmp.proxy.jetty.util.*

import javax.swing.JOptionPane as JOptionPane
import javax.swing.JFrame as JFrame

Proxy seleniumProxy = GlobalVariable.proxy

if (seleniumProxy) {
    EdgeOptions options = new EdgeOptions()

    options.setProxy(seleniumProxy)

    System.setProperty('webdriver.edge.driver', DriverFactory.getEdgeChromiumDriverPath())

    // start the browser up
    driver = new EdgeDriver(options)
    DriverFactory.changeWebDriver(driver)
}

JFrame frame = new JFrame('User Input Frame')

frame.requestFocus()

result = JOptionPane.showConfirmDialog(frame, 'Routing through proxy at ' + seleniumProxy.getSslProxy())

WebUI.closeBrowser()