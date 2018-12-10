package com.example.fabio.plcmonitor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserAccessDB;
import com.example.fabio.plcmonitor.R;

import java.util.ArrayList;

public class ManagementUserActivity extends AppCompatActivity {

    UserAccessDB db;

    Spinner spin;
    Switch switchAdmin;

    EditText etMdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_user);
        fillingSpin();

        switchAdmin = (Switch) findViewById(R.id.sw_muser_admin);

        db = new UserAccessDB(this);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                db.openForRead();
                //On récupère l'user correspondant au mail qu'on a sélectionné
                User user1 = db.getUser(spin.getSelectedItem().toString());
                db.Close();
                //On met en ON le switch si l'utilisateur est un administrateur
                if (user1.getIsAdmin())
                {
                    switchAdmin.setChecked(true);
                }

                else
                {
                    switchAdmin.setChecked(false);
                }

                // On remplis automatiquement le mot de passe de l'utilisateur dans le champ mdp
                etMdp = ((EditText)findViewById(R.id.et_muser_mdp));
                etMdp.setText(user1.getMdp().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                System.out.println("item nothing");
            }
        });
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

    public void muserClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sw_muser_admin:
                db.openForRead();
                User user1 = db.getUser(spin.getSelectedItem().toString());
                db.Close();
                //On gère le cas où on clique sur le switch
                if(switchAdmin.isChecked())
                {
                    user1.setIsAdmin(true);
                }
                else
                {
                    user1.setIsAdmin(false);
                }
                //On met à jour l'user dans la BDD
                db.openForWrite();
                db.updateUser(user1.getId(),user1);
                db.Close();
             break;


        }
    }
}
