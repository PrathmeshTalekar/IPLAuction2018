package com.spit.iplauction2018.iplauction2018;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    String displayName;
    String cash;
    String points;
    String batsman;
    String bowler;
    String wicketkeeper;
    String lobby;

    public User() {

    }

    public User(String displayName, String cash, String points) {
        this.displayName = displayName;
        this.cash = cash;
        this.points = points;
    }
    public User(String lobby){
        this.lobby=lobby;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
