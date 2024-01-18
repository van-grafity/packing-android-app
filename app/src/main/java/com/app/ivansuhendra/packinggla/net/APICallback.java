package com.app.ivansuhendra.packinggla.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.app.ivansuhendra.packinggla.MainActivity;
import com.app.ivansuhendra.packinggla.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class APICallback<T> implements Callback<T> {
    private final Context context;

    public APICallback(Context context) {
        this.context = context;
    }

    @Deprecated
    @Override
    public void onResponse(final Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());

        } else if (response.code() == 204 || response.code() == 205) {
            // HTTP 204 No Content "...response MUST NOT include a message-body"
            // HTTP 205 Reset Content "...response MUST NOT include an entity"
            onSuccess(response.body());

        } else if (response.code() == 401) { //UNAUTHORIZED
            refreshToken(call);

        } else {
            try {
                if (response.errorBody() != null) {
                    BadRequest error = API.getErrorConverter().convert(response.errorBody());
                    error.setCode(response.code());
                    onError(error);

                    if (error.code == 500) {
                        if (error.errorDetails.matches("The token has been blacklisted") ||
                                error.errorDetails.matches("Token Signature could not be verified.") ||
                                error.errorDetails.matches("Failed to create session. No user binded.") ||
                                error.errorDetails.matches("Token has expired and can no longer be refreshed")) {
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("ERROR", "exception " + e.getMessage());
                onError(new BadRequest(context.getString(R.string.server_maintenance)));
            }
        }
    }

    @Deprecated
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t != null) {
            Log.e("onFailure", t.getMessage() + " ||| " + t.toString());
        }
        if (!isNetworkAvailable()) {
            onError(new BadRequest(context.getString(R.string.timeout_message)));
        } else if (t != null) {
            if (t.getMessage() != null) {
                Log.e("API FAILURE", "getMessage() " + t.getMessage());
                if (t.getMessage().matches("timeout")) {
                    onError(new BadRequest(context.getString(R.string.timeout_message)));
                } else {
                    onError(new BadRequest(context.getString(R.string.server_maintenance)));
                }
            } else {
                onError(new BadRequest(context.getString(R.string.server_maintenance)));
            }
        } else {
            onError(new BadRequest(context.getString(R.string.server_maintenance)));
        }
    }


    protected abstract void onSuccess(T t);

    protected abstract void onError(BadRequest error);

    private void refreshToken(final Call<T> call) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (Build.VERSION.SDK_INT >= 24) {
            builder.setMessage(Html.fromHtml(context.getString(R.string.session_over), Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(Html.fromHtml(context.getString(R.string.session_over)));
        }
        builder.setPositiveButton("Ok", null);

        try {
            Log.e("Force Logout", "context" + context);
//                context.stopService(MainActivity.chatSocket);
//                context.stopService(MainActivity.splitBillSocket);

//            App.isAuthorized = false;

            AlertDialog dialog = builder.show();
            TextView messageText = dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            messageText.setText(R.string.session_over);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(final DialogInterface dialog) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
            dialog.show();
        } catch (Exception exception) {
            Log.e("ERROR", "LOGOUT : " + exception);
        }
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception exception) {
            Log.e("isNetworkAvailable", "" + exception);
            return true;
        }
    }
}