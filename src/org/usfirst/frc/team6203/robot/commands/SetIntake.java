package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class SetIntake extends TimedCommand {

	double speed;
	boolean deposit;

	public SetIntake(double timeout, double s, boolean d) {
		super(timeout);
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		speed = s;
		deposit = d;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.intake.setIntakeSpeed(deposit ? -speed : speed);
	}

	// Called once after timeout
	protected void end() {
		Robot.intake.setIntakeSpeed(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
