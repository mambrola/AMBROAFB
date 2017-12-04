/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.balance_accounts.BalAccountDataFetchProvider;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.clients.Client;
import ambroafb.clients.ClientDataFetchProvider;
import ambroafb.general.interfaces.DataFetchProvider;
import static ambroafb.general.interfaces.DataProvider.PARAM_FOR_ALL;
import ambroafb.general.interfaces.FilterModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import org.json.JSONObject;

/**
 * The class provides to fetch accounts data from DataBase. Note that it extends DataFetchProvider and has AccountDataFetchProvider instance.
 *  So it provides to implement abstract methods. In these methods it executes AccountDataFetchProvider appropriate method and adds extra actions.
 * @author dkobuladze
 */
@Deprecated
public class AccountDecoratorDataFetchProvider extends DataFetchProvider {

    private final AccountDataFetchProvider fetcher;
    
    private final BalAccountDataFetchProvider balAccountFetcher = new BalAccountDataFetchProvider();
    private final Map<Integer, BalanceAccount> balAccountsReflection = new HashMap<>();
    
    private final ClientDataFetchProvider clientsFetcher = new ClientDataFetchProvider();
    private final Map<Integer, Client> clientsReflection = new HashMap<>();
    
    private CountDownLatch latch = new CountDownLatch(2);
    
    public AccountDecoratorDataFetchProvider(AccountDataFetchProvider fetchProvider){
        fetcher = fetchProvider;
        
        Consumer<List<BalanceAccount>> balAccountsFetchSuccess = (balAccounts) -> {
            balAccounts.forEach((balAccount) -> balAccountsReflection.put(balAccount.getRecId(), balAccount));
            latch.countDown();
        };
        balAccountFetcher.filteredBy(PARAM_FOR_ALL, balAccountsFetchSuccess, null);
        
        Consumer<List<Client>> clientsFetchSuccess = (clients) -> {
            clients.forEach((client) -> clientsReflection.put(client.getRecId(), client));
            latch.countDown();
        };
        clientsFetcher.filteredBy(PARAM_FOR_ALL, clientsFetchSuccess, null);
    }
    
    @Override
    public List<Account> getFilteredBy(JSONObject params) throws Exception {
        List<Account> accounts = new ArrayList<>();
        if (fetcher != null){
            accounts.addAll(fetcher.getFilteredBy(params));
            fillAccountsObjectProperties(accounts);
        }
        return accounts;
    }

    @Override
    public List<Account> getFilteredBy(FilterModel model) throws Exception {
        List<Account> accounts = new ArrayList<>();
        if (fetcher != null){
            accounts.addAll(fetcher.getFilteredBy(model));
            fillAccountsObjectProperties(accounts);
        }
        return accounts;
    }
    
    @Override
    public Account getOneFromDB(int recId) throws Exception {
        Account account = null;
        if (fetcher != null){
            account = fetcher.getOneFromDB(recId);
            List<Account> accounts = new ArrayList<>();
            accounts.add(account);
            fillAccountsObjectProperties(accounts);
        }
        return account;
    }
    
    /**
     * The method waits while all clients and balAccounts entries are not fetched. Then iterates accounts list from DB and fill balAccount and client properties.
     * @param accounts The accounts list. They have not filling BalAccount and client properties.
     * @throws InterruptedException 
     */
    private void fillAccountsObjectProperties(List<Account> accounts) throws InterruptedException {
        latch.await(); // not wait if latch value is 0.
        accounts.forEach((account) ->  {
//                    account.balAccProperty().set(balAccountsReflection.get(account.getBalAccountId()));
//                    account.clientProperty().set(clientsReflection.get(account.getClientId()));
                });
    }
    
}
