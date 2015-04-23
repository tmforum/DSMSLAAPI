/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.sla.event;

public enum SlaEventTypeEnum {

    SlaCreationNotification("SlaCreationNotification"),
    SlaUpdateNotification("SlaUpdateNotification"),
    SlaDeletionNotification("SlaDeletionNotification"),
    SlaValueChangeNotification("SlaValueChangeNotification");

    private String text;

    SlaEventTypeEnum(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     *
     * @param text
     * @return
     */
    public static org.tmf.dsmapi.sla.event.SlaEventTypeEnum fromString(String text) {
        if (text != null) {
            for (SlaEventTypeEnum b : SlaEventTypeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}