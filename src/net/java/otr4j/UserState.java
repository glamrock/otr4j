/*
 * otr4j, the open source java otr library.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

package net.java.otr4j;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.otr4j.context.*;

/**
 * 
 * @author George Politis
 * 
 */
public final class UserState {
	public UserState(OTR4jListener listener) {
		this.setListener(listener);
	}

	private OTR4jListener listener;
	private Vector<ConnContext> contextPool;
	private static Logger logger = Logger
			.getLogger(ConnContext.class.getName());

	private ConnContext getConnContext(String user, String account,
			String protocol) {

		if (Utils.IsNullOrEmpty(user) || Utils.IsNullOrEmpty(account)
				|| Utils.IsNullOrEmpty(protocol)) {
			throw new IllegalArgumentException();
		}

		for (ConnContext connContext : getContextPool()) {
			if (connContext.getAccount().equals(account)
					&& connContext.getUser().equals(user)
					&& connContext.getProtocol().equals(protocol)) {
				return connContext;
			}
		}

		ConnContext context = new ConnContext(user, account, protocol,
				getListener());
		getContextPool().add(context);

		return context;
	}

	public String handleReceivingMessage(String user, String account,
			String protocol, String msgText) throws Exception {

		try {
			return this.getConnContext(user, account, protocol)
					.handleReceivingMessage(msgText);
		} catch (Exception e) {
			logger
					.log(
							Level.SEVERE,
							"Handling message receiving failed, returning original message.",
							e);
			return msgText;
		}
	}

	public String handleSendingMessage(String user, String account,
			String protocol, String msgText) {

		try {
			return this.getConnContext(user, account, protocol)
					.handleSendingMessage(msgText);
		} catch (Exception e) {
			logger
					.log(
							Level.SEVERE,
							"Handling message sending failed, returning original message.",
							e);
			return msgText;
		}
	}

	private void setListener(OTR4jListener listener) {
		this.listener = listener;
	}

	private OTR4jListener getListener() {
		return listener;
	}

	private Vector<ConnContext> getContextPool() {
		if (contextPool == null)
			contextPool = new Vector<ConnContext>();
		return contextPool;
	}
}
