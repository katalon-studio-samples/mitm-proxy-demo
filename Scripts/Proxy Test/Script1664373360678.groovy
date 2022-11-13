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

WebUI.openBrowser('')

if (GlobalVariable.g_proxy) {
    // get the Selenium proxy object
    Proxy seleniumProxy = GlobalVariable.g_proxy

    WebUIDriverType driverType = DriverFactory.getExecutedBrowser()

    WebDriver driver = null

    switch (driverType) {
        case WebUIDriverType.CHROME_DRIVER:
            ChromeOptions options = new ChromeOptions()

            options.setAcceptInsecureCerts(true)

            options.setProxy(seleniumProxy)

            System.setProperty('webdriver.chrome.driver', DriverFactory.getChromeDriverPath())

            // start the browser up
            driver = new ChromeDriver(options)

            break
        case WebUIDriverType.FIREFOX_DRIVER:
            FirefoxOptions options = new FirefoxOptions()

            options.setAcceptInsecureCerts(true)

            options.setProxy(seleniumProxy)

            // start the browser up
            driver = new FirefoxDriver(options)

            break
        case WebUIDriverType.SAFARI_DRIVER:
            throw new Exception('Safari is not currently supported!')
            
            break
        case WebUIDriverType.EDGE_CHROMIUM_DRIVER:
            EdgeOptions options = new EdgeOptions()

            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true)

            options.setProxy(seleniumProxy)

            System.setProperty('webdriver.edge.driver', DriverFactory.getEdgeChromiumDriverPath())

            // start the browser up
            driver = new EdgeDriver(options)

            break
    }
    
    DriverFactory.changeWebDriver(driver)
}

WebUI.navigateToUrl('https://local.foobar3000.com:8043')

WebUI.verifyElementText(findTestObject('Object Repository/Page/pre_Message'), "Hello, ${GlobalVariable.g_certificateCN}!")

WebUI.closeBrowser()

