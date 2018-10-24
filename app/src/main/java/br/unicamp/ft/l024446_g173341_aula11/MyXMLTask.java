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

public class MyXMLTask extends AsyncTask<String, Void, String> {

    TextView textView;
    String data_hora_selecionada = "03-05-2018 00h Z";

    public MyXMLTask(TextView textView) {
        this.textView = textView;
    }


    @Override
    protected void onPreExecute() {
        textView.append("Iniciado \n ");
    }

    @Override
    protected String doInBackground(String... args) {
        HttpURLConnection httpURLConnection;
        try {


            // XML
            String HOST = "http://servicos.cptec.inpe.br/XML/cidade/241/todos/tempos/ondas.xml";

            Log.v("HOST", HOST);
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

        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = xmlFactoryObject.newPullParser();
            xpp.setInput(new StringReader(args));
            String tagAtual = "";


            int eventType = xpp.getEventType();
            textView.append("");
            String dia_evento = "";

            while (eventType!=XmlPullParser.END_DOCUMENT) {


                if (eventType==XmlPullParser.START_TAG) {
                    tagAtual = xpp.getName();
                    if (tagAtual.equals("cidade")) {
                        textView.append("Iniciando Cidade: \n");

                    } /*else if (tagAtual.equals("previsao")) {
                        textView.append("Iniciando Previsão \n");
                    }*/
                } else if (eventType == XmlPullParser.TEXT && !xpp.isWhitespace()){
                    if (tagAtual.equals("nome")){
                        textView.append("   Nome da Cidade:"+ xpp.getText() + "\n");
                    } else if (tagAtual.equals("dia")){
                        dia_evento = xpp.getText();
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Dia da Previsão:" + xpp.getText() + "\n");
                        }
                    } else if (tagAtual.equals("agitacao")){
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Agitação:" + xpp.getText() + "\n");
                        }
                    } else if (tagAtual.equals("altura")){
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Altura:" + xpp.getText() + "\n");
                        }
                    } else if (tagAtual.equals("direcao")){
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Direção:" + xpp.getText() + "\n");
                        }
                    } else if (tagAtual.equals("vento")){
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Vento:" + xpp.getText() + "\n");
                        }
                    } else if (tagAtual.equals("vento")) {
                        if (dia_evento.equals(this.data_hora_selecionada)) {
                            textView.append("   Direção do Vento:" + xpp.getText() + "\n");
                        }
                    }
                }
                eventType = xpp.next();
            }


        } catch(XmlPullParserException e){

        } catch(IOException e){

        }



    }
}
