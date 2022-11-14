import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.SetUp
import com.kms.katalon.core.annotation.SetupTestCase
import com.kms.katalon.core.annotation.TearDown
import com.kms.katalon.core.annotation.TearDownTestCase

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
import com.kms.katalon.core.webui.driver.WebUIDriverType
import org.openqa.selenium.Proxy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.safari.SafariOptions
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.edge.EdgeDriver

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver;
import net.lightbody.bmp.proxy.jetty.util.*;

import com.bettercloud.vault.*

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Some methods below are samples for using SetUp/TearDown in a test suite.
 */

/**
 * Setup test suite environment.
 */
@SetUp(skipped = false) // Please change skipped to be false to activate this method.
def setUp() {
	// Put your code here.
	// start the proxy
    String clientCertPath = GlobalVariable.g_clientCertPath;
	Path clientCertPathObj = Paths.get(clientCertPath)
	String clientCertFilename = clientCertPathObj.getFileName().toString()
    String hostname = GlobalVariable.g_hostname
	String secretPath = "secret/${clientCertFilename}"
	
	VaultConfig config = new VaultConfig().address('http://127.0.0.1:8200').token('hvs.IWphVIMz5N9bHmoKLwxKLKGG').build()
	
	// You may choose not to provide a root token initially, if you plan to use
	// the Vault driver to retrieve one programmatically from an auth backend.
	// VaultConfig config = new VaultConfig().address('http://127.0.0.1:8200').build()
	
	final Vault vault = new Vault(config)
	
	// Read operation
	final String certificatePassword = vault.logical()
						   .read(secretPath)
						   .getData().get("password")
    
	BrowserMobProxy proxy = new SslBrowserMobProxyServer(
        clientCertPath,
        certificatePassword,
        hostname
    );
    proxy.setTrustAllServers(true);
	proxy.start(0);
    
    AdvancedHostResolver advancedHostResolver = proxy.getHostNameResolver();
    advancedHostResolver.remapHost("local.foobar3000.com", "127.0.0.1");
    advancedHostResolver.remapHost("google.com", "142.251.32.174");
    proxy.setHostNameResolver(advancedHostResolver);
    
    proxy.addRequestFilter({request, contents, messageInfo -> 
        return null;
    });
	
	// get the Selenium proxy object
	Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
	    
    GlobalVariable.g_proxy = seleniumProxy;
}

/**
 * Clean test suites environment.
 */
@TearDown(skipped = true) // Please change skipped to be false to activate this method.
def tearDown() {
	// Put your code here.
}

/**
 * Run before each test case starts.
 */
@SetupTestCase(skipped = true) // Please change skipped to be false to activate this method.
def setupTestCase() {
	// Put your code here.
}

/**
 * Run after each test case ends.
 */
@TearDownTestCase(skipped = true) // Please change skipped to be false to activate this method.
def tearDownTestCase() {
	// Put your code here.
}

/**
 * References:
 * Groovy tutorial page: http://docs.groovy-lang.org/next/html/documentation/
 */