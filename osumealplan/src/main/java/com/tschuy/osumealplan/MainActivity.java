package com.tschuy.osumealplan;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    String ltValue = "";
    String executionValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView)findViewById(R.id.mainview);

        // NO BAD DO NOT DO THIS
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        downloadPage("https://login.oregonstate.edu/cas/login?service=https%3A%2F%2Fuhds.oregonstate.edu%2Fmyuhds%2F");

        t.append(postData()); // at this point the lt and execution values are stored

        CookieStore cookieJar = cookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie : cookies) {
            System.out.println(cookie);
        }

    }

    public String postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://login.oregonstate.edu/cas/login?service=https%3A%2F%2Fuhds.oregonstate.edu%2Fmyuhds%2F");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", ""));
            nameValuePairs.add(new BasicNameValuePair("password", ""));
            nameValuePairs.add(new BasicNameValuePair("lt", ltValue));
            nameValuePairs.add(new BasicNameValuePair("execution", executionValue));
            nameValuePairs.add(new BasicNameValuePair("_eventId", "submit"));
            nameValuePairs.add(new BasicNameValuePair("submit", "LOGIN"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            try {
                String responseString = EntityUtils.toString(entity, "UTF-8");
                return responseString;
            } catch (Exception e) {}
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return "";
    }

    private void downloadPage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            readStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        is.close();

        return sb.toString();
    }

    private void readStream(InputStream in) {
        Document doc;
        Element body;
        TextView t = (TextView)findViewById(R.id.mainview);

        try {
            String page = convertStreamToString(in);

            doc = Jsoup.parseBodyFragment(page);

            body = doc.select("input[name=lt]").first();
            ltValue = body.attr("value");
            t.append(ltValue);

            body = doc.select("input[name=execution]").first();
            executionValue = body.attr("value");
            t.append(executionValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
