package a_zen.sharetoserver.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import a_zen.sharetoserver.MessagePackage;
import a_zen.sharetoserver.R;

/**
 * @author a-zen
 */

public class AsyncHTTPService extends AsyncTask<MessagePackage, Void, Integer> {

    private static final String contentType = "Content-Type";
    private static final String jsonType = "application/json; charset=utf-8";

    private final Context context;

    public AsyncHTTPService(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(MessagePackage... packages) {

        Integer responseCode = 0;
        final byte[] messageAsBytes = packages[0].getMessage().getBytes();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) packages[0].getUrl().openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(messageAsBytes.length);
            urlConnection.setRequestProperty(contentType, jsonType);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(messageAsBytes);
            out.close();

            responseCode = urlConnection.getResponseCode();
        } catch (ConnectException e) {
            responseCode = -1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer responseCode) {

        // Error handling
        if(responseCode == -1) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.connection_failed),
                    Toast.LENGTH_LONG).show();
        } else if(responseCode < 200 || responseCode > 300) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.api_call_error, responseCode),
                    Toast.LENGTH_LONG).show();
        }
    }

}
