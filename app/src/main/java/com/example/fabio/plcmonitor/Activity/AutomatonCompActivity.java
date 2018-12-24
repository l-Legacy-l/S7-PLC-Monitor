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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabio.plcmonitor.Automaton.ReadTaskS7;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;

public class AutomatonCompActivity extends AppCompatActivity
{
    private ReadTaskS7 readS7;
    private NetworkInfo network;
    private ConnectivityManager statusConnexion;

    private TextView tv_comp_PLCnumber;
    private TextView tv_comp_nbBouteille;
    private CheckBox cb_comp_service;
    private CheckBox cb_comp_flacon;
    private Button bt_comp_5;
    private Button bt_comp_10;
    private Button bt_comp_15;
    private ImageButton ib_comp_connexion;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaton_comp);

        statusConnexion = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = statusConnexion.getActiveNetworkInfo();

        tv_comp_PLCnumber = (TextView)findViewById(R.id.tv_comp_PLCnumber);
        tv_comp_nbBouteille = (TextView)findViewById(R.id.tv_comp_nbBouteille);
        cb_comp_service = (CheckBox)findViewById(R.id.cb_comp_service);
        cb_comp_flacon = (CheckBox)findViewById(R.id.cb_comp_flacon);
        bt_comp_5 = (Button)findViewById(R.id.bt_comp_5);
        bt_comp_10 = (Button)findViewById(R.id.bt_comp_10);
        bt_comp_15 = (Button)findViewById(R.id.bt_comp_15);
        ib_comp_connexion = (ImageButton)findViewById(R.id.ib_comp_connexion);
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
                            readS7 = new ReadTaskS7(v, tv_comp_PLCnumber, tv_comp_nbBouteille, cb_comp_service, cb_comp_flacon, bt_comp_5,
                                    bt_comp_10, bt_comp_15, ib_comp_connexion);
                            readS7.Start(Configs.getIp(), Integer.toString(Configs.getRack()), Integer.toString(Configs.getSlot()));

                            ib_comp_connexion.setBackgroundColor(getResources().getColor(R.color.green));

                        }
                        catch(Exception e){
                            Toast.makeText(getApplicationContext(),"Une erreur s'est produite, veuillez recommencer", Toast.LENGTH_LONG).show();
                            ib_comp_connexion.setBackgroundColor(getResources().getColor(R.color.orange));
                        }
                        /*
                           if(!readS7.isConnected()){
                            Toast.makeText(this,"Connexion impossible\nVérifiez l'automate", Toast.LENGTH_LONG).show();
                            }else{
                            }
                           */

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //writeS7 = new WriteTaskS7();
                        //writeS7.Start(Globals.getIp(), Globals.getRack(), Globals.getSlot());

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "! Connexion réseau IMPOSSIBLE !", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    //On est déjà connecté donc on veut se déconnecter
                    ib_comp_connexion.setBackgroundColor(getResources().getColor(R.color.red));
                }
        }
    }
}
