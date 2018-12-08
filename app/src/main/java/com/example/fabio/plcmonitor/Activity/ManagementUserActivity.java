package com.example.fabio.plcmonitor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserAccessDB;
import com.example.fabio.plcmonitor.R;

import java.util.ArrayList;

public class ManagementUserActivity extends AppCompatActivity {

    UserAccessDB db;

    Spinner spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_user);
        fillingSpin();
    }

    private void fillingSpin()
    {
        spin = (Spinner) findViewById(R.id.spi_muser_select);

        db = new UserAccessDB(this);
        db.openForRead();
        ArrayList<User> users = db.getAllUser();
        db.Close();
        //Liste contenant les emails de chaque personnes inscrites
        ArrayList<String> mailUsers = new ArrayList<String>();

        for(int i = 0 ; i < users.size();i++)
        {
            mailUsers.add(users.get(i).getEmail());
        }

        //On configure l'adaptateur pour l'ajouter au spinner
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mailUsers);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapters);
    }
}
