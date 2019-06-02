package net.ipsoft.misc;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import java.net.InetSocketAddress;

public class EsperCrashCourse {

    public static EPServiceProvider getEsperEngine() {
        return esperEngine;
    }

    private static EPServiceProvider esperEngine;

    public static void main(String[] argv) {

        initEsperEngine();

        createDaemon(9000);
    }

    protected static void initEsperEngine() {
        Configuration config = new Configuration();
        // Enable discard of events from streams with @drop
        //config.getEngineDefaults().getExecution().setPrioritized(true);
        //config.getEngineDefaults().getLogging().setAuditPattern("!!%u!!%s!!%m!!");

        // Equivalent to "import in Java" - Declares a stream
        config.addEventTypeAutoName(Event.class.getPackage().getName());

        // Setting resources for Esper Engine
        config.getEngineDefaults().getThreading().setThreadPoolInboundNumThreads(5);
        config.getEngineDefaults().getThreading().setThreadPoolInbound(true);

        esperEngine = EPServiceProviderManager.getDefaultProvider(config);
        esperEngine.initialize();

    }

    /**
     * Creates a subscriber object to a
     * @param logger name to logger object
     * @param eplStatement statement that object subscribes to
     * @return
     */
    protected static EventDeadEnd createEventSubscriber(String logger, String eplStatement) {
        EventDeadEnd subscriber = new EventDeadEnd(logger);
        esperEngine.getEPAdministrator()
                .createEPL(eplStatement)
                .setSubscriber(subscriber);
        return subscriber;
    }

    /**
    * Binds to a TCP port and create the pipeline to process line-separated events
    * @param portNumber port number to bind to
    */
    static void createDaemon(int portNumber) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                // Break event by Line
                                .addLast(new LineBasedFrameDecoder(8096,true,true))
                                // Forward event
                                .addLast(new EventProcessor());
                    }
                })
                .bind(new InetSocketAddress(portNumber));
    }
}
