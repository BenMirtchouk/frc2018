package org.usfirst.frc.team6203.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRoutine extends CommandGroup {
	
	//this is bad dont use it

	public AutoRoutine(int robot_position, int switch_position) {

		if (robot_position == switch_position) {
			addSequential(new DriveStraightToSetpoint(100, 0.5));
			addParallel(new SetIntake(2.0, 0.5));
			addParallel(new RaiseElevator(4.7, 0.5, true));
			addSequential(new TurnToSetpoint(robot_position == 0 ? 90 : -90));
			addSequential(new DriveStraightToSetpoint(20, 0.5));
			addSequential(new SetIntake(0.5, -0.8));
			addSequential(new DriveStraightToSetpoint(-20, 0.5));
		} else if (robot_position == 1) {
			//Write middle auto (deposit to vault?)
			addSequential(new DriveStraightToSetpoint(100, 0.5));
			addParallel(new SetIntake(2.0, 0.5));
		} else {
			addSequential(new DriveStraightToSetpoint(100, 0.5));
			addParallel(new SetIntake(2.0, 0.5));
		}

	}
}
