package org.etri.slice.tools.adl.project.wizards;

public class ADLSampleContstants {	
	public static final String SIMPLE_ADL_CONTENTS_WITH_DOMAIN = 
			"\ndomain {0}\n" + 
			"'{'\n" + 
			"\n\n" + 
			"}"; 
	
	public static final String SAMPLE_ADL_CONTENTS = 
	"import org.etri.slice.commons.SliceException\n" + 
	"import org.etri.slice.commons.{0}.FullBodyException\n" + 
	"import org.etri.slice.commons.{0}.^context.BodyPartLength\n" + 
	"import org.etri.slice.commons.{0}.^context.ObjectInfo\n" + 
	"import org.etri.slice.commons.{0}.^context.SeatPosture\n" + 
	"import org.etri.slice.commons.{0}.^context.UserInfo\n" + 
	"\n" + 
	"domain {0} '{'\n" + 
	"	\n" + 
	"	context ObjectInfo '{'\n" + 
	"		String objectId;\n" + 
	"		double distance;		\n" + 
	"	} \n" + 
	"	 \n" + 
	"	@topic(\"object_detected\")\n" + 
	"	event ObjectDetected '{'\n" + 
	"		ObjectInfo info;\n" + 
	"	}\n" + 
	"	\n" + 
	"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
	"	agent ObjectDetector '{'\n" + 
	"	   hasRuleSet objectdetector_rules '{'\n" + 
	"	      group-id \"org.etri.slice\"\n" + 
	"	      artifact-id \"objectdetector\"\n" + 
	"	   }\n" + 
	"	\n" + 
	"	   hasBehaviors '{'\n" + 
	"	      behavior \"Object Detection Notification\"\n" + 
	"	         on ObjectInfo\n" + 
	"	         do publish ObjectDetected \n" + 
	"	      end \n" + 
	"	   }\n" + 
	"	}	\n" + 
	"	\n" + 
	"	context BodyPartLength '{'\n" + 
	"		double head;\n" + 
	"		double torso;\n" + 
	"		double arms;\n" + 
	"		double legs;\n" + 
	"		double height;\n" + 
	"	}	\n" + 
	"	\n" + 
	"    @topic(\"full_body_detected\")\n" + 
	"    event FullBodyDetected '{'\n" + 
	"   		BodyPartLength bodyLength;\n" + 
	"    }\n" + 
	"\n" + 
	"	control Startable '{'\n" + 
	"		op void start() throws SliceException;\n" + 
	"		op void stop();\n" + 
	"	}\n" + 
	"	\n" + 
	"	exception FullBodyException;\n" + 
	"	\n" + 
	"	control FullBodyDetector extends Startable '{'\n" + 
	"		op void detect(double distance) throws FullBodyException;\n" + 
	"	}\n" + 
	"	\n" + 
	"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
	"	agent FullBodyDetector '{'\n" + 
	"	   hasRuleSet fullbodydetector_rules '{'\n" + 
	"	      group-id \"org.etri.slice\"\n" + 
	"	      artifact-id \"fullbodydetector\" \n" + 
	"	   }\n" + 
	"	\n" + 
	"	   hasBehaviors '{'\n" + 
	"	      behavior \"Initiate full-body Detection\"\n" + 
	"	         on ObjectDetected\n" + 
	"	         do call FullBodyDetector.detect \n" + 
	"	      end\n" + 
	"	\n" + 
	"	      behavior \"FullBody Detection Notificiation\"\n" + 
	"	         on BodyPartLength\n" + 
	"	         do publish FullBodyDetected\n" + 
	"	      end \n" + 
	"	   }\n" + 
	"	}\n" + 
	"\n" + 
	"	context Pressure '{'\n" + 
	"		double value;\n" + 
	"	}\n" + 
	"\n" + 
	"	context UserInfo '{'\n" + 
	"		String userId;\n" + 
	"		BodyPartLength bodyLength;\n" + 
	"	}\n" + 
	"\n" + 
	"    @topic(\"user_seated\")\n" + 
	"    event UserSeated '{'\n" + 
	"   		UserInfo info;\n" + 
	"    }\n" + 
	"    \n" + 
	"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
	"	agent PressureSensor '{'\n" + 
	"	   hasRuleSet pressuresensor_rules '{'\n" + 
	"	      group-id \"org.etri.slice\"\n" + 
	"	      artifact-id \"pressuresensor\"\n" + 
	"	   }\n" + 
	"	\n" + 
	"	   hasBehaviors '{'\n" + 
	"	      behavior \"UserSeated Notification\"\n" + 
	"	         on FullBodyDetected, Pressure\n" + 
	"	         do publish UserSeated\n" + 
	"	      end\n" + 
	"	   }\n" + 
	"	}\n" + 
	"	\n" + 
	"    context SeatPosture '{'\n" + 
	"    		double height;\n" + 
	"      	double position;\n" + 
	"      	double tilt;\n" + 
	"    }\n" + 
	"\n" + 
	"    @topic(\"seat_posture_changed\")\n" + 
	"    event SeatPostureChanged '{'\n" + 
	"      	SeatPosture posture;\n" + 
	"    }\n" + 
	"	\n" + 
	"	control SeatControl '{'\n" + 
	"		double height;\n" + 
	"		double position;\n" + 
	"		double tilt;	\n" + 
	"		SeatPosture posture;		\n" + 
	"	}\n" + 
	"	\n" + 
	"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
	"	agent CarSeat '{'\n" + 
	"		hasRuleSet carseat_rules '{'\n" + 
	"			group-id \"org.etri.slice\"\n" + 
	"			artifact-id \"carseat\"\n" + 
	"		}\n" + 
	"		\n" + 
	"	    hasBehaviors '{'\n" + 
	"	      	behavior \"Insert BodyPartLength\"\n" + 
	"	         	on UserSeated\n" + 
	"	         	do no-op\n" + 
	"	     	 end\n" + 
	"	     	 behavior \"SeatPosture Change Notification\"\n" + 
	"	        		 on SeatPosture\n" + 
	"	        		 do publish SeatPostureChanged\n" + 
	"	     	 end\n" + 
	"	    }\n" + 
	"		\n" + 
	"		hasCommandsOf SeatControl '{'\n" + 
	"			command carseat_height '{'\n" + 
	"				context BodyPartLength.height\n" + 
	"				action SeatControl.setHeight\n" + 
	"			}\n" + 
	"			\n" + 
	"			command carseat_position '{'\n" + 
	"				context BodyPartLength.height\n" + 
	"				action SeatControl.setPosition\n" + 
	"			}\n" + 
	"			\n" + 
	"			command carseat_tilt '{'\n" + 
	"				context BodyPartLength.height\n" + 
	"				action SeatControl.setTilt\n" + 
	"			}\n" + 
	"		}\n" + 
	"	}\n" + 
	"}";
}
