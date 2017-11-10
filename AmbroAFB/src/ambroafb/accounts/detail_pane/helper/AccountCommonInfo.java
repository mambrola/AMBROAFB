/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane.helper;

/**
 *
 * @author dkobuladze
 */
public class AccountCommonInfo {
    
    private String accountDescrip = "", clientDescrip = "", balAccDescrip = "";
    private boolean debitIsRed, creditIsRed;
    
    public AccountCommonInfo(){
    
    }

    public String getAccountDescrip() {
        return accountDescrip;
    }

    public String getClientDescrip() {
        return clientDescrip;
    }

    public String getBalAccDescrip() {
        return balAccDescrip;
    }

    public boolean isDebitRed() {
        return debitIsRed;
    }

    public boolean isCreditRed() {
        return creditIsRed;
    }

    
    public void setAccountDescrip(String accountDescrip) {
        this.accountDescrip = accountDescrip;
    }

    public void setClientDescrip(String clientDescrip) {
        this.clientDescrip = clientDescrip;
    }

    public void setBalAccDescrip(String balAccDescrip) {
        this.balAccDescrip = balAccDescrip;
    }

    public void setDebitIsRed(boolean isDebitRed) {
        this.debitIsRed = isDebitRed;
    }

    public void setCreditIsRed(boolean isCreditRed) {
        this.creditIsRed = isCreditRed;
    }

    @Override
    public String toString() {
        return "AccountCommonInfo{" + "accountDescrip=" + accountDescrip + ", clientDescrip=" + clientDescrip + ", balAccDescrip=" + balAccDescrip + ", debitIsRed=" + debitIsRed + ", creditIsRed=" + creditIsRed + '}';
    }
    
    
}
