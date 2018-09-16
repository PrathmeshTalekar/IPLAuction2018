package com.spit.iplauction2018.iplauction2018;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    String displayName;
    String cash;
    String points;
    String batsman;
    String bowler;
    String wicketkeeper;
    Boolean inLobby;

    public User() {

    }

    public User(String displayName, String cash, String points, Boolean inLobby) {
        this.displayName = displayName;
        this.cash = cash;
        this.points = points;
        this.inLobby = inLobby;
    }


}
