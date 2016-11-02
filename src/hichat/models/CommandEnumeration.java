/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.models;

/**
 *
 * @author Erick Chandra
 */
public enum CommandEnumeration {
    ADDFRIEND,
    LEAVEGROUP,
    LOGIN,
    REGISTER,
    CREATEGROUP,
    ADDGROUPMEMBER;
    
    public String value() {
        return name();
    }
    public static CommandEnumeration fromValue(String v) {
        return valueOf(v);
    }
}