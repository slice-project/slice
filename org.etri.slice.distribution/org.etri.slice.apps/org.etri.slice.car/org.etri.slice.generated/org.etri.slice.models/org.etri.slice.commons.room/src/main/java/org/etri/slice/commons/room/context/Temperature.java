package org.etri.slice.commons.room.context;

import org.etri.slice.commons.SliceContext;
import org.etri.slice.commons.internal.ContextBase;
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
public class Temperature  implements ContextBase {
	public static class Field {
		public static final String value = "Temperature.value";
	}
	
	public static final String dataType = "org.etri.slice.commons.room.context.Temperature";
	public static final String topic = "temperature";
	public static final String dataKey = "dataKey:" + dataType;
	
	private int value;
}
