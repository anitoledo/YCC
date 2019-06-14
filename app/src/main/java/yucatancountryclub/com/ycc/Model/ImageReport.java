package yucatancountryclub.com.ycc.Model;

import android.net.Uri;

/**
 * Created by Zazu on 04/12/18.
 */

public class ImageReport {
    private Uri uri;
    private String path;

    public ImageReport(Uri uri, String path){
        this.uri = uri;
        this.path = path;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}