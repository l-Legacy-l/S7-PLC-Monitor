package com.example.fabio.plcmonitor.Automaton;

import android.util.Log;

import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.SimaticS7.S7;
import com.example.fabio.plcmonitor.SimaticS7.S7Client;

import java.util.concurrent.atomic.AtomicBoolean;

public class WriteTaskS7
{
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread writeThread;
    private AutomateS7 plcS7;
    private S7Client comS7;
    private String[] parConnexion = new String[10];
    private byte[] motCommande = new byte[10];
    private Integer databloc = Configs.getDatablock();
    private byte[] dbb5 = new byte[2], dbb6 = new byte[2];
    private byte[] dbb7 = new byte[2], dbb8 = new byte[2], dbw18 = new byte[2];

    public WriteTaskS7()
    {
        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        writeThread = new Thread(plcS7);
    }

    public void Start(String ip, String rack, String slot){
        if (!writeThread.isAlive()) {
            parConnexion[0] = ip;
            parConnexion[1] = rack;
            parConnexion[2] = slot;

            writeThread.start();
            isRunning.set(true);
        }
    }

    public void Stop(){
        isRunning.set(false);
        comS7.Disconnect();
        writeThread.interrupt();
    }

    public class AutomateS7 implements Runnable{
        @Override
        public void run() {
            try {
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(parConnexion[0], Integer.valueOf(parConnexion[1]), Integer.valueOf(parConnexion[2]));

                while (isRunning.get() && (res.equals(0)))
                {
                    comS7.WriteArea(S7.S7AreaDB, databloc, 5, 2, dbb5);
                    comS7.WriteArea(S7.S7AreaDB, databloc, 6, 2, dbb6);
                    comS7.WriteArea(S7.S7AreaDB, databloc, 7, 2, dbb7);
                    comS7.WriteArea(S7.S7AreaDB, databloc, 8, 2, dbb8);
                    comS7.WriteArea(S7.S7AreaDB, databloc, 18, 2, dbw18);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Méthode pour écrire en "bool" une valeur donnée
    public void setWriteBool(int dbb, String value)
    {
        char[] valueInChars = value.toCharArray();
        int arrayLength = valueInChars.length;
        boolean isTrue;
        byte[] chosenDBB;

        if (dbb == 5)
        {
            chosenDBB = dbb5;
        }
        else if (dbb == 6)
        {
            chosenDBB = dbb6;
        }

        else
        {
            chosenDBB = dbb7;
        }

        for (int i = 0; i < valueInChars.length; i++)
        {
            if(valueInChars[arrayLength -(i+1)] == 1)
            {
                isTrue = true;
            }
            else
            {
                isTrue = false;
            }
            S7.SetBitAt(chosenDBB, 0, i, isTrue);
        }
    }

    //Permet d'écrire dans l'automate une valeur en octet
    public void setWriteByte(String value)
    {
        dbb8[0] = S7.ByteToBCD(Integer.parseInt(value));
    }

    //Ecrit la valeur donnée sous forme d'entier dans l'automate
    public void setWriteInt(String value)
    {
        S7.SetWordAt(dbw18, 0, Integer.parseInt(value));
    }
}
