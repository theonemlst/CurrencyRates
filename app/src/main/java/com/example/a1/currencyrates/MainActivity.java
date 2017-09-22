package com.example.a1.currencyrates;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView helloTV;

    private ToggleButton toggleButton1;
    private ToggleButton toggleButton2;
    private ToggleButton toggleButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTV = (TextView) findViewById(R.id.hello_tv);

        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
//        int width = getResources().getDisplayMetrics().widthPixels/3;
//        toggleButton1.setLayoutParams(new RelativeLayout.LayoutParams(width,toggleButton1.getHeight()));
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);

        toggleButton1.setOnCheckedChangeListener(onCheckedChangeListener);
        toggleButton2.setOnCheckedChangeListener(onCheckedChangeListener);
        toggleButton3.setOnCheckedChangeListener(onCheckedChangeListener);

        Thread thread = new Thread(new CheckRate());
        thread.start();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                      case R.id.toggleButton1 :
                          if (isChecked || !toggleButton2.isChecked() && !toggleButton3.isChecked())
                            setToggleButtons(true, false, false);
                          break;
                      case R.id.toggleButton2 :
                          if (isChecked || !toggleButton1.isChecked() && !toggleButton3.isChecked())
                            setToggleButtons(false, true, false);
                          break;
                      case R.id.toggleButton3 :
                          if (isChecked || !toggleButton1.isChecked() && !toggleButton2.isChecked())
                            setToggleButtons(false, false, true);
                          break;
                      default:
                          break;
            }
        }

        private void setToggleButtons(boolean tb1, boolean tb2, boolean tb3) {
            toggleButton1.setChecked(tb1);
            toggleButton2.setChecked(tb2);
            toggleButton3.setChecked(tb3);
        }
    };

    private class CheckRate implements Runnable {
        @Override
        public void run() {
            GetJson getJson = new GetJson();
            TextView txt = (TextView) findViewById(R.id.hello_tv);
            while (true) {
                String rate = getJson.getRate();
                //txt.setText(String.format(Locale.ENGLISH, "%(.2f", Double.parseDouble(rate)));
                new LongOperation().execute(rate);
                try {
                    Thread.sleep(30000);
                } catch (Exception e) {}
            }
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.hello_tv);
            txt.setText(String.format(Locale.ENGLISH, "%(.2f", Double.parseDouble(result))); // txt.setText(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
