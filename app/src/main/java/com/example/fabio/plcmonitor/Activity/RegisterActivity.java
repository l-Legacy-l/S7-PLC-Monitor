package com.example.fabio.plcmonitor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserAccessDB;
import com.example.fabio.plcmonitor.R;

public class RegisterActivity extends AppCompatActivity {


    EditText nom;
    EditText prenom;
    EditText email;
    EditText mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nom = ((EditText)findViewById(R.id.et_register_nom));
        prenom = ((EditText)findViewById(R.id.et_register_pre));
        email = ((EditText)findViewById(R.id.et_register_email));
        mdp = ((EditText)findViewById(R.id.et_register_mdp));

    }

    public void registerClick(View v)
    {
        //Si les champs ne sont pas vide
        if(!nom.getText().toString().isEmpty() && !prenom.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&
                !mdp.getText().toString().isEmpty())
        {
            //Si le mail a un format valide
            if (isEmailValid(email.getText().toString()))
            {
                //Si le mot de passe contient 4 caractères au minimum
                if(mdp.length() >= 4)
                {
                    //On peut créer l'utilisateur
                    User user = new User(nom.getText().toString(),prenom.getText().toString(),email.getText().toString(),
                            mdp.getText().toString(),false);

                    UserAccessDB userDB = new UserAccessDB(this);
                    userDB.openForWrite();

                    //On vérifie que le mail n'a pas déja été utilisé pour éviter les doublons
                    if(userDB.getUser(email.getText().toString()) == null)
                    {
                        userDB.insertUser(user);
                        userDB.Close();

                        Toast.makeText(getApplicationContext(), "Inscription réussie",Toast.LENGTH_SHORT).show();
                        finish();
                        //On affiche la page de connexion
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "Cette adresse mail a déjà été utilisée",Toast.LENGTH_SHORT).show();
                        userDB.Close();
                    }

                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir au minimum 4 caractères",Toast.LENGTH_SHORT).show();
                }
            }

            else
            {
                Toast.makeText(getApplicationContext(), "L'e-mail introduit est incorrect",Toast.LENGTH_SHORT).show();
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Vous devez compléter tous les champs !",Toast.LENGTH_SHORT).show();
            System.out.println("je passe "+nom);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
