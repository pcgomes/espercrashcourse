package net.ipsoft.misc;

import org.slf4j.LoggerFactory;

/**
 * Create subscriber objects
 */
public class EventSubscriber {
    private String loggerName;
    private Event lastEvent = null;

    public EventSubscriber(String loggerName) {
        this.loggerName = loggerName;
    }

    public EventSubscriber() {
        this("defaultLogger");
    }

    public void update(Object esperObject) {
        lastEvent = (Event) esperObject;
        LoggerFactory.getLogger(loggerName).info(lastEvent.toString());
        //System.out.println(lastEvent.toString());
    }

    public Event getLastEvent(){
        return lastEvent;
    }

}