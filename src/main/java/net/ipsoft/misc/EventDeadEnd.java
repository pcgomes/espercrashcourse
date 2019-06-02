package net.ipsoft.misc;

public class EventDeadEnd {
    private String loggerName;
    private Event lastEvent = null;

    public EventDeadEnd(String loggerName) {
        this.loggerName = loggerName;
    }
    public void update(Object esperObject) {
        lastEvent = (Event) esperObject;
        System.out.println( "[" + loggerName + "] - Event" + lastEvent.getServer());
    }
    public Event getLastEvent(){
        return lastEvent;
    }

}