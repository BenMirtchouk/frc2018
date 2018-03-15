package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class DriveStraightTimed extends TimedCommand {

	double speed;
	final double Kp = 0.03;
	final double tolerance = 0.5;

	public DriveStraightTimed(double timeout, double s) {
		super(timeout);
		speed = s;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.imu.reset();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// implement pid control
		double t = Robot.imu.getAngle();
		double c = Kp * (Math.abs(t) < tolerance ? 0 : t);
		Robot.drive.tankDrive(speed - c, speed);
	}

	// Called once after timeout
	protected void end() {
		Robot.drive.tankDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
