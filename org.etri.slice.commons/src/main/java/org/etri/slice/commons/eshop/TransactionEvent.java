package org.etri.slice.commons.eshop;

import java.util.Date;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
@Expires("2h30m")

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TransactionEvent {

    
	@JsonFormat(
			shape = JsonFormat.Shape.STRING,
			pattern = "dd-MM-yyyy hh:mm:ss")    
    private Date executionTime;
    private Long customerId;
    private Double totalAmount;

    public TransactionEvent() {
        super();
    }
    
    public TransactionEvent(Long customerId, Double totalAmount) {
        super();
        this.executionTime = new Date();
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    @Override
    public String toString() {
    	return String.format("TransactionEvent[executionTime=%s, id=%d, amount=%3.2f]", executionTime, customerId, totalAmount);
    }
}

