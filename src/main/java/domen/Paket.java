/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domen;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author laki
 */
public class Paket implements Serializable {

    private static final long serialVersionUID = 1L;
    private byte[] podaci;
    private int id;
    private Date vremeZaSlanje;

    public Paket(byte[] paket) {
        podaci = paket;
        ByteBuffer bb = ByteBuffer.wrap(paket);
        id = bb.getInt();
        bb.getLong(); //preskoci 8 bajtova
        long delay = bb.getInt();
        vremeZaSlanje = new Date(new Date().getTime() + delay);
    }
    
    public byte[] getPodaci() {
        return podaci;
    }

    public Date getVremeZaSlanje() {
        return vremeZaSlanje;
    }

    public int getId() {
        return id;
    }
    
    public boolean isIstekao() {
        return !vremeZaSlanje.after(new Date());
    }

}
