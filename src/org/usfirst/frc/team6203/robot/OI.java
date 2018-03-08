package org.usfirst.frc.team6203.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public static Joystick driverStick, elevatorStick;

	public OI() {
		driverStick = new Joystick(RobotMap.controller);
		elevatorStick = new Joystick(RobotMap.controller2);
	}

}
