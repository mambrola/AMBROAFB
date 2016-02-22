/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.car_fines;

import ambro.AView.Column;

/**
 *
 * @author Administrator
 */
public class CarAllData {
    
    @Column(title = "car number", width = "100")
    public String carNumber;
    
    @Column(title = "officer", width = "200")
    public String officer;
    
    @Column(title = "client number", width = "100")
    public int clientNumber;
    
    @Column(title = "client name", width = "200")
    public String clientName;
    
    @Column(title = "owner number", width = "100")
    public int ownerNumber;
    
    @Column(title = "owner name", width = "300")
    public String ownerName;
    
    @Column(title = "loan line ID", width = "100")
    public int loanLineID;
    
    @Column(title = "load ID", width = "100")
    public int loanID;
    
    
    public CarAllData(String carNumber, String officer, int clientNumber, String clientName, int ownerNumber, String ownerName, int loanLineID, int loanID){
        this.carNumber = carNumber;
        this.officer = officer;
        this.clientNumber = clientNumber;
        this.clientName = clientName;
        this.ownerNumber = ownerNumber;
        this.ownerName = ownerName;
        this.loanLineID = loanLineID;
        this.loanID = loanID;
    }
    
    @Override
    public String toString(){
        return carNumber + " " + officer + " " + clientName + " " + ownerNumber +
                " " + ownerName + " " + loanLineID + " " + loanID; 
    }
}
