/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class DocCode {
    
    private final StringProperty docCode;
    
    public DocCode(){
        docCode = new SimpleStringProperty("");
    }
    
    public StringProperty docCodeProperty(){
        return docCode;
    }

    public String getDocCode() {
        return docCode.get();
    }

    public void setDocCode(String docCode) {
        this.docCode.set(docCode);
    }
    
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        DocCode otherDocCode = (DocCode)other;
        return getDocCode().equals(otherDocCode.getDocCode());
    }

    @Override
    public String toString() {
        return docCode.get();
    }
    
}
