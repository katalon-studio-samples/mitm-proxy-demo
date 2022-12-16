package com.katalon.proxy.filters

import java.io.IOException;
import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.Headers;
import okhttp3.TlsVersion;

/** Prints TLS Version and Cipher Suite for SSL Calls through OkHttp3 */
public class SSLHandshakeInterceptor implements okhttp3.Interceptor {

    private static final String TAG = "OkHttp3-SSLHandshake";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers headers = request.headers();
        Set<String> headerNames = headers.names();

        for (headerName in headerNames) {
            System.out.println(headerName);
        }

        System.out.println(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        final Response response = chain.proceed(request);
        printTlsAndCipherSuiteInfo(response);
        return response;
    }

    private void printTlsAndCipherSuiteInfo(Response response) {
        if (response != null) {
            Handshake handshake = response.handshake();
            if (handshake != null) {
                final CipherSuite cipherSuite = handshake.cipherSuite();
                final TlsVersion tlsVersion = handshake.tlsVersion();
                System.out.println("TLS: " + tlsVersion + ", CipherSuite: " + cipherSuite);
            }
        }
    }
}