/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Erick Chandra
 */
public class ResponseCommand implements Serializable {
    String status;
    Map<String, Object> objectMap;
    
    public ResponseCommand() {
        this.objectMap = new HashMap<>();
    }
    public ResponseCommand(String status) {
        this.status = status;
        this.objectMap = null;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public Map<String, Object> getObjectMap() {
        return this.objectMap;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void addObjectMap(String newKey, Object newObject) {
        this.objectMap.put(newKey, newObject);
    }
}
