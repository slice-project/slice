package org.etri.slice.devices.aircon;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.eshop.TransactionEvent;

@Component
@Instantiate
public class TestDevice implements Runnable {


    private static final int DELAY = 1000;
    
    @Requires
    private WorkingMemory m_wm;

    private boolean m_end;
    
    @Property(value = "xxx")
    private String name;
    
    public void run() {
        while (!m_end) {
            try {
            	perform();
                Thread.sleep(DELAY);
            } catch (InterruptedException ie) {
                /* will recheck end */
            }
        }
    }

    /**
     * Invoke hello services
     */
    public void perform() {
    	TransactionEvent event = new TransactionEvent(Long.valueOf(1), Double.valueOf(1000));
    	Class cls = event.getClass();
    	m_wm.insert(event);
    }

    /**
     * Starting.
     */
    @Validate
    public void starting() {
        Thread thread = new Thread(this);
        m_end = false;
        thread.start();
    }

    /**
     * Stopping.
     */
    @Invalidate
    public void stopping() {
        m_end = true;
    }
}
