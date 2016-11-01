/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.commands;

import hichat.models.CommandEnumeration;

/**
 *
 * @author mfikria
 */
public interface Command {
    public CommandEnumeration getType();   
}
