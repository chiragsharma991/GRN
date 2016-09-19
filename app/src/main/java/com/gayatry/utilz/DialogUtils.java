package com.gayatry.utilz;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gayatry.R;


/**
 * Created by Android-132 on 21-Dec-15.
 */
public class DialogUtils {

    private Context context;

    public DialogUtils(Context context){
        this.context=context;
    }

    public Dialog setupCustomeDialogFromBottom(int layout) {
        Dialog dialog = new Dialog(context, R.style.ThemeDialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeDialog;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    public Dialog showDialogForValidation(int layout) {
        Dialog dialog = new Dialog(context, R.style.DialogAnimation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public Dialog setupCustomeDialogFromCenter(int layout) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeDialog;
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

   /* public void showValidateDialog(String title, String msg, String approve, String disApprove){
        final Dialog validateDialog = showDialogForValidation(R.layout.layout_dailog);
        TextView txtTitle = (TextView) validateDialog.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) validateDialog.findViewById(R.id.txtMessage);
        TextView btnApprove = (TextView) validateDialog.findViewById(R.id.btnApprove);
        TextView btnDisApprove = (TextView) validateDialog.findViewById(R.id.btnDisApprove);
        if(title != "")
            txtTitle.setText(title);
        if(msg != "")
            txtMessage.setText(msg);
        if(approve != "")
            btnApprove.setText(approve);
        if(disApprove != "") {
            btnDisApprove.setText(disApprove);
            btnDisApprove.setVisibility(View.VISIBLE);
        }else
            btnDisApprove.setVisibility(View.GONE);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateDialog != null)
                    validateDialog.dismiss();
            }
        });
        btnDisApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateDialog != null)
                    validateDialog.dismiss();
            }
        });
        validateDialog.show();
    }*/
}
