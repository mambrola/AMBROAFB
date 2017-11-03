/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.exceptions;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import authclient.AuthServerException;

/**
 *
 * @author dkobuladze
 */
public class ExceptionsFactory {
    
    private static final int forbidden = 403;
    private static final int notAllow = 400;
    private static final int anyOther = 500;
    
    public static Exception getAppropriateException(AuthServerException ex) {
        Exception exception = new DBActionException(ex.getLocalizedMessage(), Names.BUNDLE_TITLES_NAME, GeneralConfig.getInstance().locale);
        if (ex.getStatusCode() == notAllow || ex.getStatusCode() == anyOther){
            return new SomeKindOfException(ex.getLocalizedMessage());
        }
        return exception;
    }
    
}
