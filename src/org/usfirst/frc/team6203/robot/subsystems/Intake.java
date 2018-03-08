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

	private Spark m_intakeDropperMotor;
	private Victor m_intakeMotorM;
	private Victor m_intakeMotorS;

	final double fullspeed = .5;
	final double full_drop_time = 1000; // ms

	boolean drop = false;
	double dropped_time = -1;

	public Intake() {
		m_intakeMotorM = new Victor(RobotMap.intakeMotorM);
		m_intakeMotorS = new Victor(RobotMap.intakeMotorS); // invert?
		m_intakeDropperMotor = new Spark(RobotMap.intakeDropperMotor);
	}

	public void initDefaultCommand() {

	}

	private void updateButtons() {
		drop = drop || Robot.oi.elevatorStick.getRawButton(3);
	}

	public void dropperLoop() {
		double curr_time = System.currentTimeMillis();

		if (curr_time - dropped_time > Constants.m_IntakeFullDropTime) {
			m_intakeDropperMotor.set(0);
			return;
		}

		updateButtons();

		if (dropped_time == -1 && drop)
			dropped_time = System.currentTimeMillis();

		m_intakeDropperMotor.set(Constants.m_IntakeDropperMaxSpeed);
	}

	public void setIntakeSpeed(double speed) {
		m_intakeMotorM.set(speed);
		m_intakeMotorS.set(speed);
	}

	public void drive() {

		// Check these buttons
		if (!drop) {
			if (Robot.oi.driverStick.getRawButton(4)) {
				setIntakeSpeed(Constants.m_IntakeMaxSpeed);
			} else if (Robot.oi.driverStick.getRawButton(5)) {
				setIntakeSpeed(-Constants.m_IntakeMaxSpeed);
			} else {
				setIntakeSpeed(0);
			}
		} else
			dropperLoop();
	}
}
