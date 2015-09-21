/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domen;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

/**
 *
 * @author laki
 */
public class Paket implements Serializable {

    private static final long serialVersionUID = 1L;
    private byte[] podaci;
    private int id;
    private int id2;
    private Date vremeZaSlanje;

    public Paket(byte[] paket) {
        podaci = paket;
        ByteBuffer bb = ByteBuffer.wrap(paket);
        bb = bb.order(ByteOrder.LITTLE_ENDIAN);
        id = bb.getInt();
        int len = bb.getInt();
        id2 = bb.getInt();
        long delay = bb.getInt();
        vremeZaSlanje = new Date(new Date().getTime() + delay * 1000);

        System.out.println("Primljen paket");
        System.out.println("\tid:" + id);
        System.out.println("\tlen:" + len);
        System.out.println("\tid2:" + id2);
        System.out.println("\tdelay:" + delay);
        System.out.println("");
    }

    public byte[] getPodaci() {
        return podaci;
    }

    public Date getVremeZaSlanje() {
        return vremeZaSlanje;
    }

    public int getId() {
        return id2;
    }

    public boolean isIstekao() {
        return vremeZaSlanje.before(new Date());
    }

}
