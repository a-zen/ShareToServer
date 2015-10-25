package a_zen.sharetoserver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import a_zen.sharetoserver.helper.AsyncHTTPService;

public class SendSharedMessageActivity extends AppCompatActivity {

    private String sharedMessage;

    private MessagePackage messagePackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_shared_message);

        // Resolve the share Intent.
        boolean resolved = resolveIntent(getIntent());
        if (!resolved) {
            finish();
            return;
        }

        send();
    }

    private boolean resolveIntent(Intent intent) {
        if (Intent.ACTION_SEND.equals(intent.getAction()) &&
                "text/plain".equals(intent.getType())) {
            sharedMessage = intent.getStringExtra(Intent.EXTRA_TEXT);
            return true;
        }
        return false;
    }

    private void send() {
        /*
        Toast.makeText(getApplicationContext(), sharedMessage,
                Toast.LENGTH_LONG).show();
        */

        messagePackage = new MessagePackage(buildUrl(), convertMessageToJSON());
        new AsyncHTTPService(this).execute(messagePackage);
        finish();
    }

    private String convertMessageToJSON() {

        StringWriter sWriter = new StringWriter();
        JsonWriter jWriter = new JsonWriter(sWriter);
        try {
            jWriter.beginObject();
            jWriter.name("data").value(sharedMessage);
            jWriter.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sWriter.toString();
    }

    private URL buildUrl() {

        URL url = null;
        StringBuffer urlString = new StringBuffer();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean httpsSwitch = sharedPref.getBoolean("https_switch", true);

        if(httpsSwitch == true) {
           urlString.append(getResources().getString(R.string.https_protocol_string));
        } else {
            urlString.append(getResources().getString(R.string.http_protocol_string));
        }

        urlString.append(sharedPref.getString("host_text", "@string/pref_default_host"));
        urlString.append(":");
        urlString.append(sharedPref.getString("port_text", "@string/pref_default_port"));
        urlString.append(sharedPref.getString("url_path_text", "@string/pref_default_url_path"));

        try {
            url = new URL(urlString.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
