package a_zen.sharetoserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SendSharedMessageActivity extends AppCompatActivity {

    private String sharedMessage;

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
        Toast.makeText(getApplicationContext(), sharedMessage,
                Toast.LENGTH_LONG).show();
        finish();
    }
}
