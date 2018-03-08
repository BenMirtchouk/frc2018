package org.usfirst.frc.team6203.robot.subsystems;

import org.usfirst.frc.team6203.robot.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Intake extends Subsystem {

	private Spark intk;

	final double fullspeed = .5;
	final double full_drop_time = 1000; // ms

	boolean drop = false;
	double dropped_time = -1;

	public Intake() {
		intk = new Spark(7);
	}

	public void initDefaultCommand() {

	}

	private void updateButtons() {
		drop = drop || Robot.oi.elevatorStick.getRawButton(3);
	}

	public void drive() {
		double curr_time = System.currentTimeMillis();
		
		if (curr_time - dropped_time > full_drop_time){
			intk.set(0);
			return;
		}
		
		updateButtons();
		
		if (dropped_time==-1 && drop)
			dropped_time = System.currentTimeMillis();
		
		intk.set(fullspeed);
	}
}
