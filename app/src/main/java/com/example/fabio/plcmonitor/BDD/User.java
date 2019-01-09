package com.example.fabio.plcmonitor.BDD;

public class User {

    private int id;
    private String firstname, lastname, email, mdp;
    private boolean writeAccess, isAdmin;

    public User() {}

    public User(String lastname,String firstname, String email, String mdp,boolean writeAccess, boolean isAdmin){
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.mdp = mdp;
        this.writeAccess = writeAccess;
        this.isAdmin = isAdmin;
    }

    public int getId(){return id;}
    public void setId(int i){this.id = i;}

    public String getFirstname(){return firstname;}
    public void setFirstname(String firstname){this.firstname = firstname;}

    public String getLastname(){return lastname;}
    public void setLastname(String lastname){this.lastname = lastname;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getMdp(){return mdp;}
    public void setMdp(String mdp){this.mdp = mdp;}

    public boolean getWriteAccess() { return writeAccess; }
    public void setWriteAccess(boolean writeAccess) { this.writeAccess = writeAccess; }

    public boolean getIsAdmin(){return isAdmin;}
    public void setIsAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    @Override
    public String toString(){
        return "ID: "+Integer.toString(id)+"\n"
                +"Email: "+email+"\n"
                +"Password: "+mdp+"\n"
                +"Nom: "+lastname+"\n"
                +"Prenom: "+firstname+"\n"
                +"Admin: "+isAdmin;
    }
}
