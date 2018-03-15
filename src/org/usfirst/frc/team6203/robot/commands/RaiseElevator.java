package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class RaiseElevator extends TimedCommand {

	double speed;
	boolean up;

	public RaiseElevator(double timeout, double speed, boolean up) {
		super(timeout);
		this.speed = speed;
		this.up = up;
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.elevator.setElevator(up ? speed : -speed);
	}

	// Called once after timeout
	protected void end() {
		Robot.elevator.setElevator(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
