package org.usfirst.frc.team6203.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	//Chassis motor controllers
	public static int leftMotorF = 0;
	public static int leftMotorB = 1;
	public static int rightMotorF = 2;
	public static int rightMotorB = 3;
	
	//Elevator motor controller
	public static int elevatorMotor = 4;
	
	//Intake motor controllers
	public static int intakeMotorM = 5;
	public static int intakeMotorS = 6;
	public static int intakeDropperMotor = 7;
	
	//OI
	public static int controller = 0;
	public static int controller2 = 1;
	public static int slowspeed = 2;
	
	public static int left_encoder_channelA = 0;
	public static int left_encoder_channelB = 1;
	public static int right_encoder_channelA = 2;
	public static int right_encoder_channelB = 3;
	public static int halleffect = 2;
	
	public static int ultrasonic1 = 3;
	public static int ultrasonic2 = 4;
	
	public static int DI_bottom = 0;
	public static int DI_switch = 1;
	public static int DI_top = 2;
	
	
	
	
	
}
