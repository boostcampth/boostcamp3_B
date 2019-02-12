package com.swsnack.catchhouse.data.asynctask;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.swsnack.catchhouse.util.DataConverter;

import java.util.ArrayList;
import java.util.List;

public class ConvertImageTask extends AsyncTask<Uri, Void, List<byte[]>> {

    private OnSuccessListener<List<byte[]>> successListener;
    private OnFailureListener failureListener;
    private Application mAppContext;

    public ConvertImageTask(Application context,
                            OnSuccessListener<List<byte[]>> onSuccess,
                            OnFailureListener onFailure) {
        mAppContext = context;
        successListener = onSuccess;
        failureListener = onFailure;
    }

    @Override
    protected List<byte[]> doInBackground(Uri... uris) {
        List<byte[]> result = new ArrayList<>();

        try {
            for(Uri uri : uris) {
                Bitmap bitmap = Glide
                        .with(mAppContext)
                        .asBitmap()
                        .load(uri)
                        .submit()
                        .get();
                byte[] bytes = DataConverter.getByteArray(DataConverter.getScaledBitmap(bitmap));
                result.add(bytes);
            }
        } catch (Exception e) {
            failureListener.onFailure(e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<byte[]> result) {
        super.onPostExecute(result);
        successListener.onSuccess(result);
    }
}
