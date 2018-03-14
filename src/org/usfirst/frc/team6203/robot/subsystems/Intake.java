package org.usfirst.frc.team6203.robot.subsystems;

import org.usfirst.frc.team6203.robot.Constants;
import org.usfirst.frc.team6203.robot.Robot;
import org.usfirst.frc.team6203.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Intake extends Subsystem {

	public boolean drop = false;

	public Intake() {
	}

	public void initDefaultCommand() {

	}

	public void setIntakeSpeed(double speed) {
		Robot.m_intakeMotorM.set(-speed);
		Robot.m_intakeMotorS.set(speed);
	}

	public void drive() {

		boolean left = Robot.oi.elevatorStick.getRawButton(4);
		boolean right = Robot.oi.elevatorStick.getRawButton(5);
		if (right) {
			Robot.m_intakeDropperMotor.set(-0.8);
		} else if (left) {
			Robot.m_intakeDropperMotor.set(0.8);
		} else Robot.m_intakeDropperMotor.set(0);

		drop = drop || left || right;

		if (drop)
			setIntakeSpeed(0);
		else if (Robot.oi.driverStick.getRawButton(4))
			setIntakeSpeed(Constants.m_IntakeMaxSpeed);
		else if (Robot.oi.driverStick.getRawButton(6))
			setIntakeSpeed(-Constants.m_IntakeMaxSpeed);
		else setIntakeSpeed(0);

	}
}
