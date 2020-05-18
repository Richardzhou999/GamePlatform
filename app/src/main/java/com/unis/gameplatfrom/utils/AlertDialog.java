package com.unis.gameplatfrom.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unis.gameplatfrom.R;


/**
 * Created by wulei on 2016/11/23.
 */

public class AlertDialog extends Dialog {

    public AlertDialog(Context context) {
        super(context, R.style.MyDialogStyle);
    }

    public static class Builder {
        TextView tvMessage;
        Button btnCancel;
        Button btnConfirm;

        private Context context;
        private String message;

        private String cancelText;
        private String confirmText;
        private OnClickListener cancelButtonClickListener;
        private OnClickListener confirmButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setCancelButton(String positiveButtonText,
                                       OnClickListener listener) {
            this.cancelText = positiveButtonText;
            this.cancelButtonClickListener = listener;
            return this;
        }

        public Builder setConfirmButton(String negativeButtonText,
                                        OnClickListener listener) {
            this.confirmText = negativeButtonText;
            this.confirmButtonClickListener = listener;
            return this;
        }

        public AlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final AlertDialog dialog = new AlertDialog(context);
            View layout = inflater.inflate(R.layout.alert_dialog, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            dialog.setContentView(layout);

            tvMessage = (TextView) layout.findViewById(R.id.tv_message);
            btnCancel = (Button) layout.findViewById(R.id.btn_dialog_cancel);
            btnConfirm = (Button) layout.findViewById(R.id.btn_dialog_confirm);

            tvMessage.setText(message);

            if (confirmText != null) {
                btnConfirm.setText(confirmText);
                if (confirmButtonClickListener != null) {
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            confirmButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                btnConfirm.setVisibility(
                        View.GONE);
            }

            if (cancelText != null) {
                btnCancel.setText(cancelText);
                if (cancelButtonClickListener != null) {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancelButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                btnCancel.setVisibility(
                        View.GONE);
            }


            return dialog;
        }
    }

}
