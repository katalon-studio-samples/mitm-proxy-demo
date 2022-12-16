package com.katalon.proxy.filters

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import net.lightbody.bmp.filters.HttpsAwareFiltersAdapter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class ClientCertFilter extends HttpsAwareFiltersAdapter {
    private SSLContext sslContext;
    private String hostname;
    public ClientCertFilter(HttpRequest originalRequest, ChannelHandlerContext ctx, SSLContext sslContext, String hostname) {
        super(originalRequest, ctx);
        this.sslContext = sslContext;
        this.hostname = hostname;
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
//        System.out.println("In clientToProxyRequest...");
        return super.clientToProxyRequest(httpObject);
    }

    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        System.out.println("In proxyToServerRequest...");

        FullHttpRequest request = (FullHttpRequest) httpObject;

        if (request.headers().get("Host").contains(hostname) &&
        request.method() != HttpMethod.CONNECT) {
            System.out.println("Creating response for ${request.getUri()}");

            FullHttpResponse nettyResponse;
            try {
                CharSequence mimeType = HttpUtil.getMimeType(request);
                String mediaType = mimeType == null ? "" : mimeType.toString();
                nettyResponse = (FullHttpResponse) filterRequest(request, mediaType, request.content());
            } catch (NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException |
            CertificateException | KeyStoreException | IOException e) {
                throw new RuntimeException(e);
            }

            return nettyResponse;
        } else {
            return super.proxyToServerRequest(httpObject);
        }
    }

    //<editor-fold desc="Overrides">
    @Override
    public void proxyToServerRequestSending() {
//        System.out.println("In proxyToServerRequestSending...");
        super.proxyToServerRequestSending();
    }

    @Override
    public void proxyToServerRequestSent() {
//        System.out.println("In proxyToServerRequestSent...");
        super.proxyToServerRequestSent();
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
//        System.out.println("In serverToProxyResponse...");
        return super.serverToProxyResponse(httpObject);
    }

    @Override
    public void serverToProxyResponseTimedOut() {
        System.out.println("In serverToProxyResponseTimedOut...");
        super.serverToProxyResponseTimedOut();
    }

    @Override
    public void serverToProxyResponseReceiving() {
//        System.out.println("In serverToProxyResponseReceiving...");
        super.serverToProxyResponseReceiving();
    }

    @Override
    public void serverToProxyResponseReceived() {
//        System.out.println("In serverToProxyResponseReceived...");
        super.serverToProxyResponseReceived();
    }

    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject) {
//        System.out.println("In proxyToClientResponse...");
        return super.proxyToClientResponse(httpObject);
    }

    @Override
    public void proxyToServerConnectionQueued() {
        System.out.println("In proxyToServerConnectionQueued...");
        super.proxyToServerConnectionQueued();
    }

    @Override
    public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
        System.out.println("In proxyToServerResolutionStarted for ${resolvingServerHostAndPort}");
        return super.proxyToServerResolutionStarted(resolvingServerHostAndPort);
    }

    @Override
    public void proxyToServerResolutionFailed(String hostAndPort) {
        System.out.println("In proxyToServerResolutionFailed for ${hostAndPort}");
        super.proxyToServerResolutionFailed(hostAndPort);
    }

    @Override
    public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {
        System.out.println("In proxyToServerResolutionSucceeded for ${serverHostAndPort}");
        super.proxyToServerResolutionSucceeded(serverHostAndPort, resolvedRemoteAddress);
        System.out.println("Resolved ${serverHostAndPort} to ${resolvedRemoteAddress}")
    }

    @Override
    public void proxyToServerConnectionStarted() {
//        System.out.println("In proxyToServerConnectionStarted...");
        super.proxyToServerConnectionStarted();
    }

    @Override
    public void proxyToServerConnectionSSLHandshakeStarted() {
//        System.out.println("In proxyToServerConnectionSSLHandshakeStarted...");
        super.proxyToServerConnectionSSLHandshakeStarted();
    }

    @Override
    public void proxyToServerConnectionFailed() {
        System.out.println("In proxyToServerConnectionFailed...");
        super.proxyToServerConnectionFailed();
    }

    @Override
    public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
//        System.out.println("In proxyToServerConnectionSucceeded...");
        super.proxyToServerConnectionSucceeded(serverCtx);
    }
    //</editor-fold>

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

    public HttpResponse filterRequest(HttpRequest request, String mediaType, ByteBuf content) throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException {
        String uri = request.uri();
        String url = "https://" + request.headers().get("Host") + uri;
        System.out.println("In filterRequest for ${request.method().name()} on URL: ${url}")

        Response intermediateResponse = doHttpsRequest(sslContext, url, request.method(), mediaType, content.array());
        assert intermediateResponse != null;
        HttpResponse finalResponse = convertOkhttpResponseToNettyResponse(intermediateResponse);

        return finalResponse;
    }

    private Response doHttpsRequest(SSLContext sslContext, String url, HttpMethod httpMethod, String mediaType, byte[] body)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException {
        if (httpMethod == HttpMethod.CONNECT) {
            return null;
        }

        RequestBody requestBody = null;
        if (httpMethod != HttpMethod.GET && httpMethod != HttpMethod.HEAD) {
            // might need to prohibit body for other methods too
            requestBody = RequestBody.create(MediaType.get(mediaType), (byte[])body);
        }


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // ExtensionsKt.useOperatingSystemCertificateTrustStore(builder);
        builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]); // for the non-deprecated version, a truststore must be used as a second parameter
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
        builder.hostnameVerifier(hostnameVerifier);
        builder.addInterceptor(new SSLHandshakeInterceptor());
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .method(httpMethod.name(), requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FullHttpResponse convertOkhttpResponseToNettyResponse(Response okhttpResponse) {
        HttpResponseStatus httpResponseStatus = HttpResponseStatus.valueOf(okhttpResponse.code());
        HttpVersion httpVersion = HttpVersion.valueOf(okhttpResponse.protocol().toString());

        ByteBuf content = null;
        try {
            ResponseBody body = okhttpResponse.body();
            content = Unpooled.wrappedBuffer(body.bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        FullHttpResponse nettyResponse = new DefaultFullHttpResponse(httpVersion, httpResponseStatus, content);
        okhttpResponse.headers().toMultimap().forEach({key, values ->
            nettyResponse.headers().remove(key);
            nettyResponse.headers().add(key, String.join(",", values));
        });

        return nettyResponse;
    }
}
