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
public class Elevator extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	boolean b_bottom, b_switch, b_top, halt, preset_switch, preset_scale, move_switch, move_top;

	int state = 0;

	public Elevator() {
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	private void updateButtons() {
		b_bottom = !Robot.DI_bottom.get(); // hall effect sensors probs
		b_switch = !Robot.DI_switch.get();
		b_top = !Robot.DI_top.get();

		// preset_switch = Robot.oi.elevatorStick.getRawButton(2);
		// preset_scale = Robot.oi.elevatorStick.getRawButton(4);

		if (!move_switch && preset_switch)
			move_switch = true;
		if (!move_top && preset_scale)
			move_top = true;

		halt = Robot.oi.driverStick.getRawButton(11);

		publishValues();
	}

	public void drive() {
		updateButtons();

		if (b_switch)
			if (Robot.elevatorMotor.get() > 0)
				state = 1;
			else if (Robot.elevatorMotor.get() < 0)
				state = 0;

		if (!halt) {
			if (move_switch) {
				if (b_switch)
					move_switch = false;
				else if (state == 0)
					Robot.elevatorMotor.set(Constants.m_ElevatorPresetSpeed);
				else if (state == 1)
					Robot.elevatorMotor.set(-Constants.m_ElevatorPresetSpeed);
				return;
			}
			if (move_top) {
				if (b_top)
					move_top = false;
				else
					Robot.elevatorMotor.set(Constants.m_ElevatorPresetSpeed);
				return;
			}
		} else
			move_switch = move_top = false;

		double x = Robot.oi.elevatorStick.getX();
		SmartDashboard.putNumber("x val", x);

		publishValues();

		if (b_top)
			Robot.drive.setMaxOutput(Constants.m_DriveBaseSlowOutput);
		else
			Robot.drive.setMaxOutput(Constants.m_DriveBaseOutput);

		if (b_bottom && x < 0)
			return;
		if (b_top && x > 0)
			return;

		Robot.elevatorMotor.set(x);
	}

	public void setElevator(double speed) {

		if (b_top)
			Robot.drive.setMaxOutput(Constants.m_DriveBaseSlowOutput);
		else
			Robot.drive.setMaxOutput(Constants.m_DriveBaseOutput);

		if (b_bottom && speed < 0)
			return;
		if (b_top && speed > 0)
			return;

		Robot.elevatorMotor.set(speed);

	}

	private void publishValues() {
		SmartDashboard.putBoolean("b_bottom", b_bottom);
		SmartDashboard.putBoolean("b_switch", b_switch);
		SmartDashboard.putBoolean("b_top", b_top);
		SmartDashboard.putBoolean("preset_switch", preset_switch);
		SmartDashboard.putBoolean("preset_scale", preset_scale);
	}
}
