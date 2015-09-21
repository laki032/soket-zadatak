/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import domen.Paket;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author laki
 */
public class NitPrijemPaketa extends Thread {

    private final NitSlanjePaketa nsp;
    private DataInputStream dis;

    public NitPrijemPaketa(DataInputStream dis, NitSlanjePaketa nsp) {
        this.nsp = nsp;
        this.dis = dis;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] paket = new byte[16];
                int len = dis.read(paket);
                
                if (len == 16) {
                    Paket p = new Paket(paket);
                    nsp.dodajPaket(p);
                    Logger.getLogger(this.getName()).log(Level.INFO, "Primljen dummy paket");
                } else if (len == 12) {
                    Logger.getLogger(this.getName()).log(Level.INFO, "Primljen cancel paket");
                    nsp.cancel();
                    break;
                } else {
                    Logger.getLogger(this.getName()).log(Level.WARNING, "Primljen paket koji nije duzine 12 ili 16 b.");
                    break;
                }
            }
            dis.close();
            Logger.getLogger(this.getName()).log(Level.INFO, "Nit za prijem paketa zavrsila sa radom.");
        } catch (IOException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

}
