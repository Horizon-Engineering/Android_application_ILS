package com.homa.hls.widgetcustom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hesolutions.horizon.R;

public class CustomProgressDialog extends Dialog {
    private static CustomProgressDialog customProgressDialog;
    private Context context;

    static {
        customProgressDialog = null;
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = null;
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = null;
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.customprogressdialog);
        customProgressDialog.setCancelable(true);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.getWindow().getAttributes().gravity = 17;
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog != null) {
            ((AnimationDrawable) ((ImageView) customProgressDialog.findViewById(R.id.loadingImageView)).getBackground()).start();
        }
    }

    public CustomProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }
}
