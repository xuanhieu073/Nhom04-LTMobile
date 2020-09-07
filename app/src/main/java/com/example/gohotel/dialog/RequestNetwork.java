package com.example.gohotel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.utils.Utils;

public class RequestNetwork {
    private static RequestNetwork Instance = null;
    private static boolean isShowDialog;

    public static RequestNetwork getInstance() {
        if (Instance == null) {
            Instance = new RequestNetwork();
        }
        return Instance;
    }

    public static void show(final Context context) {
        if (!isShowDialog) {
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                final Dialog dialog = new Dialog(context, R.style.dialog_full_transparent_background);
                dialog.setOnDismissListener(dialog1 -> isShowDialog = false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.no_internet_dialog);
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.show();
                }
                TextView btnOK = dialog.findViewById(R.id.btnRetry);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (Utils.isOpenWifi(context)) {
                            dialog.dismiss();
                        } else {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    }
                });


            }
        }
    }
}
