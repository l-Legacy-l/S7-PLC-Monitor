package com.example.fabio.plcmonitor.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.fabio.plcmonitor.R;

public class ManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
    }

    public void btnParamClick(View v)
    {

    }

    public void btnCompClick(View v)
    {

    }

    public void btnRegulClick(View v)
    {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ManagementActivity.this);
            builder.setMessage("Voulez vous vous déconnecter ?");
            builder.setCancelable(true);
            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return false;
    }
}
