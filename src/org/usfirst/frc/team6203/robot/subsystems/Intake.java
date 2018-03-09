package org.usfirst.frc.team6203.robot.subsystems;

import org.usfirst.frc.team6203.robot.Constants;
import org.usfirst.frc.team6203.robot.Robot;
import org.usfirst.frc.team6203.robot.RobotMap;

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

	final double fullspeed = .5;
	final double full_drop_time = 1000; // ms

	boolean drop = false;
	double dropped_time = -1;

	public Intake() {
	}

	public void initDefaultCommand() {

	}

	private void updateButtons() {
		drop = drop || Robot.oi.elevatorStick.getRawButton(3);
	}

	public void setIntakeSpeed(double speed) {
		Robot.m_intakeMotorM.set(speed);
		Robot.m_intakeMotorS.set(speed);
	}

	public void drive() {
		double curr_time = System.currentTimeMillis();

		if (curr_time - dropped_time > Constants.m_IntakeFullDropTime) Robot.m_intakeDropperMotor.set(0);
		else {
			updateButtons();

			if (dropped_time == -1 && drop) dropped_time = System.currentTimeMillis();

			Robot.m_intakeDropperMotor.set(Constants.m_IntakeDropperMaxSpeed);
		}

		if (drop) setIntakeSpeed(0);
		else if (Robot.oi.driverStick.getRawButton(4)) setIntakeSpeed(Constants.m_IntakeMaxSpeed);
		else if (Robot.oi.driverStick.getRawButton(5)) setIntakeSpeed(-Constants.m_IntakeMaxSpeed);

	}
}
