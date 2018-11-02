package com.example.fabio.plcmonitor.BDD;

public class User {

    private int id;

    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private boolean isAdmin;

    public User(String nom,String prenom, String email, String mdp, boolean isAdmin){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.isAdmin = isAdmin;
    }

    public int getId(){return id;}
    public void setId(int i){this.id = i;}

    public String getNom(){return nom;}
    public void setNom(String nom){this.nom = nom;}

    public String getPrenom(){return prenom;}
    public void setPrenom(String prenom){this.prenom = prenom;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPassword(){return mdp;}
    public void setPassword(String mdp){this.mdp = mdp;}

    public boolean getIsAdmin(){return isAdmin;}
    public void setAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    @Override
    public String toString(){
        return "ID: "+Integer.toString(id)+"\n"
                +"Email: "+email+"\n"
                +"Password: "+mdp+"\n"
                +"Nom: "+nom+"\n"
                +"Prenom: "+prenom+"\n"
                +"Admin: "+isAdmin;
    }
}
