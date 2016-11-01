/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.models;

/**
 *
 * @author mfikria
 */
public enum NotificationEnumeration {
    SOMEONE_ADDED_YOU,
    SOMEONE_LEAVE_YOUR_GROUP,
    YOU_INVITED_TO_GROUP,
    SOMEONE_JOIN_YOUR_GROUP,
    LOGIN_SUCCESS;
    public String value() {
        return name();
    }
    public static NotificationEnumeration fromValue(String v) {
        return valueOf(v);
    }
}
