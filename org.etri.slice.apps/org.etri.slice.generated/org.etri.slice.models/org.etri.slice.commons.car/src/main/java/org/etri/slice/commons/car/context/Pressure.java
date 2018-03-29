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
public class Pressure {
	public static class Field {
		public static final String value = "Pressure.value";
	}
	
	public static final String dataType = "org.etri.slice.commons.car.context.Pressure";
	public static final String topic = "pressure";
	public static final String dataKey = "dataKey:" + dataType;
	
	private double value;
}
