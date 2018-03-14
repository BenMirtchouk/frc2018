package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoRoutine extends Command {
	
	//Drive constants
	final double phase1_drive_speed = 0.8;
	final double phase2_drive_speed = 0.4;
	
	//Subsystems
	final double elevator_speed = 0.5;
	final double eject_speed = -0.8;
	
	//Timing phases
	final double phase1 = 2 - .3;
	final double phase2 = 5 - .3;
	final double phase3 = 6 - .3;
	final double phase4 = 8 - .3;
	final double phase5 = 9 - .3;

	int robot_position;
	int switch_position;
	
	boolean same_side;
	boolean first = true;

	Timer autoTimer;

	public AutoRoutine(int r, int s) {
		robot_position = r;
		switch_position = s;
		same_side = robot_position == switch_position;
		autoTimer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		if (first) {
			autoTimer.start();
			first = false;
		}

		if (same_side) { 
			if (autoTimer.get() < phase1) {
				if (autoTimer.get() < .5) {
					Robot.intake.setIntakeSpeed(.5);
				} else {
					Robot.intake.setIntakeSpeed(0);
				}
				Robot.drive.tankDrive(phase1_drive_speed, phase1_drive_speed);
			} else if (autoTimer.get() < phase2) {
				Robot.elevatorMotor.set(elevator_speed);
			} else if (autoTimer.get() < phase3) {
				Robot.elevatorMotor.set(0);
				//Turn code
			} else if (autoTimer.get() < phase4) {
				Robot.drive.tankDrive(phase2_drive_speed, phase2_drive_speed);
			} else if (autoTimer.get() < phase5) {
				Robot.drive.tankDrive(0, 0);
				Robot.intake.setIntakeSpeed(eject_speed);
			} else {
				Robot.intake.setIntakeSpeed(0);
			}

		} else {
			if (autoTimer.get() < phase1) {
				Robot.drive.tankDrive(phase1_drive_speed, phase1_drive_speed);
			} else
				Robot.drive.tankDrive(0, 0);
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
