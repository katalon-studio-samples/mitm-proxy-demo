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

import com.charleskorn.okhttp.systemkeystore.*
import okhttp3.OkHttpClient.Builder as ClientBuilder
import okhttp3.Request.Builder as RequestBuilder
import okhttp3.Response
import okhttp3.RequestBody
//import okio.IOException
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.* 

import io.netty.handler.codec.http.HttpMethod;

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
        .url("https://127.0.0.1:8043")
        .method(httpMethod.name(), requestBody)
        .build()

    Response response = client.newCall(request).execute()
    
    println(response.body().string())
