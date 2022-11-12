package demo.https.contact;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import android.util.Log;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import demo.https.pinning.R;

public class MyTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
            factory.init((KeyStore) null);
            for (TrustManager trustManager : factory.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        //获取网络中的证书信息
        X509Certificate certificate = chain[0];
        // 证书拥有者
        String subject = certificate.getSubjectDN().getName();
        // 证书颁发者
        String issuer = certificate.getIssuerDN().getName();

        Log.d("subject", subject);
        Log.d("issuer", issuer);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
