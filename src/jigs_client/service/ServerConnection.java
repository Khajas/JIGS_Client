/*
 * Copyright (C) 2017 Anwar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jigs_client.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Anwar
 */
public class ServerConnection {
    private boolean debugMode;
    private final String hostname;
    public ServerConnection(String hostname){
        this.hostname=hostname;
    }
    public String authenticateUser(String username, String password) 
            throws IOException{
        return this.processRequest("http://"+hostname+":8080/jigs/webapi/user/authenticate/"
                +username+"_"+password);
    }
    
    public String registerUser(String username, String password) 
            throws IOException{
        return this.processRequest("http://"+hostname+":8080/jigs/webapi/user/register/"+username+"_"+password);
    }
    
    public String readMessages(String username) 
            throws MalformedURLException, IOException{
        return this.processRequest("http://"+hostname+":8080/jigs/webapi/messages/retreive/"+username);
    }
    
    public String sendMessage(String sender, String recepient, String message) 
            throws MalformedURLException, IOException{
        message=message.replaceAll(" ", "_");
        return this.processRequest("http://"+hostname+":8080/jigs/webapi/messages/sendmessage/from/"
                +sender+"/to/"+recepient+"/message/"+message);
    }
    /**
     * Reads response from the given HttpURLConnection
     * @param connection
     *      URL connection to the request API.
     * @return server_response
     *      Response received after opening the connection.
     * @throws IOException 
     *      Signals that an I/O exception of some sort has occurred.
     */
    private String readResponse(HttpURLConnection connection ) throws IOException{
        String server_response="";
        InputStream response=(InputStream)connection.getContent();
        int numBytes=response.available();
        for(int i=0;i<numBytes;++i)
           server_response+=((char)response.read());
        return server_response;
    }
    /**
     * Makes connection to server for the given URL
     * @param url
     *      API URL that should be called to serve the request.
     * @return HttpURLConnection
     *      Returns the connection variable from the above URL for GET method
     */
    private HttpURLConnection connectServer(URL url){
         HttpURLConnection connection;
         try{
             connection = (HttpURLConnection) url.openConnection(); 
             connection.setDoOutput(true); 
             connection.setInstanceFollowRedirects(false); 
             connection.setRequestMethod("GET");
         }catch(IOException e){
             throw new RuntimeException(e);
         }
         return connection;
     }
    
    private String processRequest(String connect_url)
                    throws MalformedURLException, IOException{
        String server_response="";
        String httpQuery=connect_url;
        if(debugMode) System.out.println("Http query: "+httpQuery);
        URL url;
        try{
            url = new URL(httpQuery); 
        }catch(MalformedURLException e){
            throw new MalformedURLException("Invalid query!");
        }
        HttpURLConnection connection = this.connectServer(url); 
        String responseCode=connection.getResponseMessage();
        if(debugMode) System.out.println("Respose code: "+responseCode);
        if(responseCode.equals("OK")){
            server_response=this.readResponse(connection);
        }
        connection.disconnect();
        return server_response;    
    }

    public String findUser(String username) throws IOException {
        return this.processRequest("http://"+hostname+":8080/jigs/webapi/user/search/"+username);
    }
}
//////////////////////  END OF SOURCE FILE  ////////////////////////////////////