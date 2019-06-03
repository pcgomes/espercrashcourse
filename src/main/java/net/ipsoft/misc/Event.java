package net.ipsoft.misc;

public class Event {
    private String server;
    private String service;
    private Long unixTime;

    public Event(String server, String service, Long unixTime) {
        setServer(server);
        setService(service);
        setUnixTime(unixTime);
    }

    public Event(String[] parsedEvent) {
        setServer(parsedEvent[0]);
        setService(parsedEvent[1]);
        setUnixTime(Long.parseLong(parsedEvent[2]));
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(Long unixTime) {
        this.unixTime = unixTime;
    }

    @Override
    public String toString() {
        return "Server=" + getServer() + "|" +
                "Service=" + getService() + "|" +
                "UnixTime=" + getUnixTime();
    }

    @Override
    public boolean equals(Object event) {
        return this.server.equals(event)
                && this.service.equals(event)
                && this.unixTime.equals(event);
    }

}
