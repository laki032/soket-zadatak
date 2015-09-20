package thread;

import domen.Paket;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author laki
 */
public class NitSlanjePaketa extends Thread {

    private List<Paket> listaPaketa;
    private DataOutputStream dosSoket;

    //konstruktor pri kreiranju ucitava listu prethodnih paketa i salje notifikacije koliko je paketa isteklo
    public NitSlanjePaketa(DataOutputStream dos) {
        dosSoket = dos;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("file.txt"));
            listaPaketa = (List<Paket>) ois.readObject();
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.INFO, "Ucitana lista, broj preostalih paketa: " + listaPaketa.size());
            ois.close();
            while (true) {
                if (listaPaketa.size() > 0) {
                    Paket p = listaPaketa.get(0);
                    if (p.isIstekao()) {
                        //posalji notifikaciju da je istekao
                        dosSoket.writeChars("Paket [id=" + p.getId() + "] je istekao u medjuvremenu.");
                        Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.INFO, "Paket [id={0}] je istekao u medjuvremenu.", p.getId());
                        listaPaketa.remove(0);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.INFO, "Poslate notifikacije o isteklim paketima.");
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss_dd/MM");
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.INFO, "Prvi naredni paket bice poslat u: " + sdf.format(listaPaketa.get(0).getVremeZaSlanje()));
        } catch (FileNotFoundException fnfex) {
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.SEVERE, "Nije postojao file");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            File f = new File("file.txt");
            if (listaPaketa == null) {
                listaPaketa = new ArrayList<Paket>();
            }
        }
    }

    @Override
    public void run() {
        //slanje paketa u odnosu na to da li je delay vreme proslo
        while (!isInterrupted()) {
            if (listaPaketa.size() > 0) {
                Paket p = listaPaketa.get(0);
                if (p.isIstekao()) {
                    try {
                        dosSoket.write(p.getPodaci());
                        Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.INFO, "Poslat paket [id={0}].", p.getId());
                    } catch (IOException ex) {
                        Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.SEVERE, ex.getMessage());
                    }
                    listaPaketa.remove(0);
                }
            }
        }
        try {
            dosSoket.close();
        } catch (IOException ex) {
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        Logger.getLogger(this.getName()).log(Level.INFO, "Nit za slanje paketa zavrsila sa radom.");
    }

    //metoda dodaje novi paket u listu po rastucoj vrednosti vremena za slanje
    public void dodajPaket(Paket p) {
        boolean dodat = false;
        for (Paket pul : listaPaketa) {
            if (!pul.getVremeZaSlanje().before(p.getVremeZaSlanje())) {
                dodat = true;
                listaPaketa.add(listaPaketa.indexOf(pul), p);
                break;
            }
        }
        if (!dodat) {
            listaPaketa.add(p);
        }
    }

    //metoda zaustavlja slanje paketa i listu preostalih paketa serijalizuje u fajl
    public void cancel() {
        try {
            File f = new File("file.txt");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(listaPaketa);
            oos.close();
            Logger.getLogger("").log(Level.INFO, "Lista preostalih paketa upisana u fajl.");
            this.interrupt();
        } catch (Exception ex) {
            Logger.getLogger(NitSlanjePaketa.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

}
