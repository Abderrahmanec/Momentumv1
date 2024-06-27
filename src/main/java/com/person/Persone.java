package com.person;
public abstract class Persone {
    protected int id;
    protected String username;
    protected String password;
    protected boolean isAdmin;

    public Persone(int id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Persone() {

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public abstract boolean authenticate();
}
