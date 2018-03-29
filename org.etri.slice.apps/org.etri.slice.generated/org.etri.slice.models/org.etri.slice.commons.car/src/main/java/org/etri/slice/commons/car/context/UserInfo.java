package org.etri.slice.commons.car.context;

import org.etri.slice.commons.SliceContext;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.etri.slice.commons.car.context.BodyPartLength;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Role(Role.Type.EVENT)
@SliceContext
public class UserInfo {
	public static class Field {
		public static final String userId = "UserInfo.userId";
		public static final String bodyLength = "UserInfo.bodyLength";
	}
	
	public static final String dataType = "org.etri.slice.commons.car.context.UserInfo";
	public static final String topic = "userinfo";
	public static final String dataKey = "dataKey:" + dataType;
	
	private String userId;
	private BodyPartLength bodyLength;
}
