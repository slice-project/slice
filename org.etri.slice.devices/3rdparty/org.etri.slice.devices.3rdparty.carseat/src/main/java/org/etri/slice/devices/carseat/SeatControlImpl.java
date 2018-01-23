/**
 *
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The SLICE components
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The SLICE components; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.etri.slice.devices.carseat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

@Component(publicFactory=false, immediate=true)
@Provides
//@Instantiate
public class SeatControlImpl implements SeatControl {
	
	private static Logger s_logger = LoggerFactory.getLogger(SeatControlImpl.class);
	private static final String fileName = "workspace/save_position.txt";
	
	private static final int MAX_HEIGHT = 44;
	private static final int MAX_POSITION = 240;
	private static final int MAX_TILT = 65;
	private static final int MIN_VALUE = 0;
	
	private static final int UP_CAL = 48;
	private static final int DOWN_CAL = 70;
	private static final int LEFT_CAL = 47;
	private static final int RIGHT_CAL = 42;
	private static final int FRONT_CAL = 89;
	private static final int REAR_CAL = 87;
	
	GpioController gpio;

	//A0,B0,C0,PWM0
	GpioPinDigitalOutput UDAGP, DDAGP, UDPGP;
	GpioPinDigitalInput UDCGP;
	
	//A1,B1,C1,PWM1
	GpioPinDigitalOutput LDAGP, RDAGP, LRPGP;
	GpioPinDigitalInput LRCGP;
	
	//A2,B2,C2,PWM2
	GpioPinDigitalOutput BFDAGP, BRDAGP, BFRPGP;
	GpioPinDigitalInput BFRCGP;
	
	//Static Controled
	GpioPinDigitalInput USGP, DSGP, LSGP, RSGP, BFSGP, BRSGP;
	
	UP_DOWN_MODE up_down_mode = UP_DOWN_MODE.NONE;
	LEFT_RIGHT_MODE left_right_mode = LEFT_RIGHT_MODE.NONE;
	FRONT_REAR_MODE front_rear_mode = FRONT_REAR_MODE.NONE;
	
	STATIC_REMOTE_MODE static_remote_mode = STATIC_REMOTE_MODE.NONE;
	
	
	SeatPosture seatPosture = null;
	
	long currentTime, oldTime;
	
	public enum FRONT_REAR_MODE{
		FRONT_STATIC,
		FRONT_REMOTE,
		REAR_STATIC,
		REAR_REMOTE,
		NONE
	}
	
	public enum UP_DOWN_MODE{
		UP_STATIC,
		UP_REMOTE,
		DOWN_STATIC,
		DOWN_REMOTE,
		NONE
	}
	
	public enum LEFT_RIGHT_MODE{
		LEFT_STATIC,
		LEFT_REMOTE,
		RIGHT_STATIC,
		RIGHT_REMOTE,
		NONE
	}
	
	public enum STATIC_REMOTE_MODE{
		STATIC,
		REMOTE,
		NONE
	}
	
	public SeatControlImpl() {
		s_logger.info("Gpio Init!!");
		gpioInit();
	}
	
	@Override
	public double getHeight() {
		return seatPosture.getHeight();
	}

	//Height Remote Control
	@Override
	public void setHeight(double height) {
		static_remote_mode = STATIC_REMOTE_MODE.REMOTE;
//		int calSleep = 0;
		double gapValue = 0.0;
		if(seatPosture.getHeight() < height) { //Down to Up
			gapValue = height - seatPosture.getHeight();
			gapValue = gapValue * (UP_CAL/0.4);
//			up_down_mode = UP_DOWN_MODE.UP_REMOTE;
			s_logger.info("Height calSleep : "+gapValue);
			directControl(1, 0, UDAGP, DDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			UDPGP.low();
		}else if(seatPosture.getHeight() > height) { //Up to Down
			gapValue = seatPosture.getHeight() - height;
			gapValue = gapValue * (UP_CAL/0.4);
//			up_down_mode = UP_DOWN_MODE.DOWN_REMOTE;
			s_logger.info("Height calSleep : "+gapValue);
			directControl(2, 0, UDAGP, DDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			UDPGP.low();
		}
		seatPosture.setHeight(height); 
		savePosition();
		static_remote_mode = STATIC_REMOTE_MODE.STATIC;
	}

	//Position Remote Control
	@Override
	public double getPosition() {
		return seatPosture.getPosition();
	}

	@Override
	public void setPosition(double position) {
		static_remote_mode = STATIC_REMOTE_MODE.REMOTE;
		double gapValue = 0.0;
		if(seatPosture.getPosition() < position) { //Left to Right
			gapValue = position - seatPosture.getPosition();
			gapValue = gapValue * RIGHT_CAL;
//			left_right_mode = LEFT_RIGHT_MODE.LEFT_REMOTE;
			s_logger.info("Position calSleep : "+gapValue);
			directControl(1, 1, LDAGP, RDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LRPGP.low();
		}else if(seatPosture.getPosition() > position) { //Right to Left
			gapValue = seatPosture.getPosition() - position;
			gapValue = gapValue * LEFT_CAL;
//			left_right_mode = LEFT_RIGHT_MODE.RIGHT_REMOTE;
			s_logger.info("Position calSleep : "+gapValue);
			directControl(2, 1, LDAGP, RDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LRPGP.low();
		}
		seatPosture.setPosition(position);
		savePosition();
		static_remote_mode = STATIC_REMOTE_MODE.STATIC;
	}

	@Override
	public double getTilt() {
		seatPosture.getTilt();
		return 0;
	}

	//Tilt Remote Control
	@Override
	public void setTilt(double tilt) {
		static_remote_mode = STATIC_REMOTE_MODE.REMOTE;
		double gapValue = 0.0;
		if(seatPosture.getTilt() < tilt) { //Down to Up
			gapValue = tilt - seatPosture.getTilt();
			gapValue = gapValue * (REAR_CAL / 0.35);
//			front_rear_mode = FRONT_REAR_MODE.REAR_REMOTE;
			s_logger.info("Tilt calSleep : "+gapValue);
			directControl(1, 2, BFDAGP, BRDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BFRPGP.low();
		}else if(seatPosture.getTilt() > tilt) { //Up to Down
			gapValue = seatPosture.getTilt() - tilt;
			gapValue = gapValue * (FRONT_CAL / 0.35);
//			front_rear_mode = FRONT_REAR_MODE.FRONT_REMOTE;
			s_logger.info("Tilt calSleep : "+gapValue);
			directControl(2, 2, BFDAGP, BRDAGP);
			try {
				Thread.sleep((int)gapValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BFRPGP.low();
		}
		seatPosture.setTilt(tilt);
		savePosition();
		static_remote_mode = STATIC_REMOTE_MODE.STATIC;
	}

	@Override
	public SeatPosture getPosture() {
		return seatPosture;
	}

	@Override
	public void setPosture(SeatPosture posture) {
		s_logger.info("Set Height");
		setHeight(posture.getHeight());
		s_logger.info("Set Position");
		setPosition(posture.getPosition());
		s_logger.info("Set Tilt");
		setTilt(posture.getTilt());
	}	
	
	private void gpioInit() {
		
		loadPosition();
		
		static_remote_mode = STATIC_REMOTE_MODE.STATIC;
		
		gpio = GpioFactory.getInstance();
		
		UDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW); //up, direct, A, Gpio, Pin, GPIO_17
		DDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, PinState.LOW); //down, direct, A, Gpio, Pin, GPIO_15
		UDCGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN); //up, down, CS, Gpio, Pin, GPIO_27
		UDPGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW); //up, down, PWM, Gpio, Pin, GPIO_18
		
		LDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, PinState.LOW); //left, direct, A, Gpio, Pin, GPIO_16
		RDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.LOW); //right, direct, A, Gpio, Pin, GPIO_26
		LRCGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN); //up, right, down, CS, Gpio, Pin, GPIO_20
		LRPGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW); //up, right, down, PWM, Gpio, Pin, GPIO_19
		
		BFDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW); //back, Forward, direct, A, Gpio, Pin, GPIO_05
		BRDAGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.LOW); //back, Rear, direct, A, Gpio, Pin, GPIO_06
		BFRCGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_DOWN); //back, Forward, Rear, CS, Gpio, Pin, GPIO_13
		BFRPGP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, PinState.LOW); //back, Forward, Rear, PWM, Gpio, Pin, GPIO_12
		
		USGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_UP); //up, Setp, Gpio, Pin, GPIO_04
		DSGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, PinPullResistance.PULL_UP); //GPIO_14
		LSGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP); //left, Step, Gpio, Pin, GPIO_22
		RSGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP); //GPIO_23
		BFSGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_UP); //back, Forward, Step, Gpio, Pin, GPIO_24
		BRSGP = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, PinPullResistance.PULL_UP); //back, Rear, Step, GPIO, Pin, GPIO_10
		
		//Up, Down CS callback
		UDCGP.setShutdownOptions(true);
		UDCGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				try {
					Thread.sleep(1000);
					if(UDCGP.getState() == PinState.LOW) {
						UDPGP.low();
						if(up_down_mode == UP_DOWN_MODE.UP_REMOTE || up_down_mode == UP_DOWN_MODE.UP_STATIC) {
							seatPosture.setHeight(MAX_HEIGHT);
							s_logger.info("CS Pin UP_MODE");
						}else if (up_down_mode == UP_DOWN_MODE.DOWN_REMOTE || up_down_mode == UP_DOWN_MODE.DOWN_STATIC) {
							seatPosture.setHeight(MIN_VALUE);
							s_logger.info("CS Pin DOWN_MODE");
						}
						savePosition();
						up_down_mode = UP_DOWN_MODE.NONE;
					}else{
						s_logger.info("Up Down Fake CS!!");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//Left, Right CS callback
		LRCGP.setShutdownOptions(true);
		LRCGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				try {
					Thread.sleep(50);
					if(LRCGP.getState() == PinState.LOW) {
						LRPGP.low();
						if(left_right_mode == LEFT_RIGHT_MODE.LEFT_REMOTE || left_right_mode == LEFT_RIGHT_MODE.LEFT_STATIC) {
							seatPosture.setPosition(MAX_POSITION);
							s_logger.info("CS Pin LEFT_MODE");
						}else if (left_right_mode == LEFT_RIGHT_MODE.RIGHT_REMOTE || left_right_mode == LEFT_RIGHT_MODE.RIGHT_STATIC) {
							seatPosture.setPosition(MIN_VALUE);
							s_logger.info("CS Pin RIGHT_MODE");
						}
						savePosition();
						left_right_mode = LEFT_RIGHT_MODE.NONE;
					}else{
						s_logger.info("Left Right Fake CS!!");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//Front, Rear CS callback
		BFRCGP.setShutdownOptions(true);
		BFRCGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				try {
					Thread.sleep(50);
					if(BFRCGP.getState() == PinState.LOW) {
						BFRPGP.low();
						if(front_rear_mode == FRONT_REAR_MODE.FRONT_REMOTE || front_rear_mode == FRONT_REAR_MODE.FRONT_STATIC) {
							seatPosture.setTilt(MIN_VALUE);
							s_logger.info("CS Pin FRONT_MODE");
						}else if (front_rear_mode == FRONT_REAR_MODE.REAR_REMOTE || front_rear_mode == FRONT_REAR_MODE.REAR_STATIC) {
							seatPosture.setTilt(MAX_TILT);
							s_logger.info("CS Pin REAR_MODE");
						}
						savePosition();
						front_rear_mode = FRONT_REAR_MODE.NONE;
					}else{
						s_logger.info("Front Rear Fake CS!!");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		//UP Button callback
		USGP.setShutdownOptions(true);
		USGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(USGP.getState() == PinState.HIGH) {
						s_logger.info("Detect UP pin Stop!!");
						UDPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(up_down_mode == UP_DOWN_MODE.UP_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setHeight(seatPosture.getHeight() + ((gapValue / UP_CAL) * 0.4));
							savePosition();
							up_down_mode = UP_DOWN_MODE.NONE;
						}
					}else {
						if(up_down_mode == UP_DOWN_MODE.NONE) {
							s_logger.info("Detect UP pin Start!!");
							up_down_mode = UP_DOWN_MODE.UP_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(1, 0, UDAGP, DDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
		
		//Down Button callback
		DSGP.setShutdownOptions(true);
		DSGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(DSGP.getState() == PinState.HIGH) {
						s_logger.info("Detect Down pin Stop!!");
						UDPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(up_down_mode == UP_DOWN_MODE.DOWN_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setHeight(seatPosture.getHeight() - ((gapValue / DOWN_CAL) * 0.4));
							savePosition();
							up_down_mode = UP_DOWN_MODE.NONE;
						}
					}else {
						if(up_down_mode == UP_DOWN_MODE.NONE ) {
							s_logger.info("Detect Down pin Start!!");
							up_down_mode = UP_DOWN_MODE.DOWN_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(2, 0, UDAGP, DDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
		
		//LEFT Button callback
		LSGP.setShutdownOptions(true);
		LSGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(LSGP.getState() == PinState.HIGH) {
						s_logger.info("Detect LEFT pin Stop!!");
						LRPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(left_right_mode == LEFT_RIGHT_MODE.LEFT_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setPosition(seatPosture.getPosition() + (gapValue / LEFT_CAL));
							savePosition();
							left_right_mode = LEFT_RIGHT_MODE.NONE;
						}
					}else {
						if(left_right_mode == LEFT_RIGHT_MODE.NONE ) {
							s_logger.info("Detect LEFT pin Start!!");
							left_right_mode = LEFT_RIGHT_MODE.LEFT_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(1, 1, LDAGP, RDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
		
		//RIGHT Button callback
		RSGP.setShutdownOptions(true);
		RSGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(RSGP.getState() == PinState.HIGH) {
						s_logger.info("Detect RIGHT pin Stop!!");
						LRPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(left_right_mode == LEFT_RIGHT_MODE.RIGHT_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setPosition(seatPosture.getPosition() - (gapValue / RIGHT_CAL));
							savePosition();
							left_right_mode = LEFT_RIGHT_MODE.NONE;
						}
					}else {
						if(left_right_mode == LEFT_RIGHT_MODE.NONE) {
							s_logger.info("Detect RIGHT pin Start!!");
							left_right_mode = LEFT_RIGHT_MODE.RIGHT_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(2, 1, LDAGP, RDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
		
		//FRONT Button callback
		BFSGP.setShutdownOptions(true);
		BFSGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(BFSGP.getState() == PinState.HIGH) {
						s_logger.info("Detect FRONT pin Stop!!");
						BFRPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(front_rear_mode == FRONT_REAR_MODE.FRONT_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setTilt(seatPosture.getTilt() - ((gapValue / FRONT_CAL) * 0.35));
							savePosition();
							front_rear_mode = FRONT_REAR_MODE.NONE;
						}
					}else {
						if(front_rear_mode == FRONT_REAR_MODE.NONE) {
							s_logger.info("Detect FRONT pin Start!!");
							front_rear_mode = FRONT_REAR_MODE.FRONT_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(1, 2, BFDAGP, BRDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
		
		//REAR Button callback
		BRSGP.setShutdownOptions(true);
		BRSGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				if(static_remote_mode == STATIC_REMOTE_MODE.STATIC) {
					if(BRSGP.getState() == PinState.HIGH) {
						s_logger.info("Detect REAR pin Stop!!");
						BFRPGP.low();
						currentTime = System.currentTimeMillis();
						s_logger.info("Check Gap : " + (currentTime - oldTime));
						if(front_rear_mode == FRONT_REAR_MODE.REAR_STATIC) {
							long gapValue = currentTime - oldTime;
							seatPosture.setTilt(seatPosture.getTilt() + ((gapValue / REAR_CAL) * 0.35));
							savePosition();
							front_rear_mode = FRONT_REAR_MODE.NONE;
						}
					}else {
						if(front_rear_mode == FRONT_REAR_MODE.NONE) {
							s_logger.info("Detect FRONT pin Start!!");
							front_rear_mode = FRONT_REAR_MODE.REAR_STATIC;
							oldTime = System.currentTimeMillis();
							directControl(2, 2, BFDAGP, BRDAGP);
						}
					}
				}else if(static_remote_mode == STATIC_REMOTE_MODE.REMOTE) {
					s_logger.info("REMOTE MODE !!");
				}
			}
		});
	}
	
	private void directControl(int dir, int P, GpioPinDigitalOutput A, GpioPinDigitalOutput B) {
		if(dir == 1) {
			A.setState(PinState.HIGH);
			B.setState(PinState.LOW);
		}else if(dir == 2) {
			A.setState(PinState.LOW);
			B.setState(PinState.HIGH);
		}
		
		if(P == 0) {
			UDPGP.high();
		}else if( P == 1) {
			LRPGP.high();
		}else if ( P == 2) {
			BFRPGP.high();
		}
	}
	
	private void savePosition() {
		if(seatPosture.getPosition() > MAX_POSITION) {
			seatPosture.setPosition(MAX_POSITION);
		}else if(seatPosture.getPosition() < MIN_VALUE) {
			seatPosture.setPosition(MIN_VALUE);
		}
		
		if(seatPosture.getHeight() > MAX_HEIGHT) {
			seatPosture.setHeight(MAX_HEIGHT);
		}else if(seatPosture.getPosition() < MIN_VALUE) {
			seatPosture.setHeight(MIN_VALUE);
		}
		
		if(seatPosture.getTilt() > MAX_TILT) {
			seatPosture.setTilt(MAX_TILT);
		}else if(seatPosture.getTilt() < MIN_VALUE) {
			seatPosture.setTilt(MIN_VALUE);
		}
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			
			out.write(Double.toString(seatPosture.getHeight()));
			out.newLine();
			out.write(Double.toString(seatPosture.getPosition()));
			out.newLine();
			out.write(Double.toString(seatPosture.getTilt()));
			out.close();
			s_logger.info("Seat Position Save : "+ seatPosture.getHeight() + ", "+seatPosture.getPosition()+", "+seatPosture.getTilt());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println("SAVE DATA OK !!");
	}
	
	
	private void loadPosition() {
		seatPosture = new SeatPosture(0,0,0);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			if((str = in.readLine()) !=null) {
				seatPosture.setHeight(Double.parseDouble(str));
			}
			if((str = in.readLine()) !=null) {
				seatPosture.setPosition(Double.parseDouble(str));
			}
			if((str = in.readLine()) !=null) {
				seatPosture.setTilt(Double.parseDouble(str));
			}
			in.close();
			s_logger.info("Seat Position Load : "+ seatPosture.getHeight() + ", "+seatPosture.getPosition()+", "+seatPosture.getTilt());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
