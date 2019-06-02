package net.ipsoft.misc;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import java.util.ArrayList;
import java.util.List;

public class EventProcessor extends StringDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, java.util.List<java.lang.Object> out) throws Exception {
        List<Object> inputLine = new ArrayList<Object>();

        // Convert into String
        super.decode(ctx, in, inputLine);

        Event incomingEvent = new Event(((String) inputLine .get(0)).split(","));

        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        epService.getEPRuntime().sendEvent(incomingEvent);
    }
}
