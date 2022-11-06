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
import net.lightbody.bmp.filters.RequestFilter;
import io.netty.handler.codec.http.*;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Call;
import org.openqa.selenium.Proxy;
import okhttp3.OkHttpClient.Builder as ClientBuilder
import okhttp3.Request.Builder as RequestBuilder

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.SecureRandom;
import java.util.stream.Stream;

//import com.katalon.proxy.filters.HttpsClientCertFilter;
//import com.katalon.proxy.filters.StaticContentFilter;

TrustManager[] trustAllCerts = [
    new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
];

// start the proxy
BrowserMobProxy proxy = new BrowserMobProxyServer();

proxy.setTrustAllServers(true)
proxy.start(0);

advancedHostResolver = proxy.getHostNameResolver();
advancedHostResolver.remapHost("local.foobar3000.com", "127.0.0.1");
//advancedHostResolver.remapHost("google.com", "142.251.32.174");
proxy.setHostNameResolver(advancedHostResolver);

addresses = advancedHostResolver.resolve("local.foobar3000.com")
println(addresses)

//RequestFilter clientCertFilter = new HttpsClientCertFilter()
//RequestFilter staticFilter = new StaticContentFilter()

proxy.addRequestFilter({ request, contents, info -> 
    println("${request.getMethod()} ${request.getUri()}")
    
    HttpResponse finalResponse = null
    
    if (request.getMethod().toString().startsWith("GET")) {
        println("Got it!")
        clientBuilder = new ClientBuilder()
        println("Created client builder...")
        
        clientSslCertificate = new File("/Users/coty/Code/katalon/demo/nodejs-ssl-trusted-peer-example/certs/client/my-app-client.p12")
        certificatePassword = "secret"

        keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(clientSslCertificate), certificatePassword.toCharArray());
        println("Loaded keystore...")

        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
        keyManagers = keyManagerFactory.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("SSL")
        sslContext.init(keyManagers, trustAllCerts, new SecureRandom())
        clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0] as X509TrustManager)
        println("Set SSL socket faactorey")
   
    client = clientBuilder.build()
    println("Buyilt client")

    httpMethod = HttpMethod.GET
    RequestBody requestBody = null;
    
    request = new RequestBuilder()
        .url("https://local.foobar3000:8043")
        .method(httpMethod.name(), requestBody)
        .build()
    println("Created request")

    Response response = client.newCall(request).execute()
    println("Executed call")
    
    println(response.body().string())
    println("Printed body")
    }

    return finalResponse;
})

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

WebUI.navigateToUrl('https://local.foobar3000.com:8043')

WebUI.verifyElementText(findTestObject('Object Repository/Page/pre_Message'), 'Hello, client.example.com!')

//WebUI.closeBrowser()


public Response doHttpsRequest(SSLContext sslContext, String url, HttpMethod httpMethod, String mediaType, byte[] body) {
    println("Doing HTTP request ${httpMethod} ${url} ${mediaType} ...")
    RequestBody requestBody = null;
    if (httpMethod != HttpMethod.GET) {
        // might need to prohibit body for other methods too
        requestBody = RequestBody.create(MediaType.get(mediaType), (byte[])body);
    }

    println("Building request ${httpMethod} ${url}...")
    Request request = null
    Request.Builder requestBuilder = new Request.Builder()
    println("instantiated request builder ${httpMethod} ${url}")
    requestBuilder.url(url)
    println("added url ${httpMethod} ${url}")
    requestBuilder.method(httpMethod.name(), requestBody)
    println("set method ${httpMethod} ${url}")
    request = requestBuilder.build();
    println("Request built! ${httpMethod} ${url}")
                    
    println("Building client...")
    OkHttpClient client = new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]) // for the non-deprecated version, a truststore must be used as a second parameter
            .build();
            
    println("Trying call...")
    try {
        Call call = client.newCall(request)
        println("Executing call...")
        Response response = call.execute();
        println("Returning response...")
        return response;
    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
}

public SSLContext createSslContext(File clientSslCertificate, String certificatePassword) {
    try {
        KeyStore keyStore = getAndLoadKeyStore(clientSslCertificate, certificatePassword)

        KeyManager[] keyManagers = getKeyManagers(keyStore, certificatePassword)

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);

        return sslContext;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public KeyStore getAndLoadKeyStore(File clientSslCertificate, String certificatePassword) {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(new FileInputStream(clientSslCertificate), certificatePassword.toCharArray());
}

public KeyManager[] getKeyManagers(KeyStore keyStore, String certificatePassword) {
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
    KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
}

private FullHttpResponse convertOkhttpResponseToNettyResponse(Response okhttpResponse) {
    HttpResponseStatus httpResponseStatus = HttpResponseStatus.valueOf(okhttpResponse.code());
    HttpVersion httpVersion = HttpVersion.valueOf(okhttpResponse.protocol().toString());

    ByteBuf content = null;
    try {
        ResponseBody body = okhttpResponse.body()
        content = Unpooled.wrappedBuffer(body.bytes());
    } catch (IOException e) {
        e.printStackTrace();
    }

    String stringContent = new String(content.array(), 'UTF-8')
    println(stringContent)
    stringContent.replaceAll('UNKNOWN', 'client.example.com')
    content = encoder.encode(CharBuffer.wrap(stringContent))

    FullHttpResponse nettyResponse = new DefaultFullHttpResponse(httpVersion, httpResponseStatus, content);
    okhttpResponse.headers().toMultimap().forEach({key, values ->
        nettyResponse.headers().remove(key);
        nettyResponse.headers().add(key, String.join(",", values));
    });

    return nettyResponse;
}