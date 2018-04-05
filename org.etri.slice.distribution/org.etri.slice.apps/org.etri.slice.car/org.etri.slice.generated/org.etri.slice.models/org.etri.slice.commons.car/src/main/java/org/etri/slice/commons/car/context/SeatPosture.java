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
public class SeatPosture {
	public static class Field {
		public static final String height = "SeatPosture.height";
		public static final String position = "SeatPosture.position";
		public static final String tilt = "SeatPosture.tilt";
	}
	
	public static final String dataType = "org.etri.slice.commons.car.context.SeatPosture";
	public static final String topic = "seatposture";
	public static final String dataKey = "dataKey:" + dataType;
	
	private double height;
	private double position;
	private double tilt;
}
