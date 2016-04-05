/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tabramishvili
 */
public class KFZClient {

    private String serverAddress = "http://localhost:8080/KFZ_Server/api";

    private final Credentials credentials;
    private String token;

    public KFZClient(String username, String password) throws IOException, InvalidCredentialsException {
        this(new Credentials(username, password));
    }

    public KFZClient(Credentials credentials) throws IOException, InvalidCredentialsException {
        this.credentials = credentials;
        login();
    }

    public HttpURLConnection createConnection(String resource) throws IOException {
        URL url = createUrl(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + token);
        return con;
    }

    public final void login() throws IOException, InvalidCredentialsException {
        URL url = createUrl("/authentication");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.connect();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(con.getOutputStream(), credentials);
        int status = con.getResponseCode();
        if (status == 200) {
            token = Utils.readStream(con.getInputStream());
            System.out.println("token: " + token);
        } else if (status == 401) {
            con.disconnect();
            throw new InvalidCredentialsException();
        }
        con.disconnect();
    }

    public final void logout() {
        HttpURLConnection con = null;
        try {
            URL url = createUrl("/authentication/logout");
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.connect();
            int status = con.getResponseCode();
            System.out.println("status: " + status);
        } catch (ProtocolException ex) {
            Logger.getLogger(KFZClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KFZClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public final URL createUrl(String resource) {
        if (resource != null && !resource.startsWith("/")) {
            resource = "/" + resource;
        }
        String url_str = serverAddress + (resource == null ? "" : resource);
        try {
            return new URL(url_str);
        } catch (MalformedURLException ex) {
            Logger.getLogger(KFZClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static class Credentials {

        public final String username;
        public final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class InvalidCredentialsException extends Exception {
    }

}
