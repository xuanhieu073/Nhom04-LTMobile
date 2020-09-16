package com.example.gohotel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.example.gohotel.R;

public class TextOnDrawable extends AsyncTask<Void, Void, Bitmap> {
    private Context context;
    private int resource;
    private String price;
    private LoadTaskComplete loadTaskComplete;

    public TextOnDrawable(Context context, int resource, String price, LoadTaskComplete loadTaskComplete) {
        this.context = context.getApplicationContext();
        this.resource = resource;
        this.price = price;
        this.loadTaskComplete = loadTaskComplete;
    }


    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource).copy(Bitmap.Config.ARGB_8888, true);
        if (price != null && !price.equals("-1")) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(context.getResources().getDimension(R.dimen.text_maker));
            Canvas canvas = new Canvas(bitmap);

            canvas.drawText(price, bitmap.getWidth() / 2, bitmap.getHeight() / 1.6f, paint);
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        loadTaskComplete.onTaskComplete(bitmap);
    }

    public interface LoadTaskComplete {
        void onTaskComplete(Bitmap bitmap);
    }
}
