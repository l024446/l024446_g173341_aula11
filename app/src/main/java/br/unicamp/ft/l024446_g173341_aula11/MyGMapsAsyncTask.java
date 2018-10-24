package br.unicamp.ft.l024446_g173341_aula11;

import android.annotation.SuppressLint;
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

public class MyGMapsAsyncTask extends AsyncTask<String, Void, String> {

    @SuppressLint("StaticFieldLeak")
    TextView textView;

    public MyGMapsAsyncTask(TextView textView) {
        this.textView = textView;
    }


    @Override
    protected void onPreExecute() {
        textView.append("####################### \n ");
        textView.append("Iniciando Google Maps \n ");
    }

    @Override
    protected String doInBackground(String... args) {
        if (args.length == 0) {
            return "No Parameter";
        }

        HttpURLConnection httpURLConnection;
        try {

            String HOST = "https://maps.googleapis.com/maps/api/geocode/json?" +
                    "sensor=false&address=" +
                    URLEncoder.encode(args[0], "UTF-8")+
                    "&key="+args[1];

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
            JSONObject json2 = jsonObject.getJSONArray("results").getJSONObject(0).
                    getJSONObject("geometry").getJSONObject("location");
            textView.append(json2.getString("lat")+","+json2.getString("lng"));
        } catch(JSONException e){
            textView.append("ERRO: Não foi possível converter em JSONObject: " + args+"\n");
        }
    }
}
