package com.example.gohotel.Asyntask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.gohotel.activity.SplashScreen;
import com.example.gohotel.utils.Utils;

public class CheckInternetAsync extends AsyncTask<Void, Void, Void> {
    private Context context;
    private int result;
    private SplashScreen.OnTaskCompleted listener;

    public CheckInternetAsync(Context context, SplashScreen.OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //check open wifi
        if (Utils.isOpenWifi(context)) {
            //check ping google
            if (Utils.isInternetWork()) {
                // Internet work successfully
                result = 1;
            } else {
                // Connected to wifi but internet not working
                result = 2;
            }
        } else {
            // Not open wifi
            result = 3;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onCheckingInternetComplete(result);
    }
}
