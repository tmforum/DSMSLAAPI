/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.slaViolation.event;

public enum SlaViolationEventTypeEnum {

    SlaViolationCreationNotification("SlaViolationCreationNotification"),
    SlaViolationUpdateNotification("SlaViolationUpdateNotification"),
    SlaViolationDeletionNotification("SlaViolationDeletionNotification"),
    SlaViolationValueChangeNotification("SlaViolationValueChangeNotification");

    private String text;

    SlaViolationEventTypeEnum(String text) {
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
    public static org.tmf.dsmapi.slaViolation.event.SlaViolationEventTypeEnum fromString(String text) {
        if (text != null) {
            for (SlaViolationEventTypeEnum b : SlaViolationEventTypeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}