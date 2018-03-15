package org.usfirst.frc.team6203.robot.subsystems;

import org.usfirst.frc.team6203.robot.OI;
import org.usfirst.frc.team6203.robot.Robot;
import org.usfirst.frc.team6203.robot.RobotMap;
import org.usfirst.frc.team6203.robot.commands.Drive;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Chassis extends Subsystem {

	//Drive constants
	private final double root2 = Math.sqrt(2);
	private final double sin135 = root2 / 2;
	private final double cos135 = -root2 / 2;

	public Chassis() {

	}

	public void initDefaultCommand() {
	}

	public void tankDrive(double a, double b) { //TAKE OUT 0.5 LATER
		SmartDashboard.putString("type", "tank");
		
		SmartDashboard.putNumber("axisY1", a*0.5);
		SmartDashboard.putNumber("axisY2", b*0.5);

		Robot.drive.tankDrive(a*0.5, b*0.5);
	}

	public void straightDrive(double speed) {
		double Kp = 0.03;
		Robot.imu.reset();
		Robot.drive.tankDrive(speed - Kp*Robot.imu.getAngle(), speed);
	}
	
	public void arcadeDrive() {
		SmartDashboard.putString("type", "arcade");
		
		double yspeed = Robot.oi.driverStick.getY();
		double xspeed = Robot.oi.driverStick.getX();
		
		double b = cos135 * xspeed - sin135 * yspeed;
		double a = sin135 * xspeed + cos135 * yspeed;

		SmartDashboard.putNumber("magnitude", a + b);
		SmartDashboard.putNumber("direction", a - b);

		// test 0 - ???
		//drive.arcadeDrive((a + b) / (Drive.slow ? 2 : 1), (a - b) / (Drive.slow ? 2 : 1));

		// test 1 - correct way to do single joystick driving
//		Robot.drive.tankDrive(yspeed + xspeed, yspeed - xspeed);

		// test 2 - scaling may be needed
		Robot.drive.tankDrive((yspeed + xspeed) / 1.5, (yspeed - xspeed) / 1.5);
	}

}
