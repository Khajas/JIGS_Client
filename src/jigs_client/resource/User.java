package jigs_client.resource;


import java.io.Serializable;

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

/**
 * A user class
 * @author Anwar
 */
public class User implements Serializable{
    private final String username;
    private final String password;
    public User(String username){
        this.username=username;
        password=null;
    }
    public User(String username, String password){
        this.username=username;
        this.password=password;
    }
    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
////////////////    END OF SOURCE FILE  //////////////////