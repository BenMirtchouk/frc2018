package org.usfirst.frc.team6203.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoTest extends CommandGroup {

    public AutoTest() {
        addSequential(new TurnToSetpoint(90));
        addSequential(new TurnToSetpoint(180));
    }
}
