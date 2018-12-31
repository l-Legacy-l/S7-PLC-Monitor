package com.example.fabio.plcmonitor.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserAccessDB;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;

import java.util.ArrayList;

public class ManagementUserActivity extends AppCompatActivity {

    UserAccessDB db;

    Spinner spin;
    Switch switchAdmin, switchWrite;

    EditText etMdp;
    String mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_user);
        fillingSpin();

        switchAdmin = (Switch) findViewById(R.id.sw_muser_admin);
        switchWrite = (Switch) findViewById(R.id.sw_muser_write);

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

                //Idem pour les droit en écriture pour l'automate
                if (user1.getWriteAccess())
                {
                    switchWrite.setChecked(true);
                }

                else
                {
                    switchWrite.setChecked(false);
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
                    Configs.setIsAdmin(true);
                }
                else
                {
                    user1.setIsAdmin(false);
                    Configs.setIsAdmin(false);
                }
                //On met à jour l'user dans la BDD
                db.openForWrite();
                db.updateUser(user1.getId(),user1);
                db.Close();
             break;

            case R.id.sw_muser_write:
                db.openForRead();
                User user2 = db.getUser(spin.getSelectedItem().toString());
                db.Close();
                //On gère le cas où on clique sur le switch
                if(switchWrite.isChecked())
                {
                    user2.setWriteAccess(true);
                    Configs.setIsWriteAccess(true);
                }
                else
                {
                    user2.setWriteAccess(false);
                    Configs.setIsWriteAccess(false);
                }
                //On met à jour l'user dans la BDD
                db.openForWrite();
                db.updateUser(user2.getId(),user2);
                db.Close();
             break;


            case R.id.bt_muser_editMdp:
                mdp = etMdp.getText().toString();
                db.openForRead();
                User user3 = db.getUser(spin.getSelectedItem().toString());
                db.Close();
                //On met à jour le mdp
                user3.setMdp(mdp);
                db.openForWrite();
                db.updateUser(user3.getId(),user3);
                db.Close();

                Toast.makeText(getApplicationContext(), "Le mot de passe a bien été modifié",Toast.LENGTH_SHORT).show();
             break;

            case R.id.bt_muser_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ManagementUserActivity.this);
                builder.setMessage("Cette action supprimera l'utilisateur, voulez-vous continuer ?");
                builder.setCancelable(true);
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.openForRead();
                        User user4 = db.getUser(spin.getSelectedItem().toString());
                        db.Close();

                        db.openForWrite();
                        db.removeUser(user4.getId());
                        db.Close();
                        //On reremplis le spinner sinon l'utilsateur reste selectionnable dans le spinner
                        fillingSpin();

                        Toast.makeText(getApplicationContext(), "L'utilisateur a bien été supprimé",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
    }
}
