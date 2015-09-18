/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import thread.NitSlanjePaketa;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import thread.NitPrijemPaketa;

/**
 *
 * @author laki
 */
public class Main {

    public static void main(String[] args) {
        try {
            Socket s = new Socket("matilda.plusplus.rs", 4000);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            final NitSlanjePaketa nsp = new NitSlanjePaketa(dos);
            nsp.start();
            final NitPrijemPaketa npp = new NitPrijemPaketa(dis, nsp);
            npp.start();
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Pokrenute niti za slanje i prijem.");
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    nsp.cancel();
                    npp.interrupt();
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

}
