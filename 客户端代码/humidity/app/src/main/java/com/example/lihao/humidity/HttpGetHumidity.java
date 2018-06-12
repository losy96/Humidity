package com.example.lihao.humidity;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpGetHumidity {
    public String main(String urlGet){
        try {
            URL url = new URL(urlGet);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(3000);
            connection.connect();
            int code = connection.getResponseCode();
            String msg = "";
            if (code == 200){
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null){
                    msg += line + "\n";
                }
                reader.close();
            }
            connection.disconnect();
            return msg;


        } catch (ProtocolException e) {
            return e.toString();
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            return e.toString();
            //e.printStackTrace();
        } catch (IOException e) {
            return e.toString();
            //e.printStackTrace();
        }
    //return "error";
    }

}
