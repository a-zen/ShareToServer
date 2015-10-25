package a_zen.sharetoserver.helper;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import a_zen.sharetoserver.MessagePackage;

/**
 * @author a-zen
 */

public class AsyncHTTPService extends AsyncTask<MessagePackage, Void, Integer> {

    @Override
    protected Integer doInBackground(MessagePackage... packages) {

        int responseCode = 0;

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) packages[0].getUrl().openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeUTF(packages[0].getMessage());
            oos.close();

            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return responseCode;
    }
}
