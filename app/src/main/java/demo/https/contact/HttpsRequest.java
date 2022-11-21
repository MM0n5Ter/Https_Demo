package demo.https.contact;

import android.util.Log;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpsRequest {
    Context context;
    int Id;
    public HttpsRequest(Context r){
        this.context = r;
    }

    public void setId(int id){
        Id = id;
    }

    public int CreateSimpleHttps(String path) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, KeyManagementException{
        KeyStore keyStore = buildKeyStore(context, Id);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        try{
            URL url = new URL("https://"+path);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if(code==200){
                InputStream inStream = conn.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                inStream.close();
                String res = outStream.toString("UTF-8");
                Log.d("GRAB", res);
            }
            return code;
        } catch (Throwable e) {
            Log.e("ERROR", e.toString());
            return -1;
        }
    }

    public int CreateOkHttps(String path) {

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .get().url("https://"+path).build();
        try{
            Response response = client.newCall(request).execute();
            Log.d("GRAB", Objects.requireNonNull(response.body()).string());
            Objects.requireNonNull(response.body()).close();
            return response.code();
        } catch (Throwable e){
            Log.e("ERROR", e.toString());
            return -1;
        }
    }

    private static KeyStore buildKeyStore(Context context, int certId) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {
        // init a default key store
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        // read and add certificate authority
        Certificate cert = readCert(context, certId);
        keyStore.setCertificateEntry("alter", cert);

        return keyStore;
    }


    private static Certificate readCert(Context context,int certID) throws CertificateException, IOException {
        InputStream CaInput = context.getResources().openRawResource(certID);

        Certificate ca;
        try {
            // generate a certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(CaInput);
        } finally {
            CaInput.close();
        }

        return ca;

    }
}

