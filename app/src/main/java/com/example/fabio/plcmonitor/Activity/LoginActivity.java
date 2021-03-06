package com.example.fabio.plcmonitor.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserAccessDB;
import com.example.fabio.plcmonitor.Configs;
import com.example.fabio.plcmonitor.R;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    String email;
    String mdp;
    EditText etEmail;
    EditText etMdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = ((EditText)findViewById(R.id.et_login_email));
        etMdp = ((EditText)findViewById(R.id.et_login_mdp));

    }

    public void btnLoginClick(View v)
    {
        email = etEmail.getText().toString();
        mdp = etMdp.getText().toString();

        //Vérification champs remplis
        if(!email.isEmpty() && !mdp.isEmpty())
        {
            UserAccessDB db = new UserAccessDB(this);
            db.openForRead();

            //On recrée l'user
            try
            {
                User user1 = db.getUser(email);
                db.Close();

                //On vérifie si le mot de passe entrée correspond à l'user qui possède le mail
                if(mdp.equals(user1.getMdp()))
                {
                    Toast.makeText(getApplicationContext(), "Bienvenue " + user1.getFirstname() + " "+user1.getLastname(),Toast.LENGTH_SHORT).show();
                    if(user1.getIsAdmin())
                    {
                        Configs.setIsAdmin(true);
                    }
                    if(user1.getWriteAccess())
                    {
                        Configs.setIsWriteAccess(true);
                    }

                    //On est connecté, on passe à l'activité de management
                    Intent intent = new Intent(getApplicationContext(), ManagementActivity.class);
                    startActivity(intent);

                }

                else
                {
                    Toast.makeText(this, "Le mot de passe est incorrect", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, "L'adresse e-mail est incorrect", Toast.LENGTH_LONG).show();
            }

        }

        else
        {
            Toast.makeText(getApplicationContext(), "Tous les champs doivent être remplis",Toast.LENGTH_SHORT).show();
        }
    }
}
