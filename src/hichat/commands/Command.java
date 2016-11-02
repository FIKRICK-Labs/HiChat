/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.commands;

import java.io.Serializable;


/**
 *
 * @author mfikria
 */
public class Command implements Serializable {
    protected String type;
    public String getType(){
        return this.type;
    }   
}
