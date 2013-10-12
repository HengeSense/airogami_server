package com.airogami.common;

import com.airogami.persistence.entities.AccountStat;
import com.airogami.persistence.entities.Agent;

public class ClientAgent {

	public static final byte DeviceTypeIOS = 1;
	public static final byte DeviceTypeAndroid = 2;
	private byte devType;
	private byte devVersion;
	private String devToken;

	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte getDevVersion() {
		return devVersion;
	}

	public void setDevVersion(byte devVersion) {
		this.devVersion = devVersion;
	}

	public String getDevToken() {
		return devToken;
	}

	public void setDevToken(String devToken) {
		this.devToken = devToken;
	}

	public boolean equals(Object o) {
		if (o instanceof ClientAgent) {
			ClientAgent clientAgent = (ClientAgent) o;
			return devType == clientAgent.devType
					&& devVersion == clientAgent.devVersion
					&& (devToken == null && clientAgent.devToken == null
							|| devToken != null && devToken
								.equals(clientAgent.devToken));
		} else if (o instanceof Agent) {
			Agent agent = (Agent) o;
			return devType == agent.getDevType()
					&& devVersion == agent.getDevVersion()
					&& (devToken == null && agent.getDevToken() == null
							|| devToken != null && devToken
								.equals(agent.getDevToken()));
		}
		return false;
	}

}
