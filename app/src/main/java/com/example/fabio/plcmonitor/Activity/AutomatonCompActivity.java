package com.example.fabio.plcmonitor.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fabio.plcmonitor.Automaton.ReadTaskS7;
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
}
