package a_zen.sharetoserver;

import java.net.URL;

/**
 * @author a-zen
 */
public class MessagePackage {

    private URL url;
    private String message;

    public MessagePackage(URL url, String message) {
        this.url = url;
        this.message = message;
    }

    public URL getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }
}
