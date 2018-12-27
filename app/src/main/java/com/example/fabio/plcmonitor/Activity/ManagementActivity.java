package com.example.fabio.plcmonitor.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.fabio.plcmonitor.R;

public class ManagementActivity extends AppCompatActivity {

    MenuItem gestionItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu.xml à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        gestionItem = menu.findItem(R.id.action_gestion);
        Boolean isAdmin =  Boolean.parseBoolean(this.getIntent().getStringExtra("isAdmin"));

        if(!isAdmin)
        {
            gestionItem.setVisible(false);
        }
        return true;
    }


    public void btnClick(View v)
    {
        if(v.getId() == R.id.bt_management_comprime)
        {
            Intent intent = new Intent(getApplicationContext(), AutomatonCompActivity.class);
            startActivity(intent);
        }

        else
        {
            Intent intent = new Intent(getApplicationContext(), AutomatonAsservActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gestion) {
            Intent intent = new Intent(getApplicationContext(), ManagementUserActivity.class);
            startActivity(intent);
        }

        else
        {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
