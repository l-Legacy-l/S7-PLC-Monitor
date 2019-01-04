package com.example.fabio.plcmonitor.Automaton;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabio.plcmonitor.Activity.AutomatonAsservActivity;
import com.example.fabio.plcmonitor.Activity.AutomatonCompActivity;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;
import com.example.fabio.plcmonitor.SimaticS7.S7;
import com.example.fabio.plcmonitor.SimaticS7.S7Client;
import com.example.fabio.plcmonitor.SimaticS7.S7OrderCode;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReadTaskS7
{
    //constantes pour la gestion du traitement activé par les message Handler
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    //Pour les comprimés
    private TextView tv_comp_plcNumber, tv_comp_nbBouteille, tv_comp_nbFlacon;
    private CheckBox cb_comp_service, cb_comp_flacon;
    private Button bt_comp_5, bt_comp_10, bt_comp_15;
    private ImageButton ib_comp_connexion;

    //Pour l'asservissement de niveau
    private TextView tv_asserv_PLCnumber, tv_asserv_niveauEau, tv_asserv_consigneAuto, tv_asserv_consigneManuel, tv_asserv_motPilotageVanne;
    private CheckBox cb_asserv_valve1, cb_asserv_valve2, cb_asserv_valve3, cb_asserv_valve4;
    private Button bt_asserv_manuel, bt_asserv_auto;
    private ImageButton ib_asserv_connexion;

    private View vi_ui;
    private AutomatonCompActivity automatonCompActivity;
    private AutomatonAsservActivity automatonAsservActivity;

    //Attribut permettant de connaitre quelle automate on contacte
    private int numAutomate;

    private Integer databloc = Configs.getDatablock();

    //Classe interne implémentant Runnable et Thread
    private AutomateS7 plcS7;
    private Thread readThread;

    //Objet S7Client nécessaire pour la connexion avec l'API
    private S7Client comS7;

    //Tableaux contenant les paramètres de connexion et permettant les échanges avec l’automate
    private String[] param = new String[10];

    private byte[] datasPLC = new byte[512], pillsPLC = new byte[2];

    //Variable permettant de déterminer si l'utilisateur est connecté à l'automate
    public boolean isConnected= false;

    //Constructeur ReadTaskS7 pour les comprimés
    public ReadTaskS7(AutomatonCompActivity automatonCompActivity,View vi_ui,TextView tv_comp_plcNumber, TextView tv_comp_nbBouteille, TextView tv_comp_nbFlacon, CheckBox cb_comp_service, CheckBox cb_comp_flacon,
                      Button bt_comp_5, Button bt_comp_10, Button bt_comp_15, ImageButton ib_comp_connexion, int numAutomate)
    {
        //Objets modifiés par la tâche de fond
        this.automatonCompActivity = automatonCompActivity;
        this.vi_ui = vi_ui;
        this.tv_comp_plcNumber = tv_comp_plcNumber;
        this.tv_comp_nbBouteille = tv_comp_nbBouteille;
        this.tv_comp_nbFlacon = tv_comp_nbFlacon;
        this.cb_comp_service = cb_comp_service;
        this.cb_comp_flacon = cb_comp_flacon;
        this.bt_comp_5 = bt_comp_5;
        this.bt_comp_10 = bt_comp_10;
        this.bt_comp_15 = bt_comp_15;
        this.ib_comp_connexion = ib_comp_connexion;
        this.numAutomate = numAutomate;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        //Thread de lecture d'informations
        readThread = new Thread(plcS7);
    }

    //Constructeur pour l'asservissement
    public ReadTaskS7(AutomatonAsservActivity automatonAsservActivity, View vi_ui, TextView tv_asserv_PLCnumber, TextView tv_asserv_niveauEau, TextView tv_asserv_consigneAuto, TextView tv_asserv_consigneManuel,
                      TextView tv_asserv_motPilotageVanne, CheckBox cb_asserv_valve1, CheckBox cb_asserv_valve2, CheckBox cb_asserv_valve3,
                      CheckBox cb_asserv_valve4, Button bt_asserv_manuel, Button bt_asserv_auto, ImageButton ib_asserv_connexion, int numAutomate)
    {
        this.vi_ui = vi_ui;
        this.automatonAsservActivity = automatonAsservActivity;
        this.tv_asserv_PLCnumber = tv_asserv_PLCnumber;
        this.tv_asserv_niveauEau = tv_asserv_niveauEau;
        this.tv_asserv_consigneAuto = tv_asserv_consigneAuto;
        this.tv_asserv_consigneManuel = tv_asserv_consigneManuel;
        this.tv_asserv_motPilotageVanne = tv_asserv_motPilotageVanne;
        this.cb_asserv_valve1 = cb_asserv_valve1;
        this.cb_asserv_valve2 = cb_asserv_valve2;
        this.cb_asserv_valve3 = cb_asserv_valve3;
        this.cb_asserv_valve4 = cb_asserv_valve4;
        this.bt_asserv_manuel = bt_asserv_manuel;
        this.bt_asserv_auto = bt_asserv_auto;
        this.ib_asserv_connexion = ib_asserv_connexion;
        this.numAutomate = numAutomate;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        //Thread de lecture d'informations
        readThread = new Thread(plcS7);
    }

    public void Start(String ip, String rack, String slot) {
        //Vérification si le thread n'est pas en cours
        if(!readThread.isAlive()){
            //Récupère les paramètres de connexion à l'API (IP, rack, slot)
            param[0] = ip;
            param[1] = rack;
            param[2] = slot;

            //Démare le thread et active le flag de gestion
            readThread.start();
            isRunning.set(true);
        }
    }

    public void Stop(){
        //Interruption du traitement (flag)
        isRunning.set(false);
        //Arrêt de communication avec l'API
        comS7.Disconnect();
        //interruption du Thread avant la fin de son traitement
        readThread.interrupt();
    }

    //Méthode exécuté avant le lancement
    private void downloadOnPreExecute(int t) {
        //Affichage du numéro de PLC
        //Si la demande vient de l'activité pour les comprimés
        if(numAutomate == 1)
        {
            tv_comp_plcNumber.setText(String.valueOf(t));
            ib_comp_connexion.setBackgroundColor(automatonCompActivity.getResources().getColor(R.color.green));
        }
        else
        {
            tv_asserv_PLCnumber.setText(String.valueOf(t));
            ib_comp_connexion.setBackgroundColor(automatonAsservActivity.getResources().getColor(R.color.green));
        }
        Toast.makeText(vi_ui.getContext(), "Connexion établie", Toast.LENGTH_LONG).show();
    }

    //Mise à jour durant le traitement
    private void downloadOnProgressUpdate(int nbFlacon) {
        //Automate comprimé
        if(numAutomate == 1)
        {
            //Selecteur en service
            if(S7.GetBitAt(datasPLC,0,0))
            {
                cb_comp_service.setChecked(true);
            }
            else
            {
                cb_comp_service.setChecked(false);
            }

            //Arrivée des flacons vides
            if(S7.GetBitAt(datasPLC,1,3))
            {
                cb_comp_flacon.setChecked(true);
            }
            else
            {
                cb_comp_flacon.setChecked(false);
            }

            //Demande de 5 comprimés
            if(S7.GetBitAt(datasPLC,4,3))
            {
                bt_comp_5.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                bt_comp_10.getBackground().clearColorFilter();
                bt_comp_15.getBackground().clearColorFilter();

            }
            //Demande de 10 comprimés
            if (S7.GetBitAt(datasPLC,4,4))
            {
                bt_comp_10.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                bt_comp_5.getBackground().clearColorFilter();
                bt_comp_15.getBackground().clearColorFilter();
            }
            //Demande de 15 comprimés
            if (S7.GetBitAt(datasPLC,4,5))
            {
                bt_comp_15.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                bt_comp_10.getBackground().clearColorFilter();
                bt_comp_5.getBackground().clearColorFilter();
            }

            //Comprimés mis en flacon
            tv_comp_nbFlacon.setText(String.valueOf(nbFlacon) + "");

            //Bouteille remplies
            tv_comp_nbBouteille.setText(String.valueOf(S7.GetWordAt(datasPLC, 16) +""));

        }
        //Automate asservissement
        else
        {
            //Selecteur de mode manuel/auto
            if(S7.GetBitAt(datasPLC,0,5))
            {
                bt_asserv_auto.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                bt_asserv_manuel.getBackground().clearColorFilter();
            }
            else
            {
                bt_asserv_manuel.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                bt_asserv_auto.getBackground().clearColorFilter();
            }

            //Valve 1
            if(S7.GetBitAt(datasPLC,0,1))
            {
                cb_asserv_valve1.setChecked(true);
            }
            else
            {
                cb_asserv_valve1.setChecked(false);
            }

            //Valve 2
            if(S7.GetBitAt(datasPLC,0,2))
            {
                cb_asserv_valve2.setChecked(true);
            }
            else
            {
                cb_asserv_valve2.setChecked(false);
            }

            //Valve 3
            if(S7.GetBitAt(datasPLC,0,3))
            {
                cb_asserv_valve3.setChecked(true);
            }
            else
            {
                cb_asserv_valve3.setChecked(false);
            }

            //Valve 4
            if(S7.GetBitAt(datasPLC,0,4))
            {
                cb_asserv_valve4.setChecked(true);
            }
            else
            {
                cb_asserv_valve4.setChecked(false);
            }

            //Niveau d'eau
            tv_asserv_niveauEau.setText(S7.GetWordAt(datasPLC, 16) + "");

            //Consigne auto
            tv_asserv_consigneAuto.setText(S7.GetWordAt(datasPLC,18) + "");

            //Consigne manuelle
            tv_asserv_consigneManuel.setText(S7.GetWordAt(datasPLC,20) + "");

            //Mot de pilotage vanne
            tv_asserv_motPilotageVanne.setText(S7.GetWordAt(datasPLC,22) + "");
        }
    }

    //Après le traitement de la tâche de fond
    private void downloadOnPostExecute() {
        Toast.makeText(vi_ui.getContext(), "Vous avez été déconnecté de l'automate", Toast.LENGTH_LONG).show();

        if(numAutomate == 1)
        {
            tv_comp_nbBouteille.setText("");
            tv_comp_nbFlacon.setText("");
            tv_comp_plcNumber.setText("");
            cb_comp_service.setChecked(false);
            cb_comp_flacon.setChecked(false);
            bt_comp_5.getBackground().clearColorFilter();
            bt_comp_10.getBackground().clearColorFilter();
            bt_comp_15.getBackground().clearColorFilter();
        }
        else
        {
            bt_asserv_manuel.getBackground().clearColorFilter();
            bt_asserv_auto.getBackground().clearColorFilter();
            cb_asserv_valve1.setChecked(false);
            cb_asserv_valve2.setChecked(false);
            cb_asserv_valve3.setChecked(false);
            cb_asserv_valve4.setChecked(false);
            tv_asserv_niveauEau.setText("");
            tv_asserv_consigneAuto.setText("");
            tv_asserv_consigneManuel.setText("");
            tv_asserv_motPilotageVanne.setText("");
            tv_asserv_PLCnumber.setText("");
        }
    }

    //Handler -> gestion des différents messages envoyés au thread
    private Handler monHandler = new Handler() {

        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

    //Classe interne pour lire les valeurs de l'automate.
    //Utilise l'handler pour écrire les valeurs lues
    private class AutomateS7 implements Runnable
    {
        //Méthode qui se connecte à l'automate et écrit les valeurs de l'automate
        @Override
        public void run()
        {
            try{
                //Type de connexion
                comS7.SetConnectionType(S7.S7_BASIC);
                //Retourne 0 si la connexion est réussi
                Integer res = comS7.ConnectTo(param[0],Integer.valueOf(param[1]),Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                //Récupère le numéro de référence de la CPU, retourné 0 si c'est réussi
                Integer result = comS7.GetOrderCode(orderCode);
                int  numCPU=-1;
                if(res.equals(0) && result.equals(0)){
                    numCPU = Integer.valueOf(orderCode.Code().toString().substring(5, 8));
                }
                else{
                    numCPU = 0000;
                }

                sendPreExecuteMessage(numCPU);

                //Lecture de la variable API, traitement en boucle
                while(isRunning.get())
                {
                    if(res.equals(0)){
                        int retInfo = comS7.ReadArea(S7.S7AreaDB, databloc,0,32,datasPLC);
                        int pillsRead = comS7.ReadArea(S7.S7AreaDB, databloc,15,2, pillsPLC);
                        int pills;
                        int data = 0;

                        if( retInfo == 0){
                            //Lecture de la donnée
                            data = S7.GetWordAt(datasPLC,0);
                            //On transfert la valeur à l'UI
                            sendProgressMessage(data);
                        }

                        if(pillsRead == 0)
                        {
                            pills = S7.BCDtoByte(pillsPLC[0]);
                            sendProgressMessage(pills);
                        }
                        Log.i("Variable A.P.I. ->",String.valueOf(data));
                    }
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //Traitement terminé
                sendPostExecuteMessage();
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("je passe" + e);

                if(numAutomate == 1)
                {
                    //Connecté en wifi mais mauvaise configuration
                    ib_comp_connexion.setBackgroundColor(automatonCompActivity.getResources().getColor(R.color.orange));
                    //On affiche un toast de cette mannière autrement, erreur
                    automatonCompActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(automatonCompActivity, "Impossible de se connecter à l'automate, vérifiez la configuration", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    ib_asserv_connexion.setBackgroundColor(automatonAsservActivity.getResources().getColor(R.color.orange));
                    //On affiche un toast de cette mannière autrement, erreur
                    automatonAsservActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(automatonAsservActivity, "Impossible de se connecter à l'automate, vérifiez la configuration", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        }

        //Après le traitement principal --> définition du message: connexion stoppé
        private void sendPostExecuteMessage() {
            Message postExecuteMsg = new Message();
            postExecuteMsg.what = MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }

        //Avant le traitement principal --> définition du message: connexion démarré
        //Transfert de la référence de l'API (PLC)
        private void sendPreExecuteMessage(int v) {
            Message preExecuteMsg = new Message();
            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            monHandler.sendMessage(preExecuteMsg);
        }

        //Durant de traitement principal --> définition du message: automate en service
        //Transfert de la valeur dans la variable récupérée dans l’A.P.I.
        private void sendProgressMessage(int i) {
            Message progressMsg = new Message();
            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1 = i;
            monHandler.sendMessage(progressMsg);
        }
    }

}


