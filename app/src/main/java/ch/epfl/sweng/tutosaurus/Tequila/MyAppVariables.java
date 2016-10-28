package ch.epfl.sweng.tutosaurus.Tequila;

import android.app.Application;

/**
 * Created by Stephane on 10/28/2016.
 */

public class MyAppVariables extends Application {
    private static boolean registered = false;

    public static boolean getRegistered(){
        return registered;
    }

    public static void setRegistered(boolean reg){
        registered = reg;
    }
}
