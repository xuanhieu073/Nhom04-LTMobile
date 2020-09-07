package com.example.gohotel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.gohotel.R;

public class DialogLoadingProgress {
    private static DialogLoadingProgress Instance = null;
    private Dialog dialog;
    private Context context;

    public static DialogLoadingProgress getInstance() {
        if (Instance == null) {
            Instance = new DialogLoadingProgress();
        }
        return Instance;
    }

    public void show(final Context context) {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            if (dialog == null) {
                this.context = context;
                dialog = new Dialog(context, R.style.dialog_full_transparent_background);
                dialog.setOwnerActivity((Activity) context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_dialog_loading_progress);
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.show();
                }
            }
        }
    }

    public void hide() {
        try {
            if (dialog != null && !((Activity) context).isFinishing()) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        } catch (Exception ignored) {

        }

    }

}
