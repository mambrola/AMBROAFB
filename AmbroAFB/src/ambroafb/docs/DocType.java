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
public class DocType {
    
    private int id;
    private String descrip;
    
    public DocType(){}
    
    public DocType(int id, String descrip){
        this.id = id;
        this.descrip = descrip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    @Override
    public String toString() {
        return descrip;
    }
    
    
    
}
