import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.katalon.proxy.filters.TracingFilter;

public class SslBrowserMobProxyServer extends BrowserMobProxyServer {
    private String clientCertPath;
    private String clientCertPassword;
    private SSLContext sslContext;

    private String hostname;

    TrustManager[] trustAllCerts = [
        new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }
    ];

    public SslBrowserMobProxyServer(String clientCertPath, String clientCertPassword, String hostname) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        this.clientCertPath = clientCertPath;
        this.clientCertPassword = clientCertPassword;
        this.hostname = hostname;

        File clientSslCertificate = new File(clientCertPath);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(clientSslCertificate), clientCertPassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, clientCertPassword.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(keyManagers, trustAllCerts, null);
    }

    @Override
    protected void addBrowserMobFilters() {
        super.addBrowserMobFilters();
        this.addHttpFilterFactory(new HttpFiltersSourceAdapter() {
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        return new TracingFilter(originalRequest, ctx, sslContext, hostname);
                    }
                });
    }
}
