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

public class AutomatonAsservActivity extends AppCompatActivity {

    private ReadTaskS7 readS7;
    private NetworkInfo network;
    private ConnectivityManager statusConnexion;

    private TextView tv_asserv_PLCnumber;
    private TextView tv_asserv_niveauEau;
    private CheckBox cb_asserv_valve1;
    private CheckBox cb_asserv_valve2;
    private CheckBox cb_asserv_valve3;
    private CheckBox cb_asserv_valve4;
    private Button bt_asserv_manuel;
    private Button bt_asserv_auto;
    private ImageButton ib_asserv_connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaton_asserv);

        statusConnexion = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = statusConnexion.getActiveNetworkInfo();

        tv_asserv_niveauEau = (TextView)findViewById(R.id.tv_asserv_niveauEau);
        tv_asserv_PLCnumber = (TextView)findViewById(R.id.tv_asserv_PLCnumber);
        cb_asserv_valve1 = (CheckBox)findViewById(R.id.cb_asserv_valve1);
        cb_asserv_valve2 = (CheckBox)findViewById(R.id.cb_asserv_valve2);
        cb_asserv_valve3 = (CheckBox)findViewById(R.id.cb_asserv_valve3);
        cb_asserv_valve4 = (CheckBox) findViewById(R.id.cb_asserv_valve4);
        bt_asserv_manuel= (Button)findViewById(R.id.bt_asserv_manuel);
        bt_asserv_auto = (Button)findViewById(R.id.bt_asserv_auto);
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
                            readS7 = new ReadTaskS7(v, tv_asserv_PLCnumber, tv_asserv_niveauEau, cb_asserv_valve1, cb_asserv_valve2, cb_asserv_valve3,
                                    cb_asserv_valve4, bt_asserv_manuel, bt_asserv_auto, ib_asserv_connexion,2);
                            readS7.Start(Configs.getIp(), Integer.toString(Configs.getRack()), Integer.toString(Configs.getSlot()));

                            ib_asserv_connexion.setBackgroundColor(getResources().getColor(R.color.green));

                        }
                        catch(Exception e){
                            Toast.makeText(getApplicationContext(),"Une erreur s'est produite, veuillez recommencer", Toast.LENGTH_LONG).show();
                            ib_asserv_connexion.setBackgroundColor(getResources().getColor(R.color.orange));
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
                    ib_asserv_connexion.setBackgroundColor(getResources().getColor(R.color.red));
                    readS7.Stop();
                }
        }
    }
}
