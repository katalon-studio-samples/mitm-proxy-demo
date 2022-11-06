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
proxy.start(8888);

proxy.addRequestFilter({ request, contents, info ->
    println("${request.getMethod()} ${request.getUri()}")
    println("${info.isHttps()} ${info.getUrl()} ${info.getOriginalUrl()}")
    
    if ((request.getMethod() != HttpMethod.GET) || !info.getUrl().contains("foobar3000")) {
        println("Bypassing filter...")
        return null;
    }
    
    allowUntrusted = true
    
    clientBuilder = new ClientBuilder()
        
    if (allowUntrusted) {
    
            clientSslCertificate = new File("/Users/coty/Code/katalon/demo/nodejs-ssl-trusted-peer-example/certs/client/my-app-client.p12")
            certificatePassword = "secret"
    
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(clientSslCertificate), certificatePassword.toCharArray());
    
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
            keyManagers = keyManagerFactory.getKeyManagers();
    
            SSLContext sslContext = SSLContext.getInstance("SSL")
            sslContext.init(keyManagers, trustAllCerts, new SecureRandom())
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0] as X509TrustManager)
    //        var hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ ->
    //            true
    //        }
    //        clientBuilder.hostnameVerifier(hostnameVerifier)
        }
    
        client = clientBuilder.build()
    
        httpMethod = HttpMethod.GET
        RequestBody requestBody = null;
        
        request = new RequestBuilder()
            .url(info.getUrl())
            .method(httpMethod.name(), requestBody)
            .build()
    
        Response response = client.newCall(request).execute()
        
        println("Request filter: returning filtered response...")
        println(response.body().string())
//        contents.setTextContents(response.body().string())
        
        return response
})

proxy.addRequestFilter({ request, contents, info ->
    println("More stuff...")
    return null
})


proxy.addResponseFilter({ _, contents, info ->
    println("Filtering response...")
    request = info.getOriginalRequest()
    println("${request.getMethod()} ${request.getUri()}")
    println("${info.isHttps()} ${info.getUrl()} ${info.getOriginalUrl()}")
    if (info.getOriginalUrl().contains("foobar3000")) {
        if ((request.getMethod() != HttpMethod.GET) || !info.getUrl().contains("foobar3000")) {
            println("Bypassing filter...")
            return null;
        }
        
        allowUntrusted = true
        
        clientBuilder = new ClientBuilder()
            
        if (allowUntrusted) {
        
                clientSslCertificate = new File("/Users/coty/Code/katalon/demo/nodejs-ssl-trusted-peer-example/certs/client/my-app-client.p12")
                certificatePassword = "secret"
        
                keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(new FileInputStream(clientSslCertificate), certificatePassword.toCharArray());
        
                keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
                keyManagers = keyManagerFactory.getKeyManagers();
        
                SSLContext sslContext = SSLContext.getInstance("SSL")
                sslContext.init(keyManagers, trustAllCerts, new SecureRandom())
                clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0] as X509TrustManager)
        //        var hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ ->
        //            true
        //        }
        //        clientBuilder.hostnameVerifier(hostnameVerifier)
            }
        
            client = clientBuilder.build()
        
            httpMethod = HttpMethod.GET
            RequestBody requestBody = null;
            
            request = new RequestBuilder()
                .url(info.getUrl())
                .method(httpMethod.name(), requestBody)
                .build()
        
            Response response = client.newCall(request).execute()
            
            println("Response filter: returning filtered response...")
            String responseString = response.body().string()
            println(reponseString)
            contents.setTextContents(responseString)
    }
})

advancedHostResolver = proxy.getHostNameResolver();
advancedHostResolver.remapHost("local.foobar3000.com", "127.0.0.1");
//advancedHostResolver.remapHost("google.com", "142.251.32.174");
proxy.setHostNameResolver(advancedHostResolver);

addresses = advancedHostResolver.resolve("local.foobar3000.com")
println(addresses)

Thread.sleep(5 * 60 * 1000)