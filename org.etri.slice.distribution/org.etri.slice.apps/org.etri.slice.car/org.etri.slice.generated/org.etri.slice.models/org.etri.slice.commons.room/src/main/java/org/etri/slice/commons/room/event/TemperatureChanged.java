package org.etri.slice.commons.room.event;

import org.etri.slice.commons.SliceContext;
import org.etri.slice.commons.SliceEvent;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.etri.slice.commons.room.context.Temperature;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper=false)

@Role(Role.Type.EVENT)
@SliceContext
public class TemperatureChanged extends SliceEvent {

	public static final String TOPIC = "temperate_changed";
	private static final long serialVersionUID = -7685719531233393802L;
	
	private Temperature temp;
}
