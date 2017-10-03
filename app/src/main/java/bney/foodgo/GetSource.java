package bney.foodgo;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by bney on 5/4/2017.
 */

public class GetSource extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... params) {
        String html = "";
        try {
            html = Jsoup.connect("http://webapps.studentaffairs.cmu.edu/dining/conceptinfo/?page=listConcepts")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get()
                    .html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public GetSource(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
