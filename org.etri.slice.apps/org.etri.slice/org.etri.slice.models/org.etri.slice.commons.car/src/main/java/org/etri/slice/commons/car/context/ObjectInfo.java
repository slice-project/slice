package org.etri.slice.commons.car.context;

import org.etri.slice.commons.SliceContext;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Role(Role.Type.EVENT)
@SliceContext
public class ObjectInfo {
	public static class Field {
		public static final String objectId = "ObjectInfo.objectId";
		public static final String distance = "ObjectInfo.distance";
	}
	
	public static final String dataType = "org.etri.slice.commons.car.context.ObjectInfo";
	public static final String topic = "objectinfo";
	public static final String dataKey = "dataKey:" + dataType;
	
	private String objectId;
	private double distance;
}
