package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToSetpoint extends Command {

	double target;
	double curr_angle;
	boolean left = false;
	double leftSpeed = 0.8;
	double rightSpeed = 0.8;

	public TurnToSetpoint(double angle) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.target = angle;
		if (angle > 180 || angle < 0)
			left = true;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.imu.reset();
		Robot.imu.calibrate();
		curr_angle = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (isFinished())
			Robot.drive.tankDrive(0, 0);
		else {
			if (left)
				Robot.drive.tankDrive(-leftSpeed, rightSpeed);
			else
				Robot.drive.tankDrive(leftSpeed, -rightSpeed);
		}
		curr_angle = Robot.imu.getAngleZ();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Math.abs(curr_angle - target) < 0.2;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
