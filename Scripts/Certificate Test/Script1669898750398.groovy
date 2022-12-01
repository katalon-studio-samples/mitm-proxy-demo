import javax.net.ssl.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import internal.GlobalVariable as GlobalVariable

File clientSslCertificate = new File(GlobalVariable.clientCertPath);
KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(new FileInputStream(clientSslCertificate), GlobalVariable.certificatePassword.toCharArray());
