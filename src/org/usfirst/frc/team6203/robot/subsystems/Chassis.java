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

	public Chassis() {

	}

	public void initDefaultCommand() {
	}

	public void arcadeDrive() {
		double yspeed = Robot.oi.driverStick.getY();
		double xspeed = Robot.oi.driverStick.getX();
		
		Robot.drive.tankDrive((yspeed + xspeed) / 2, (yspeed - xspeed) / 2);
	}

}
