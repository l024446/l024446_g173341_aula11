package br.unicamp.ft.l024446_g173341_aula11;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Luis on 24/10/2018.
 */

public class MyAccuWeatherAsyncTask extends AsyncTask<String, Void, String>  {

    @SuppressLint("StaticFieldLeak")
    TextView textView;

    public MyAccuWeatherAsyncTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        textView.append("####################### \n ");
        textView.append("Iniciando AccuWeather \n ");
    }

    @Override
    protected String doInBackground(String... args) {
        if (args.length == 0) {
            return "No Parameter";
        }

        HttpURLConnection httpURLConnection;
        try {

            String HOST = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=XTepvZaLtAAtvuImwh6dlUBRMwPEKTRY&q="+args[0]+"&q="+args[1]+"%2C"+args[2];


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
        /* Google Maps */
        try {
            JSONObject jsonObject = new JSONObject(args);

            textView.append(jsonObject.getString("LocalizedName"));
            textView.append("/n");
            textView.append(jsonObject.getJSONObject("Country").getString("LocalizedName"));
            textView.append("/n");
            textView.append(jsonObject.getJSONObject("TimeZone").getString("Name"));
            textView.append("/n");
        } catch(JSONException e){
            textView.append("ERRO: Não foi possível converter em JSONObject: " + args+"\n");
        }
    }
}