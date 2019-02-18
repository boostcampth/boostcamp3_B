package com.swsnack.catchhouse.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.swsnack.catchhouse.AppApplication;

import java.util.concurrent.ExecutionException;

public class GlideUtil {

    public Bitmap getBitmapFromUri(Uri uri) throws ExecutionException, InterruptedException {
        return new AsyncGetBitmap().execute(uri).get();
    }

    private static class AsyncGetBitmap extends AsyncTask<Uri, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            try {
                return Glide.with(AppApplication.getAppContext())
                        .asBitmap()
                        .load(uris[0])
                        .submit().get();
            } catch (ExecutionException | InterruptedException e) {
                return null;
            }
        }
    }

}
