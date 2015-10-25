package a_zen.sharetoserver.helper;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import a_zen.sharetoserver.MainActivity;
import a_zen.sharetoserver.MessagePackage;
import a_zen.sharetoserver.R;
import a_zen.sharetoserver.SendSharedMessageActivity;

/**
 * @author a-zen
 */

public class AsyncHTTPService extends AsyncTask<MessagePackage, Void, Integer> {

    private Context context;

    public AsyncHTTPService(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(MessagePackage... packages) {

        Integer responseCode = 0;

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

    @Override
    protected void onPostExecute(Integer responseCode) {

        // Error
        if(responseCode < 200 || responseCode > 300) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.api_call_error, responseCode),
                    Toast.LENGTH_LONG).show();
        }
    }

}
