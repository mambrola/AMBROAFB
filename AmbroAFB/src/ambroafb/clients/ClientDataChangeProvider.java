/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ClientDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "client_delete";
    
    @Override
    public void deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
    }

    @Override
    public Client editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Client saveOneToDB(EditorPanelable object) throws Exception {
        Client clientFromAfB = (Client) object;
        Client clientFromDB = saveClient(clientFromAfB);
        if (clientFromDB == null) return null;
        clientFromAfB.getClientImageGallery().sendDataToServer("" + clientFromDB.getRecId(), (String path, Boolean isDeleted) -> {
            if (isDeleted){
                for (int i = 0; i < clientFromDB.getDocuments().size(); i++) {
                    Client.Document doc = clientFromDB.getDocuments().get(i);
                    if (doc.path.equals(path)){
                        clientFromDB.getDocuments().remove(i);
                        break;
                    }
                }
            }
            else {
                Client.Document doc = new Client.Document();
                doc.path = path;
                clientFromDB.getDocuments().add(doc);
            }
        }, new Client.GalleryDBUpdater(clientFromDB));
        
        return clientFromDB;
    }
    
    
    private static Client saveClient(Client client) throws IOException, AuthServerException{
        JSONObject targetJson = Utils.getJSONFromClass(client);
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject jsonFromDB = dbClient.insertUpdateFromAfb("client", targetJson);
        return Utils.getClassFromJSON(Client.class, jsonFromDB);
    }
}
