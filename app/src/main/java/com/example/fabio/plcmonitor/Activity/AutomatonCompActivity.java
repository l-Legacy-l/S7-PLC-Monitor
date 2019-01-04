package com.example.fabio.plcmonitor.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabio.plcmonitor.Automaton.ReadTaskS7;
import com.example.fabio.plcmonitor.Automaton.WriteTaskS7;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;

public class AutomatonCompActivity extends AppCompatActivity
{
    private ReadTaskS7 readS7;
    private WriteTaskS7 writeS7;
    private NetworkInfo network;
    private ConnectivityManager statusConnexion;

    private String ip = Configs.getIp();
    private String rack = Integer.toString(Configs.getRack());
    private String slot = Integer.toString(Configs.getSlot());

    private TextView tv_comp_PLCnumber, tv_comp_nbBouteille, tv_comp_nbFlacon;
    private CheckBox cb_comp_service, cb_comp_flacon;
    private Button bt_comp_5, bt_comp_10, bt_comp_15, bt_comp_ecrire;
    private ImageButton ib_comp_connexion;
    private EditText et_comp_dbb5, et_comp_dbb6, et_comp_dbb7, et_comp_dbb8, et_comp_dbw18;
    private LinearLayout ll_comp_layoutEcriture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaton_comp);

        statusConnexion = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = statusConnexion.getActiveNetworkInfo();

        tv_comp_PLCnumber = (TextView)findViewById(R.id.tv_comp_PLCnumber);
        tv_comp_nbBouteille = (TextView)findViewById(R.id.tv_comp_nbBouteille);
        tv_comp_nbFlacon = (TextView)findViewById(R.id.tv_comp_nbFlacon);
        cb_comp_service = (CheckBox)findViewById(R.id.cb_comp_service);
        cb_comp_flacon = (CheckBox)findViewById(R.id.cb_comp_flacon);
        bt_comp_5 = (Button)findViewById(R.id.bt_comp_5);
        bt_comp_10 = (Button)findViewById(R.id.bt_comp_10);
        bt_comp_15 = (Button)findViewById(R.id.bt_comp_15);
        bt_comp_ecrire = (Button) findViewById(R.id.bt_comp_ecrire);
        ib_comp_connexion = (ImageButton)findViewById(R.id.ib_comp_connexion);
        ll_comp_layoutEcriture = (LinearLayout) findViewById(R.id.ll_comp_layoutEcriture);
        et_comp_dbb5 = (EditText) findViewById(R.id.et_comp_dbb5);
        et_comp_dbb6 = (EditText) findViewById(R.id.et_comp_dbb6);
        et_comp_dbb7 = (EditText) findViewById(R.id.et_comp_dbb7);
        et_comp_dbb8 = (EditText) findViewById(R.id.et_comp_dbb8);
        et_comp_dbw18 = (EditText) findViewById(R.id.et_comp_dbw18);
    }

    public void onMainClickManager(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_comp_connexion:
                ColorDrawable ibBackgroundColor = (ColorDrawable) ib_comp_connexion.getBackground();
                int colorId = ibBackgroundColor.getColor();

                //On vérifie si on est déjà connecté ou non
                if(colorId == getResources().getColor(R.color.red) || colorId == getResources().getColor(R.color.orange))
                {
                    if (network != null && network.isConnectedOrConnecting()) {
                        Toast.makeText(getApplicationContext(), "Connecté en : " + network.getTypeName(), Toast.LENGTH_SHORT).show();

                        try{
                            readS7 = new ReadTaskS7(this, v, tv_comp_PLCnumber, tv_comp_nbBouteille, tv_comp_nbFlacon, cb_comp_service, cb_comp_flacon,
                                    bt_comp_5, bt_comp_10, bt_comp_15, ib_comp_connexion,1);
                            readS7.Start(ip,rack,slot);

                            writeS7 = new WriteTaskS7(1);
                            writeS7.Start(ip,rack,slot);

                            if(Configs.getIsWriteAccess())
                            {
                                bt_comp_ecrire.setVisibility(View.VISIBLE);
                            }
                        }
                        catch(Exception e){
                            Toast.makeText(getApplicationContext(),"Une erreur s'est produite, veuillez recommencer", Toast.LENGTH_LONG).show();
                            ib_comp_connexion.setBackgroundColor(getResources().getColor(R.color.orange));
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Impossible d'accéder au réseau, veuillez activer la connexion Wi-Fi", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    //On est déjà connecté donc on veut se déconnecter
                    ib_comp_connexion.setBackgroundColor(getResources().getColor(R.color.red));
                    bt_comp_ecrire.setVisibility(View.GONE);
                    readS7.Stop();
                    writeS7.Stop();
                }

             break;

            case R.id.bt_comp_ecrire:
                //Si le layout n'est pas déjà affiché, on l'affiche, sinon on le cache
                if(ll_comp_layoutEcriture.getVisibility() == View.GONE)
                {
                    ll_comp_layoutEcriture.setVisibility(View.VISIBLE);
                }
                else
                {
                    ll_comp_layoutEcriture.setVisibility(View.GONE);
                    writeS7.Stop();
                }
             break;

            case R.id.bt_comp_saveDBB5:
                if(!et_comp_dbb5.getText().toString().isEmpty())
                {
                    writeS7.setWriteBool(5, et_comp_dbb5.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
             break;

            case R.id.bt_comp_saveDBB6:
                if(!et_comp_dbb6.getText().toString().isEmpty())
                {
                    writeS7.setWriteBool(6, et_comp_dbb6.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
             break;

            case R.id.bt_comp_saveDBB7:
                if(!et_comp_dbb7.getText().toString().isEmpty())
                {
                    writeS7.setWriteBool(7, et_comp_dbb7.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }

             break;

            case R.id.bt_comp_saveDBB8:
                if(!et_comp_dbb8.getText().toString().isEmpty())
                {
                    writeS7.setWriteByte(et_comp_dbb8.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
             break;

            case R.id.bt_comp_saveDBW18:
                if(!et_comp_dbw18.getText().toString().isEmpty())
                {
                    writeS7.setWriteInt(18,et_comp_dbw18.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
             break;
        }
    }
}
