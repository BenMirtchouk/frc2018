package org.usfirst.frc.team6203.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TimedAutoRoutine2 extends CommandGroup {

	// Drive constants
	final double phase1_drive_speed = 0.8;
	final double phase2_drive_speed = 0.5;
	final double cross_baseline_speed = 0.5;

	// Subsystems
	final double elevator_speed = 0.5;
	final double eject_speed = -0.8;
	final double intake_initial_speed = 0.5;

	// Timing phases
	final double cross_baseline_time = 5 - .3;
	final double phase1 = 5 - .3;
	final double phase1_e = 5 - .3;
	final double phase2 = 5 - .3;
	
	final double intake_time = 1.0;

	int robot_position;
	int switch_position;

	boolean same_side;
	boolean first = true;

	Timer autoTimer;

	public TimedAutoRoutine2(int robot_position, int switch_position) {

		if (robot_position == switch_position) {
			addSequential(new DriveStraightTimed(phase1, phase1_drive_speed));
			addParallel(new SetIntake(intake_time, intake_initial_speed));
			addParallel(new RaiseElevator(phase1_e, elevator_speed, true));
			addSequential(new TurnToSetpoint(robot_position == 0 ? 90 : -90));
			addSequential(new DriveStraightTimed(phase2, phase2_drive_speed));
			addSequential(new SetIntake(intake_time, eject_speed));
			addSequential(new DriveStraightTimed(phase2, -phase2_drive_speed));
		} else if (robot_position == 1) {
			// Write middle auto (deposit to vault?)
			addSequential(new DriveStraightTimed(cross_baseline_time, cross_baseline_speed));
			addParallel(new SetIntake(intake_time, intake_initial_speed));
		} else {
			addSequential(new DriveStraightTimed(cross_baseline_time, cross_baseline_speed));
			addParallel(new SetIntake(intake_time, intake_initial_speed));
		}

	}
}
