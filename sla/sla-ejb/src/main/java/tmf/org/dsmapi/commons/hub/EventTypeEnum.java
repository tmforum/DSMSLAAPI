/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub;

/**
 *
 * @author pierregauthier
 */
public enum EventTypeEnum {

    CreateNotification("CreateNotification"),
    StatusChangedNotification("StatusChangedNotification"),
    ValueChangeNotification("ValueChangeNotification"),
    RemoveNotification("RemoveNotification");
    
    private String text;

    EventTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static tmf.org.dsmapi.commons.hub.EventTypeEnum fromString(String text) {
        if (text != null) {
            for (EventTypeEnum b : EventTypeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}