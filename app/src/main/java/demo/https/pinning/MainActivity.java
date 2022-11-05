package demo.https.pinning;

import demo.https.contact.HttpsRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Button btnSimple,btnOKhttp;
    private EditText requestAddress;
    private String defaultURL = "www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnSimple = (Button) findViewById(R.id.simple_https_request);
        btnOKhttp = (Button) findViewById(R.id.okhttp_https_request);
        requestAddress = (EditText) findViewById(R.id.RequestAddress);

        btnSimple.setOnClickListener(new btnSimpleClick());
        btnOKhttp.setOnClickListener(new btnOKhttpClick());
    }

    class btnSimpleClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    String newURL = requestAddress.getText().toString().trim();
                    if(newURL.length()==0) {newURL = defaultURL;}
                    int code = new HttpsRequest().CreateSimpleHttps(newURL);
                    Toast ero;
                    if(code!=-1){
                        ero = Toast.makeText(getApplicationContext(), "Connection Succeed!\nResponse Code:" + code, Toast.LENGTH_SHORT);
                    }else{
                        ero = Toast.makeText(getApplicationContext(), "出戳啦\n", Toast.LENGTH_SHORT);
                    }
                    ero.show();
                    Looper.loop();
                }
            }).start();

        }
    }

    class btnOKhttpClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String newURL = requestAddress.getText().toString().trim();
            if(newURL.length()==0) {newURL = defaultURL;}

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .get().url("https://"+newURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("GRAB", "onFailure: ");
                    Looper.prepare();
                    Toast ero = Toast.makeText(getApplicationContext(), "出戳啦\n" + e.toString(),Toast.LENGTH_SHORT);
                    ero.show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("GRAB", "onResponse: " + response.body().string());
                    Looper.prepare();
                    Toast ero = Toast.makeText(getApplicationContext(), "Connection Succeed!\nResponse Code:" + response.code(),Toast.LENGTH_SHORT);
                    ero.show();
                    Looper.loop();
                }
            });

        }
    }
}