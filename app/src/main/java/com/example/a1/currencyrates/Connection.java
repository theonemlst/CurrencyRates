package com.example.a1.currencyrates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 1 on 23.09.2017.
 */

public class Connection {

    public static String getJson(URL url) {
        BufferedReader stream = null;
        String line;
        String result = "";
        try {
            InputStream is = url.openStream();
            stream = new BufferedReader(new InputStreamReader(is));
            while ((line = stream.readLine()) != null) {
                result += line;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException ioe) {}
        }
        return result;
    }
}
