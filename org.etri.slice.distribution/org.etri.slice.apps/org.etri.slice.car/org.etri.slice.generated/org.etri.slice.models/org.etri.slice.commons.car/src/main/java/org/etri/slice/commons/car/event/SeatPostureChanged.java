package org.etri.slice.commons.car.event;

import org.etri.slice.commons.SliceContext;
import org.etri.slice.commons.SliceEvent;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.etri.slice.commons.car.context.SeatPosture;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper=false)

@Role(Role.Type.EVENT)
@SliceContext
public class SeatPostureChanged extends SliceEvent {

	public static final String TOPIC = "seat_posture_changed";
	private static final long serialVersionUID = -2907746216334373110L;
	
	private SeatPosture posture;
}
