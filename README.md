# Katalon Studio mitmproxy Demo

This project demonstrates how to use [mitmproxy][1] (or really any similar proxy) with Katalon Studio. In particular, it contains examples of configuring [Desired Capabilities][2] for a Chrome browser to connect to a proxy running locally on port 8080. It contains examples for doing that configuring both in Katalon Studio project settings and in code.

# Requirements

* Katalon Studio
* [mitmproxy][1]
# Installing and configuring mitmproxy

You should be able to follow the standard instructions for [installing](https://docs.mitmproxy.org/stable/overview-installation/) and [configuring](https://docs.mitmproxy.org/stable/overview-getting-started/) mitmproxy to run on your platform.
# Configuring project proxy settings

You can see the project settings that configure Katalon Studio scripts to connect to mitmproxy running locally on port 8080 by selecting Project / Settings then Desired Capabilities / Web UI / Chrome in the Project Settings dialog.

# Configuring proxy via code

You can also configure proxy usage in a Katalon Studio test using code. An example can be seen in the "Proxy Test - Code" test case in this project.

# Bypassing SSL warnings

This project also shows how to bypass SSL warnings that are normally displayed by browsers when the certificate for an SSL connection does not match the target domain name. 

There is also a test case named "Proxy Config" which will open a browser window where one can install a certificate that will allow the mitmproxy to generate certificates on-the-fly to bypass these browser checks. More details on this process can be found [here][3].

[1]: <https://mitmproxy.org> "mitmproxy"
[2]: <https://docs.katalon.com/docs/author/manage-projects/project-settings/desired-capabilities/introduction-to-desired-capabilities-in-katalon-studio> "Introduction to Desired Capabilities in Katalon Studio"
[3]: <https://docs.mitmproxy.org/stable/concepts-certificates/> "mitmproxy Certificates"
