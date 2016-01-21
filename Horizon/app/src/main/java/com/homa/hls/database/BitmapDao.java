package com.homa.hls.database;

import android.graphics.Bitmap;

public class BitmapDao {
    String Path;
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return this.Path;
    }

    public void setPath(String path) {
        this.Path = path;
    }
}
