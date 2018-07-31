package org.etri.slice.commons.device.mqtt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class MqttReply {
	
	private String method;
	private Object reply;
}
