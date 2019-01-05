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

public class AutomatonAsservActivity extends AppCompatActivity {

    private ReadTaskS7 readS7;
    private WriteTaskS7 writeS7;
    private NetworkInfo network;
    private ConnectivityManager statusConnexion;

    private String ip = Configs.getIp();
    private String rack = Integer.toString(Configs.getRack());
    private String slot = Integer.toString(Configs.getSlot());

    private TextView tv_asserv_PLCnumber, tv_asserv_niveauEau, tv_asserv_consigneAuto, tv_asserv_consigneManuel, tv_asserv_motPilotageVanne;
    private CheckBox cb_asserv_valve1, cb_asserv_valve2, cb_asserv_valve3, cb_asserv_valve4;
    private Button bt_asserv_manuel, bt_asserv_auto, bt_asserv_ecrire, bt_asserv_saveDBB2, bt_asserv_saveDBB3, bt_asserv_saveDBW24,
            bt_asserv_saveDBW26, bt_asserv_saveDBW28, bt_asserv_saveDBW30;
    private EditText et_asserv_dbb2, et_asserv_dbb3, et_asserv_dbw24, et_asserv_dbw26, et_asserv_dbw28, et_asserv_dbw30;
    private LinearLayout ll_asserv_layoutEcriture;
    private ImageButton ib_asserv_connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaton_asserv);

        statusConnexion = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = statusConnexion.getActiveNetworkInfo();

        tv_asserv_niveauEau = (TextView)findViewById(R.id.tv_asserv_niveauEau);
        tv_asserv_PLCnumber = (TextView)findViewById(R.id.tv_asserv_PLCnumber);
        tv_asserv_consigneAuto = (TextView)findViewById(R.id.tv_asserv_consAuto);
        tv_asserv_consigneManuel = (TextView)findViewById(R.id.tv_asserv_consManuel);
        tv_asserv_motPilotageVanne = (TextView) findViewById(R.id.tv_asserv_motPilotageVanne);
        cb_asserv_valve1 = (CheckBox)findViewById(R.id.cb_asserv_valve1);
        cb_asserv_valve2 = (CheckBox)findViewById(R.id.cb_asserv_valve2);
        cb_asserv_valve3 = (CheckBox)findViewById(R.id.cb_asserv_valve3);
        cb_asserv_valve4 = (CheckBox) findViewById(R.id.cb_asserv_valve4);
        bt_asserv_manuel= (Button)findViewById(R.id.bt_asserv_manuel);
        bt_asserv_auto = (Button)findViewById(R.id.bt_asserv_auto);
        bt_asserv_ecrire = (Button) findViewById(R.id.bt_asserv_ecrire);
        bt_asserv_saveDBB2 = (Button) findViewById(R.id.bt_asserv_saveDBB2);
        bt_asserv_saveDBB3 = (Button) findViewById(R.id.bt_asserv_saveDBB3);
        bt_asserv_saveDBW24 = (Button) findViewById(R.id.bt_asserv_saveDBW24);
        bt_asserv_saveDBW26 = (Button) findViewById(R.id.bt_asserv_saveDBW26);
        bt_asserv_saveDBW28 = (Button) findViewById(R.id.bt_asserv_saveDBW28);
        bt_asserv_saveDBW30 = (Button) findViewById(R.id.bt_asserv_saveDBW30);
        et_asserv_dbb2 = (EditText)findViewById(R.id.et_asserv_dbb2);
        et_asserv_dbb3 = (EditText)findViewById(R.id.et_asserv_dbb3);
        et_asserv_dbw24 = (EditText)findViewById(R.id.et_asserv_dbw24);
        et_asserv_dbw26 = (EditText)findViewById(R.id.et_asserv_dbw26);
        et_asserv_dbw28 = (EditText)findViewById(R.id.et_asserv_dbw28);
        et_asserv_dbw30 = (EditText)findViewById(R.id.et_asserv_dbw30);
        ll_asserv_layoutEcriture = (LinearLayout) findViewById(R.id.ll_asserv_layoutEcriture);
        ib_asserv_connexion = (ImageButton)findViewById(R.id.ib_asserv_connexion);
    }

    public void onMainClickManager(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_asserv_connexion:
                ColorDrawable ibBackgroundColor = (ColorDrawable) ib_asserv_connexion.getBackground();
                int colorId = ibBackgroundColor.getColor();

                //On vérifie si on est déjà connecté ou non
                if(colorId == getResources().getColor(R.color.red) || colorId == getResources().getColor(R.color.orange))
                {
                    if (network != null && network.isConnectedOrConnecting()) {
                        Toast.makeText(getApplicationContext(), "Connecté en : " + network.getTypeName(), Toast.LENGTH_SHORT).show();

                        try{
                            readS7 = new ReadTaskS7(this,v, tv_asserv_PLCnumber, tv_asserv_niveauEau, tv_asserv_consigneAuto, tv_asserv_consigneManuel,
                                    tv_asserv_motPilotageVanne, cb_asserv_valve1, cb_asserv_valve2, cb_asserv_valve3, cb_asserv_valve4,
                                    bt_asserv_manuel, bt_asserv_auto, bt_asserv_ecrire, ib_asserv_connexion,2);
                            readS7.Start(ip,rack,slot);

                            writeS7 = new WriteTaskS7(2);
                            writeS7.Start(ip,rack,slot);
                        }
                        catch(Exception e){
                            Toast.makeText(getApplicationContext(),"Une erreur s'est produite, veuillez recommencer", Toast.LENGTH_LONG).show();
                            ib_asserv_connexion.setBackgroundColor(getResources().getColor(R.color.orange));
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
                    ib_asserv_connexion.setBackgroundColor(getResources().getColor(R.color.red));
                    bt_asserv_ecrire.setVisibility(View.GONE);
                    readS7.Stop();
                    writeS7.Stop();
                }
             break;

            case R.id.bt_asserv_ecrire:
                //Si le layout n'est pas déjà affiché, on l'affiche, sinon on le cache
                if(ll_asserv_layoutEcriture.getVisibility() == View.GONE)
                {
                    ll_asserv_layoutEcriture.setVisibility(View.VISIBLE);
                }
                else
                {
                    ll_asserv_layoutEcriture.setVisibility(View.GONE);
                }
                break;

            case R.id.bt_asserv_saveDBB2:
                if(!et_asserv_dbb2.getText().toString().isEmpty())
                {
                    writeS7.setWriteBool(2, et_asserv_dbb2.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_asserv_saveDBB3:
                if(!et_asserv_dbb3.getText().toString().isEmpty())
                {
                    writeS7.setWriteBool(3, et_asserv_dbb3.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_asserv_saveDBW24:
                if(!et_asserv_dbw24.getText().toString().isEmpty())
                {
                    writeS7.setWriteInt(24, et_asserv_dbw24.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.bt_asserv_saveDBW26:
                if(!et_asserv_dbw26.getText().toString().isEmpty())
                {
                    writeS7.setWriteInt(26,et_asserv_dbw26.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_asserv_saveDBW28:
                if(!et_asserv_dbw28.getText().toString().isEmpty())
                {
                    writeS7.setWriteInt(28,et_asserv_dbw28.getText().toString());
                    Toast.makeText(getApplicationContext(), "La valeur a bien été écrite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_asserv_saveDBW30:
                if(!et_asserv_dbw30.getText().toString().isEmpty())
                {
                    writeS7.setWriteInt(30,et_asserv_dbw30.getText().toString());
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
