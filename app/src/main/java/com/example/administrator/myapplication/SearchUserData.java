package com.example.administrator.myapplication;

/**
 * Created by Administrator on 18/10/2559.
 */
public class SearchUserData {
    private String usr_mac_address = null;

    public SearchUserData(String usr_mac_address) {
        this.usr_mac_address = usr_mac_address;
    }

    public String getUsr_mac_address() {
        return usr_mac_address;
    }

    public void setUsr_mac_address(String usr_mac_address) {
        this.usr_mac_address = usr_mac_address;
    }
}
