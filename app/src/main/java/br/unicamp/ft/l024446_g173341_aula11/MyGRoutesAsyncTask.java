package br.unicamp.ft.l024446_g173341_aula11;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ulisses on 9/27/17.
 */

public class MyGRoutesAsyncTask extends AsyncTask<String, Void, String> {

    TextView textView;

    public MyGRoutesAsyncTask(TextView textView) {
        this.textView = textView;
    }


    @Override
    protected void onPreExecute() {

        textView.append("####################### \n ");
        textView.append("Iniciando Rotas no Google Maps \n ");
    }

    @Override
    protected String doInBackground(String... args) {
        if (args.length == 0) {
            return "No Parameter";
        }

        HttpURLConnection httpURLConnection;
        try {

            //https://developers.google.com/maps/documentation/directions/intro
            String HOST = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin="+URLEncoder.encode(args[0], "UTF-8")+"&destination=" +
                   URLEncoder.encode(args[1], "UTF-8")+"&key="+args[2];


        /*
          Abrindo uma conexão com o servidor
        */

            URL url = new URL(HOST);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
        /*
          Lendo a resposta do servidor
        */
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));


            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.v("Erro", e.getMessage());
            return "Exception\n" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String args) {
        textView.append(args);
    }
}
