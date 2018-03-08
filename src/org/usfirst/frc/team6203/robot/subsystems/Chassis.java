package org.usfirst.frc.team6203.robot.subsystems;

import org.usfirst.frc.team6203.robot.OI;
import org.usfirst.frc.team6203.robot.Robot;
import org.usfirst.frc.team6203.robot.commands.Drive;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Chassis extends Subsystem {

	public static Victor elv;

	public static Victor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;

	public static SpeedControllerGroup m_left, m_right;

	public static DifferentialDrive drive;

	private final double root2 = Math.sqrt(2);
	private final double sin135 = root2 / 2;
	private final double cos135 = -root2 / 2;
	private final double slow_multiplier = 0.6;

	public Chassis() {

		leftFrontMotor = new Victor(0);
		leftBackMotor = new Victor(1);
		rightFrontMotor = new Victor(2);
		rightBackMotor = new Victor(3);

		m_left = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
		m_right = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

		drive = new DifferentialDrive(m_left, m_right);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new Drive());
	}

	public void tankDrive() {
		double a = OI.driverStick.getY();
		double b = OI.elevatorStick.getY();

		SmartDashboard.putNumber("axisY1", a);
		SmartDashboard.putNumber("axisY2", b);

		drive.tankDrive(a, b);
	}

	public void arcadeDrive() {
		SmartDashboard.putString("type", "tank");
		double yspeed = Robot.oi.driverStick.getY();
		double xspeed = Robot.oi.driverStick.getX();
		double b = cos135 * xspeed - sin135 * yspeed;
		double a = sin135 * xspeed + cos135 * yspeed;

		SmartDashboard.putNumber("magnitude", a + b);
		SmartDashboard.putNumber("direction", a - b);

		// test 0 - ???
		//drive.arcadeDrive((a + b) / (Drive.slow ? 2 : 1), (a - b) / (Drive.slow ? 2 : 1));

		// test 1 - correct way to do single joystick driving
		drive.tankDrive(yspeed + xspeed, yspeed - xspeed);

		// test 2 - scaling may be needed
		//drive.tankDrive((yspeed + xspeed) / 2, (yspeed - xspeed) / 2);
	}

}
