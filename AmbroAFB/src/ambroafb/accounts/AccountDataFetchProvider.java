/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.accounts.detail_pane.helper.AccountCommonInfo;
import ambroafb.accounts.filter.AccountFilterModel;
import ambroafb.balance_accounts.BalAccountDataFetchProvider;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.clients.Client;
import ambroafb.clients.ClientDataFetchProvider;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AccountDataFetchProvider extends DataFetchProvider {

    private final String ACCOUNT_INFO_PROCEDURE = "account_get_info";
    private final String ACCOUNT_DEBIT_CREDIT_ENTRIES_PROCEDURE = "account_get_turnover";
    
    private final int separateFetchersCount = 2;
    
    private final BalAccountDataFetchProvider balAccountFetcher = new BalAccountDataFetchProvider();
    private final Map<Integer, BalanceAccount> balAccountsReflection = new HashMap<>();
    
    private final ClientDataFetchProvider clientsFetcher = new ClientDataFetchProvider();
    private final Map<Integer, Client> clientsReflection = new HashMap<>();
    
    public AccountDataFetchProvider(){
        DB_VIEW_NAME = "accounts_whole";
        
        Consumer<List<BalanceAccount>> balAccountsFetchSuccess = (balAccounts) -> {
            System.out.println("<-- balAccountsFetchSuccess -->");
            balAccounts.forEach((balAccount) -> balAccountsReflection.put(balAccount.getRecId(), balAccount));
            System.out.println(": " + latch.getCount());
            latch.countDown();
        };
        balAccountFetcher.filteredBy(PARAM_FOR_ALL, balAccountsFetchSuccess, null);
        
        Consumer<List<Client>> clientsFetchSuccess = (clients) -> {
            System.out.println("<-- clientsFetchSuccess -->");
            clients.forEach((client) -> clientsReflection.put(client.getRecId(), client));
            System.out.println(": " + latch.getCount());
            latch.countDown();
        };
        clientsFetcher.filteredBy(PARAM_FOR_ALL, clientsFetchSuccess, null);
    }

    @Override
    protected int getLatchValue() {
        return separateFetchersCount;
    }

    @Override
    protected Consumer<List<Account>> getInfluenceAction() {
        return (accounts) -> {
                        accounts.forEach((account) -> {
                                    account.balAccProperty().set(balAccountsReflection.get(account.getBalAccountId()));
                                    System.out.println("account.getClientId(): " + account.getClientId());
                                    account.clientProperty().set(clientsReflection.get(account.getClientId()));
                                });
                };
    }

    @Override
    public List<Account> getFilteredBy(JSONObject params) throws Exception {
        return getObjectsListFromDBTable(Account.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<Account> getFilteredBy(FilterModel model) throws Exception {
        AccountFilterModel accountFiletModel = (AccountFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        
        if (accountFiletModel.isSelectedConcreteCurrency()){
            whereBuilder.and("iso", "=", accountFiletModel.getCurrencyIso());
        }
        if (accountFiletModel.isSelectedConcreteBalAccount()){
            whereBuilder.and("bal_account_id", "=", accountFiletModel.getBalAccountId());
        }
        if (accountFiletModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", accountFiletModel.getClientId());
        }
        if (!accountFiletModel.getTypeIndeterminate()){
            String relation = (accountFiletModel.isTypeSelected()) ? "is null" : "is not null";
            whereBuilder.and("date_close", relation, "");
        }
        // this is extra condition, none of statements was setisfied:
        whereBuilder.and(DB_ID, ">", 0);
        JSONObject params = whereBuilder.condition().build();
        return getFilteredBy(params);
    }

    @Override
    public Account getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Account.class, DB_VIEW_NAME, params);
    }

    public void fetchAccountInfo(int accountId, Consumer<AccountCommonInfo> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            AccountCommonInfo accInfo = null;
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            try {
                ArrayList<AccountCommonInfo> data = callProcedure(AccountCommonInfo.class, ACCOUNT_INFO_PROCEDURE, dbClient.getLang(), accountId);
                Platform.runLater(() -> {
                    if (successAction != null && data != null) successAction.accept(data.get(0));
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    public void getAccountEntries(int accountId, LocalDate from, LocalDate to, Consumer<JSONArray> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            try {
                DBClient dbClient = GeneralConfig.getInstance().getDBClient();
                JSONArray data = dbClient.callProcedureAndGetAsJson(ACCOUNT_DEBIT_CREDIT_ENTRIES_PROCEDURE, accountId, from, to);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(data);
                });
            } catch (AuthServerException | IOException ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
}
