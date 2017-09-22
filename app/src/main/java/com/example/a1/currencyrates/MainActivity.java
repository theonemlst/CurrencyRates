package com.example.a1.currencyrates;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {

    TextView helloTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTV = (TextView) findViewById(R.id.hello_tv);
        Button helloBTN = (Button) findViewById(R.id.hello_btn);

        helloBTN.setOnClickListener(onClickListener);
        helloTV.setOnClickListener(onClickListener);

        new LongOperation().execute("");
    }

    private class CheckRate implements Runnable {
        @Override
        public void run() {
            GetJson getJson = new GetJson();
            TextView txt = (TextView) findViewById(R.id.hello_tv);
            while (true) {
                String rate = getJson.getRate();
                try {
                    Thread.sleep(30000);
                } catch (Exception e) {}
                txt.setText(String.format(Locale.ENGLISH, "%(.2f", Double.parseDouble(rate))); 
            }
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            GetJson getJson = new GetJson();
            String rate = getJson.getRate();
            //helloTV.setText(rate);
            return rate;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.hello_tv);
            txt.setText(String.format(Locale.ENGLISH, "%(.2f", Double.parseDouble(result))); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.hello_btn:
                    new LongOperation().execute("");
                    break;
            }
        }
    };
}
