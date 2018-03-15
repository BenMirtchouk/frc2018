package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightToSetpoint extends Command {

	int distance;
	double speed;
	
	public DriveStraightToSetpoint(int distance, double speed) {
		this.distance = distance;
		this.speed = speed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.left_PID_controller.setSetpoint(distance);
		Robot.right_PID_controller.setSetpoint(distance);
		Robot.left_PID_controller.enable();
		Robot.right_PID_controller.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
			Robot.drive.tankDrive(speed, speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.left_PID_controller.onTarget() && Robot.right_PID_controller.onTarget();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.tankDrive(0, 0);
		Robot.left_encoder.reset();
		Robot.right_encoder.reset();
		Robot.left_PID_controller.reset();
		Robot.right_PID_controller.reset();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
