package com.example.fabio.plcmonitor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;

public class SettingsActivity extends AppCompatActivity {

    EditText etIp;
    EditText etRack;
    EditText etSlot;
    EditText etDatablock;

    String ip;
    int rack;
    int slot;
    int datablock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etIp = (EditText)findViewById(R.id.et_settings_ip);
        etRack = (EditText)findViewById(R.id.et_settings_rack);
        etSlot = (EditText)findViewById(R.id.et_settings_slot);
        etDatablock = (EditText)findViewById(R.id.et_settings_datablock);

        //On remplis par défaut la configuration déjà enregistré au préalable dans les champs
        etIp.setText(Configs.getIp());
        etRack.setText(String.valueOf(Configs.getRack()));
        etSlot.setText(String.valueOf(Configs.getSlot()));
        etDatablock.setText(String.valueOf(Configs.getDatablock()));
    }

    public void btnValiderClick(View v)
    {
        if(!(etIp.getText().toString().isEmpty()) && !(etRack.getText().toString().isEmpty()) && !(etSlot.getText().toString().isEmpty())
                && !(etDatablock.getText().toString().isEmpty()))
        {
            ip = etIp.getText().toString();
            rack = Integer.parseInt(etRack.getText().toString());
            slot = Integer.parseInt(etSlot.getText().toString());
            datablock = Integer.parseInt(etDatablock.getText().toString());
            //Enregistrement dans la classe statique (plus pratique dans ce cas pour ne pas avoir plusieurs instances)
            Configs.setIp(ip);
            Configs.setRack(rack);
            Configs.setSlot(slot);
            Configs.setDatablock(datablock);

            Toast.makeText(getApplicationContext(), "Enregistrement de la configuration effectué",Toast.LENGTH_SHORT).show();

        }

        else
        {
            Toast.makeText(getApplicationContext(), "Vous devez remplir tous les champs",Toast.LENGTH_SHORT).show();
        }
    }
}
