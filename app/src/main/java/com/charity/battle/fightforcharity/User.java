package com.charity.battle.fightforcharity;

/**
 * Created by bge on 15-07-18.
 */
public class User {
    private String username;
    private String password;
    // TODO: charity
    private Float donation;
    // TODO: paypal

    public String getUsername() {
        return username;
    }
    public void setUsername(String newName){
        username = newName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String newPass) {
        password = newPass;
    }

    public Float getDonation(){
        return donation;
    }

    public void setDonation(Float newDonation) {
        donation = newDonation;
    }


}
