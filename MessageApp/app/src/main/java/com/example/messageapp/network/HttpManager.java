package com.example.messageapp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class HttpManager implements Callable<String> {
    private URL url;
    private HttpURLConnection conn;

    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader bufferedReader;

    private final String urlAddress;

    public HttpManager(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    @Override
    public String call() {
        try {
            return preluareStringHttp();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return null;
    }

    private String preluareStringHttp() throws IOException {
        url = new URL(urlAddress);
        conn = (HttpURLConnection) url.openConnection();
        is = conn.getInputStream();
        isr = new InputStreamReader(is);
        bufferedReader = new BufferedReader(isr);
        String linie;
        String buffer="";
        while ((linie = bufferedReader.readLine()) != null) {
            buffer+=linie;
        }
        return buffer;
    }

    private void closeConnections() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();
    }
}
