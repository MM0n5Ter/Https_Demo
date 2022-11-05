package demo.https.contact;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpsRequest {

    public int CreateSimpleHttps(String path){
        try{
            URL url = new URL("https://"+path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if(code==200){
                InputStream inStream = conn.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
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

    public int CreateOkHttps(String path){
        OkHttpClient client = new OkHttpClient();
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
}
