/*
 * $Header$
 * $Revision$
 * $Date$
 * ------------------------------------------------------------------------------------------------------
 *
 * Copyright (c) SymphonySoft Limited. All rights reserved.
 * http://www.symphonysoft.com
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.mule.management.mbeans;

import org.mule.management.stats.ComponentStatistics;
import org.mule.management.stats.RouterStatistics;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * <code>ComponentStats</code> TODO
 *
 * @author Guillaume Nodet
 * @version $Revision$
 */
public class ComponentStats implements ComponentStatsMBean, MBeanRegistration {

	private MBeanServer server;

	private ObjectName name;
	private ObjectName inboundName;
	private ObjectName outboundName;
	
	private ComponentStatistics statistics;
	
	public ComponentStats(ComponentStatistics statistics) {
		this.statistics = statistics; 
	}
	
	/**
	 * 
	 */
	public void clear() {
		statistics.clear();
	}
	
	/**
	 * @return
	 */
	public long getAsyncEventsReceived() {
		return statistics.getAsyncEventsReceived();
	}
	/**
	 * @return
	 */
	public long getAsyncEventsSent() {
		return statistics.getAsyncEventsSent();
	}
	/**
	 * @return
	 */
	public long getAverageExecutionTime() {
		return statistics.getAverageExecutionTime();
	}
	/**
	 * @return
	 */
	public long getAverageQueueSize() {
		return statistics.getAverageQueueSize();
	}
	/**
	 * @return
	 */
	public long getExecutedEvents() {
		return statistics.getExecutedEvents();
	}
	/**
	 * @return
	 */
	public long getExecutionErrors() {
		return statistics.getExecutionErrors();
	}
	/**
	 * @return
	 */
	public long getFatalErrors() {
		return statistics.getFatalErrors();
	}
	/**
	 * @return
	 */
	public long getMaxExecutionTime() {
		return statistics.getMaxExecutionTime();
	}
	/**
	 * @return
	 */
	public long getMaxQueueSize() {
		return statistics.getMaxQueueSize();
	}
	/**
	 * @return
	 */
	public long getMinExecutionTime() {
		return statistics.getMinExecutionTime();
	}
	/**
	 * @return
	 */
	public String getName() {
		return statistics.getName();
	}
	/**
	 * @return
	 */
	public long getQueuedEvents() {
		return statistics.getQueuedEvents();
	}
	/**
	 * @return
	 */
	public long getReplyToEventsSent() {
		return statistics.getReplyToEventsSent();
	}
	/**
	 * @return
	 */
	public long getSyncEventsReceived() {
		return statistics.getSyncEventsReceived();
	}
	/**
	 * @return
	 */
	public long getSyncEventsSent() {
		return statistics.getSyncEventsSent();
	}
	/**
	 * @return
	 */
	public long getTotalEventsReceived() {
		return statistics.getTotalEventsReceived();
	}
	/**
	 * @return
	 */
	public long getTotalEventsSent() {
		return statistics.getTotalEventsSent();
	}
	/**
	 * @return
	 */
	public long getTotalExecutionTime() {
		return statistics.getTotalExecutionTime();
	}

	/* (non-Javadoc)
	 * @see javax.management.MBeanRegistration#preRegister(javax.management.MBeanServer, javax.management.ObjectName)
	 */
	public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
		this.server = server;
		this.name = name;
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	public void postRegister(Boolean registrationDone) {
		try {
			RouterStatistics is = this.statistics.getInboundRouterStat();
			if (is != null) {
				inboundName = new ObjectName(name.toString() + ",router=inbound");
				this.server.registerMBean(new RouterStats(is), this.inboundName);
			}
			RouterStatistics os = this.statistics.getOutboundRouterStat();
			if (os != null) {
				outboundName = new ObjectName(name.toString() + ",router=outbound");
				this.server.registerMBean(new RouterStats(is), this.outboundName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	public void preDeregister() throws Exception {
	}

	/* (non-Javadoc)
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	public void postDeregister() {
	}

	/* (non-Javadoc)
	 * @see org.mule.management.mbeans.ComponentStatsMBean#getInboundRouter()
	 */
	public ObjectName getRouterInbound() {
		return this.inboundName;
	}

	/* (non-Javadoc)
	 * @see org.mule.management.mbeans.ComponentStatsMBean#getOutboundRouter()
	 */
	public ObjectName getRouterOutbound() {
		return this.outboundName;
	}
}
