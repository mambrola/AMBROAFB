/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

/**
 *
 * @author dkobuladze
 */
public class DocSide {
    
    private int id;
    private int account;
    private String descrip;
    private boolean isDebit;
    
    public DocSide(boolean isDebit){
        this.isDebit = isDebit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public boolean isIsDebit() {
        return isDebit;
    }

    /**
     * The method copy one DocSide to another if they are same side (both are debit or credit).
     * @param other 
     */
    public void copyFrom(DocSide other){
        if (isDebit == other.isDebit){
            setId(other.getId());
            setAccount(other.getAccount());
            setDescrip(other.getDescrip());
        }
    }
    
    @Override
    public boolean equals(Object other){
        DocSide otherDocSide = (DocSide)other;
        return getId() == otherDocSide.getId() && getAccount() == otherDocSide.getAccount() && getDescrip().equals(otherDocSide.getDescrip());
    }

    @Override
    public String toString() {
        return "DocSide{" + "id=" + id + ", account=" + account + ", descrip=" + descrip + ", isDebit=" + isDebit + '}';
    }
    
    
}
