package net.ipsoft.misc;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EsperTest {

    @Before
    public void before() {
        EsperCrashCourse.initEsperEngine();
    }

    @Test
    public void testEventFromSameServer() {
        Event aEvent = new Event("a","b", (long) 1);
        EventDeadEnd subscriber = EsperCrashCourse.createEventSubscriber("ServerA",
                "select * from Event as ev where ev.server = 'a'" );
        sendTestEvent(aEvent);
        try {
            Thread.sleep(1000);
        } catch (Exception e){};
        assertThat(subscriber.getLastEvent().getServer(),is(aEvent.getServer()));

        Event zEvent = new Event("z","b", (long) 1);
        sendTestEvent(aEvent);
        try {
            Thread.sleep(1000);
        } catch (Exception e){};
        assertThat(subscriber.getLastEvent().getServer(),is(zEvent.getServer()));
    }

    private void sendTestEvent(Object event) {
       EsperCrashCourse.getEsperEngine().getEPRuntime().sendEvent(event);
    }
}