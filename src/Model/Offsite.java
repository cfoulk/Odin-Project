package Model;

import Server.OdinServer;

public class Offsite {
    public static void main(String[] args){
        OdinModel a = new OdinModel();
        System.out.println(a.getUserID("phil.thomas", "password"));
    }
}
