package com.example.gohotel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gohotel.R;

public class DialogUtils {


    public static void showNetworkError(final Activity activity, final DialogCallback callback) {
        if (activity != null && !activity.isFinishing()) {
            final Dialog dialog = new Dialog(activity, R.style.dialog_full_transparent_background);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.no_internet_dialog);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
            }

            TextView btnRetry = dialog.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(v -> {
                dialog.dismiss();
                callback.finished();

            });

        }
    }

}
