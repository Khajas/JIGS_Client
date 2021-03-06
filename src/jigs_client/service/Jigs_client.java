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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import jigs_client.resource.Message;
import jigs_client.resource.User;


/**
 *
 * @author Anwar
 */
public class Jigs_client {
    private static final Scanner sc=new Scanner(System.in);
    private static final ServerConnection srcn=new ServerConnection("132.148.65.148");
    // Uncomment the following line if you're working on local server
    //private static final ServerConnection srcn=new ServerConnection("localhost");
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static void main(String[] args) 
            throws FileNotFoundException, IOException, 
            ClassNotFoundException, NoSuchAlgorithmException {
        String username;
        String password;
        try {
            final FileInputStream fis= new FileInputStream("./conf");
            final ObjectInputStream ois=new ObjectInputStream(fis);
            User user=(User) ois.readObject();
            String au=srcn.authenticateUser(user.getUsername(), user.getPassword());
//            System.out.println(au);
            if(au.contains("Logged")){
                System.out.println("Hello "+user.getUsername()+"!");
                ois.close();
                fis.close();
                // Let's read and write message
                while(true){
                    System.out.print("Check new messages? ");
                    String choice=sc.nextLine();
                    if(choice.equalsIgnoreCase("yes")){
                        System.out.println(readMessages(user.getUsername()));
                    }
                    System.out.print("Compose a new message? ");
                    choice=sc.nextLine();
                    if(choice.equalsIgnoreCase("yes")){
                        System.out.print("To: ");
                        User to=new User(sc.nextLine());
                        System.out.print("Message: ");
                        Message msg=new Message(to,user,sc.nextLine());
                        sendMessage(msg);
                    }
                }
            }
            else{
                System.out.println("Authentication error!");
                System.out.println("Have you changed your password?");
                password=sc.nextLine();
                MessageDigest m=MessageDigest.getInstance("MD5");
                m.update(password.getBytes(),0,password.length());
                password=(new BigInteger(1,m.digest()).toString(16));
                String aut=srcn.authenticateUser(user.getUsername(), password);
                if(aut.contains("Logged")){
                    writeToFile(user.getUsername(),password);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.print("First time user?\nPlease choose a username: ");
            username=sc.nextLine();
            System.out.print("Please choose a password: ");
            password=sc.nextLine();
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(password.getBytes(),0,password.length());
            password=(new BigInteger(1,m.digest()).toString(16));
            try{
                String ru = srcn.registerUser(username, password);
                if(!ru.contains("fail")){
                    writeToFile(username, password);
                }
                String auth=srcn.authenticateUser(username, password);
                if(auth.contains("Logged")){
                    writeToFile(username, password);
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }            
        }
    }
    public static String sendMessage(Message message){
        try {
            return srcn.sendMessage(message.getFrom(), message.getTo(), message.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return "Can't send messages!";
        }
    }
    public static String readMessages(String username){
        try {
            return srcn.readMessages(username);
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }
    public static void writeToFile(String username, String password) 
            throws FileNotFoundException, IOException{
        User user=new User(username, password);
        try (FileOutputStream fos = new FileOutputStream("./conf"); 
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            oos.flush();
        }
    }
    
    public static String findUser(String username) 
            throws IOException{
        return srcn.findUser(username);
    }
    
}
/////////////////////   END OF SOURCE FILE  //////////////////////