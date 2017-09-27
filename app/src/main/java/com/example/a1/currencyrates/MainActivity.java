package com.example.a1.currencyrates;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;

    private static final long RATE_UPDATE_DELAY = 30000;

    private TextView helloTV;

    private ToggleButton toggleButton1;
    private ToggleButton toggleButton2;
    private ToggleButton toggleButton3;

    private Button button;

    private String path = "https://api.cryptonator.com/api/ticker/";
    private String currency = "eth";
    private String second = "usd";

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTV = (TextView) findViewById(R.id.hello_tv);

        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);

        button = (Button) findViewById(R.id.Button);

        toggleButton1.setOnCheckedChangeListener(onCheckedChangeListener);
        toggleButton2.setOnCheckedChangeListener(onCheckedChangeListener);
        toggleButton3.setOnCheckedChangeListener(onCheckedChangeListener);

        button.setOnClickListener(onClickListener);

        thread = new Thread(new CheckRate());
        thread.start();
    }

    private URL updateURL(String currency) {
        URL url = null;
        try {
            url = new URL(path + currency + "-" + second);
        } catch (MalformedURLException e) {}
        return url;
    }

    private void updateRate() {
        String json = Connection.getJson(updateURL(currency));
        String rate = JsonParser.getJsonField(json, "ticker", "price");
        //String rate = getJson.getRate();
        //txt.setText(String.format(Locale.ENGLISH, "%(.2f", Double.parseDouble(rate)));
        new LongOperation().execute(rate);
    }

    private CompoundButton.OnClickListener onClickListener = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Получаем вид с файла prompt.xml, который применим для диалогового окна:
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompt, null);

            //Создаем AlertDialog
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

            //Настраиваем prompt.xml для нашего AlertDialog:
            mDialogBuilder.setView(promptsView);

            //Настраиваем отображение поля для ввода текста в открытом диалоге:
            final EditText userInput = (EditText) promptsView.findViewById(R.id.eth_threshold);

            //Настраиваем сообщение в диалоговом окне:
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            null)
                    .setNegativeButton("Отмена",
                            null);

            //Создаем AlertDialog:
            AlertDialog alertDialog = mDialogBuilder.create();

            //и отображаем его:
            alertDialog.show();
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                      case R.id.toggleButton1 :
                          if (isChecked || !toggleButton2.isChecked() && !toggleButton3.isChecked())
                            setToggleButtons(true, false, false);
                            currency = "sc";
                            thread.interrupt();
                          break;
                      case R.id.toggleButton2 :
                          if (isChecked || !toggleButton1.isChecked() && !toggleButton3.isChecked())
                            setToggleButtons(false, true, false);
                            currency = "eth";
                            thread.interrupt();
                          break;
                      case R.id.toggleButton3 :
                          if (isChecked || !toggleButton1.isChecked() && !toggleButton2.isChecked())
                            setToggleButtons(false, false, true);
                            currency = "xmr";
                            thread.interrupt();
                          break;
                      default:
                          break;
            }
            //updateRate();

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
            //GetJson getJson = new GetJson();
            TextView txt = (TextView) findViewById(R.id.hello_tv);
            while (true) {
                updateRate();
                try {
                    Thread.sleep(RATE_UPDATE_DELAY);
                } catch (Exception e) {
                    //Thread.currentThread().interrupt();
                }
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
            txt.setText(String.format(Locale.ENGLISH, getCorrectPattern(result), Double.parseDouble(result)));
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

        private String getCorrectPattern(String target) {
            Double rate = Double.parseDouble(target);
            String result;
            if (rate > 1.0) {
                result = "%(.2f";
            } else {
                String fraction = target.split("\\.")[1];
                int counter = 0;
                while (!fraction.equals("") && fraction.charAt(0) == '0') {
                    counter++;
                    fraction = fraction.substring(1, fraction.length());
                }
                if (fraction.equals(""))
                    result = "%(.2f";
                 else
                    result = "%(." + (counter + 3) + "f";
            }
            return result;
        }
    }
}
