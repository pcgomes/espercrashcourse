package net.ipsoft.misc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.espertech.esper.client.EPAdministrator;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

public class EsperTest {

    private void sendTestEvent(Object event) {
        EsperCrashCourse.getEsperEngine().getEPRuntime().sendEvent(event);
    }

    @Before
    public void before() {
        EsperCrashCourse.initEsperEngine();
    }

    @Test
    public void testEventFromSameServer() {
        EventSubscriber subscriber = EsperCrashCourse.createEventSubscriber("EventsToServerA",
                "select * from Event as ev where ev.server = 'ServerA'" );

        // Send first event
        Long currentTime =new Long((new Date()).getTime()/1000);
        Event aEvent = new Event("ServerA","httpd", currentTime);
        sendTestEvent(aEvent);

        // Check it arrived at Subscriber
        assertThat(subscriber.getLastEvent().getServer(),is(aEvent.getServer()));

        // Send second event
        Event zEvent = new Event("ServerZ","httpd", currentTime);
        sendTestEvent(zEvent);

        // Check it was not captured by subscriber
        assertThat(subscriber.getLastEvent().getServer(),is(not(zEvent.getServer())));
    }

    @Test
    public void testTimeWindow() {
        EPAdministrator esperEngine = EsperCrashCourse.getEsperEngine().getEPAdministrator();

        // Create a timed-window named TestWindow with objects of type Event
        esperEngine.createEPL("create window TestWindow.win:time(5 sec) as Event");
        esperEngine.createEPL("insert into TestWindow select * from Event");

        // Create a subscriber to events that leave the timed window
        EventSubscriber subscriber = EsperCrashCourse.createEventSubscriber("EventsFromTimedWindow",
                "select rstream * from TestWindow" );

        // Create test event and send
        Long currentTime =new Long((new Date()).getTime()/1000);
        Event aEvent = new Event("ServerA","httpd", currentTime);
        sendTestEvent(aEvent);


        // Check that event hasn't arrived after two seconds
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
        assertThat(subscriber.getLastEvent(),is(nullValue()));

        // Check that event has arrived
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
        assertThat(subscriber.getLastEvent(),is(notNullValue()));

    }

}