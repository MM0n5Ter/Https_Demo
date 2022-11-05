package demo.https.pinning;

import demo.https.contact.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText requestAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button btnSimple = (Button) findViewById(R.id.simple_https_request);
        Button btnOkhttp = (Button) findViewById(R.id.okhttp_https_request);
        requestAddress = (EditText) findViewById(R.id.RequestAddress);

        btnSimple.setOnClickListener(new btnSimpleClick());
        btnOkhttp.setOnClickListener(new btnOkhttpClick());
    }

    protected String getURL(){
        String newURL = requestAddress.getText().toString().trim();
        if(newURL.length()==0) {
            return "www.baidu.com";}
        else {return newURL;}
    }

    class btnSimpleClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(() -> {
                Looper.prepare();
                int code = new HttpsRequest().CreateSimpleHttps(getURL());
                Toast ero;
                if(code!=-1){
                    ero = Toast.makeText(getApplicationContext(), "Connection Succeed!\nResponse Code:" + code, Toast.LENGTH_SHORT);
                }else{
                    ero = Toast.makeText(getApplicationContext(), "出戳啦\n", Toast.LENGTH_SHORT);
                }
                ero.show();
                Looper.loop();
            }).start();

        }
    }

    class btnOkhttpClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(() -> {
                Looper.prepare();
                int code = new HttpsRequest().CreateOkHttps(getURL());
                Toast ero;
                if(code!=-1){
                    ero = Toast.makeText(getApplicationContext(), "Connection Succeed!\nResponse Code:" + code, Toast.LENGTH_SHORT);
                }else{
                    ero = Toast.makeText(getApplicationContext(), "出戳啦\n", Toast.LENGTH_SHORT);
                }
                ero.show();
                Looper.loop();
            }).start();
        }
    }
}