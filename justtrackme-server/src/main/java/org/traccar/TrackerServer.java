/*
 * Copyright 2012 - 2014 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.Properties;

import org.jboss.netty.bootstrap.Bootstrap;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracker server
 */
public abstract class TrackerServer {
	
	private static final Logger LOG = LoggerFactory.getLogger(TrackerServer.class);

    private final Bootstrap bootstrap;
    private final String protocol;
    
    private Properties properties = ContextFactory.getContext().getProperties();

    public String getProtocol() {
        return protocol;
    }

    public TrackerServer(Bootstrap bootstrap, String protocol) {
        this.bootstrap = bootstrap;
        this.protocol = protocol;

        // Set appropriate channel factory
        if (bootstrap instanceof ServerBootstrap) {
            bootstrap.setFactory(GlobalChannelFactory.getFactory());
        } else if (bootstrap instanceof ConnectionlessBootstrap) {
            bootstrap.setFactory(GlobalChannelFactory.getDatagramFactory());
        }

        address = getProperties().getProperty(protocol + ".address");
        String portProperty = getProperties().getProperty(protocol + ".port");
        port = Integer.valueOf(portProperty);

        bootstrap.setPipelineFactory(new BasePipelineFactory(this, protocol) {
            @Override
            protected void addSpecificHandlers(ChannelPipeline pipeline) {
                TrackerServer.this.addSpecificHandlers(pipeline);
            }
        });
    }

    protected abstract void addSpecificHandlers(ChannelPipeline pipeline);

    /**
     * Server port
     */
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Server listening interface
     */
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Set endianness
     */
    void setEndianness(ByteOrder byteOrder) {
        bootstrap.setOption("bufferFactory", new HeapChannelBufferFactory(byteOrder));
        bootstrap.setOption("child.bufferFactory", new HeapChannelBufferFactory(byteOrder));
    }

    /**
     * Opened channels
     */
    private final ChannelGroup allChannels = new DefaultChannelGroup();

    public ChannelGroup getChannelGroup() {
        return allChannels;
    }

    public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
        bootstrap.setPipelineFactory(pipelineFactory);
    }

    public ChannelPipelineFactory getPipelineFactory() {
        return bootstrap.getPipelineFactory();
    }

    /**
     * Start server
     */
    public void start() {
    	LOG.info("Start server [{}] -> [{}:{}]", getProtocol(), getAddress(), getPort());    	
        InetSocketAddress endpoint;
        if (address == null) {
            endpoint = new InetSocketAddress(port);
        } else {
            endpoint = new InetSocketAddress(address, port);
        }

        Channel channel = null;
        if (bootstrap instanceof ServerBootstrap) {
            channel = ((ServerBootstrap) bootstrap).bind(endpoint);
        } else if (bootstrap instanceof ConnectionlessBootstrap) {
            channel = ((ConnectionlessBootstrap) bootstrap).bind(endpoint);
        }

        if (channel != null) {
            getChannelGroup().add(channel);
        }
    }

    /**
     * Stop server
     */
    public void stop() {
    	LOG.info("Stop server [{}]: [{}:{}]", getProtocol(), getAddress(), getPort());
    	
        ChannelGroupFuture future = getChannelGroup().close();
        future.awaitUninterruptibly();
    }

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
