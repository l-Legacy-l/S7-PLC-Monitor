package com.example.fabio.plcmonitor.Automaton;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.SimaticS7.S7Client;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReadTaskS7
{
    //constantes pour la gestion du traitement activé par les message Handler
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private TextView tv_comp_plcNumber;
    private TextView tv_comp_nbBouteille;
    private CheckBox cb_comp_service;
    private CheckBox cb_comp_flacon;
    private Button bt_comp_5;
    private Button bt_comp_10;
    private Button bt_comp_15;
    private ImageButton ib_comp_connexion;
    private View vi_comp_ui;

    private Integer databloc = Configs.getDatablock();

    //Classe interne implémentant Runnable et Thread
    private AutomateS7 plcS7;
    private Thread readThread;

    //Objet S7Client nécessaire pour la connexion avec l'API
    private S7Client comS7;

    //Tableaux contenant les paramètres de connexion et permettant les échanges avec l’automate
    private String[] param = new String[10];

    private byte[] datasPLC = new byte[512];

    //Constructeur ReadTaskS7 pour les comprimés

    public ReadTaskS7(View vi_comp_ui,TextView tv_comp_plcNumber, TextView tv_comp_nbBouteille, CheckBox cb_comp_service, CheckBox cb_comp_flacon,
                      Button bt_comp_5, Button bt_comp_10, Button bt_comp_15, ImageButton ib_comp_connexion)
    {
        this.vi_comp_ui = vi_comp_ui;
        this.tv_comp_plcNumber = tv_comp_plcNumber;
        this.tv_comp_nbBouteille = tv_comp_nbBouteille;
        this.cb_comp_service = cb_comp_service;
        this.cb_comp_flacon = cb_comp_service;
        this.bt_comp_5 = bt_comp_5;
        this.bt_comp_10 = bt_comp_10;
        this.bt_comp_15 = bt_comp_15;
        this.ib_comp_connexion = ib_comp_connexion;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();

        readThread = new Thread(plcS7);
    }

    private class AutomateS7 implements Runnable
    {

        @Override
        public void run() {

        }
    }

}


