# Katalon Studio In-process Proxy/SSL Client Cert Demo

This project demonstrates how to use an in-process proxy server with Katalon Studio to attach SSL client certificates to requests when required for testing.

# Requirements

* Katalon Studio
* Gradle 6.9
* [Hashicorp Vault](https://www.vaultproject.io) (for storing certificate passwords)
* Application configured for authentication via SSL client certificate
  * The test case in this demo is based on this simple Node application: https://github.com/katalon-studio-samples/nodejs-ssl-trusted-peer-example

# Running the demo

You can view the demonstration by following these steps:

1. Install and configure the prequisites listed above.
2. Start a Vault server in development mode:

        vault server -dev

3. Export environment variables containing Vault server configuration:

        export VAULT_ADDR=http://127.0.0.1:8200
        export VAULT_TOKEN=[token from vault startup]

4. Configure passwords for the two sample certificates:

        vault kv put -mount=secret my-app-client.p12 password=secret
        vault kv put -mount=secret my-app-client2.p12 password=secret

5. Start the Node server:

        node ./server.js 8043

6. Install Vault Java library using gradle

        cd <Katalon project directory>
        gradle katalonCopyDependencies

7. Start Katalon Studio
8. Open this project
9.  Open the test suite "In-process Proxy Suite"
10. Run the test suite using Chrome, Firefox, or Edge Chromium (other browser configurations are not supported)

# How it works

This demonstration works by instantiating a [BrowserMob Proxy](https://github.com/lightbody/browsermob-proxy) server in the setUp method of a Katalon Studio test suite. It then configures the selected browser driver to use that proxy to connect to the designated application URL. The proxy intercepts any HTTPS connections to the configured URL and attaches the configured client certificate and sends the request on to the application at the URL.
