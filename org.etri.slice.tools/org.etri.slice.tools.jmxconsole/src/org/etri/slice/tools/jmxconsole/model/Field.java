package org.etri.slice.tools.jmxconsole.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * key, value 형식의 데이터를 위한 모델, 테이블 Input 데이터로 사용함
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
public class Field {
	private String key;
	private Object value;
}
