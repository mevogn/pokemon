package com.maria.pokemon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private MenuItem item;
    private String url = "http://pokeapi.co/api/v2/berry/1/";
    TextView  rspText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rspText = (TextView) findViewById(R.id.showOutput);
        Button getButton = (Button) findViewById(R.id.sendGet);
        getButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = rspText.getText().toString();
                item.setActionView(R.layout.progress);
                SendHttpRequestTask t = new SendHttpRequestTask();

                String[] params = new String[]{url, name};
                t.execute(params);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String name = params[1];

            String data = sendHttpRequest(url, name);
            System.out.println("Data ["+data+"]");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            rspText.setText(result);
            item.setActionView(null);

        }
    }

    private String sendHttpRequest(String url, String name) {
        StringBuffer buffer = new StringBuffer();
        try {
            System.out.println("URL ["+url+"] - Name ["+name+"]");

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int respCode = con.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String jsonLine = bufferedReader.readLine();
            if (jsonLine != null) {
                JSONObject jsonObject = new JSONObject(jsonLine);
                jsonObject.has("id");
            }

//            try {
//                ReadMessagesArray(bufferedReader);
//            } finally {
//                bufferedReader.close();
//            }

            con.disconnect();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return buffer.toString();
    }

    private void ReadMessagesArray(BufferedReader bufferedReader) throws IOException {
       // jsonReader.beginArray();
//        while(bufferedReader.hasNext()) {
//            String name = bufferedReader.nextName();
//            if (name.equals("id")) {
//                System.out.println(bufferedReader.nextLong());
//            } else if (name.equals("name")) {
//                rspText.append(bufferedReader.nextString());
//            }
//        }
    }
}
