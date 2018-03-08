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
public class Elevator extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private Victor elv;
	public static DigitalInput DI_bottom, DI_switch, DI_scale, DI_top;
	boolean b_bottom, b_switch, b_top, halt, preset_switch, preset_scale, move_switch, move_top;
	final double fullspeed = 1;
	final double preset_speed = .2;
	int state = 0;

	public Elevator() {
		elv = new Victor(4);
		DI_bottom = new DigitalInput(0);
		DI_switch = new DigitalInput(1);
		DI_top = new DigitalInput(2);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	private void updateButtons() {
		b_bottom = !DI_bottom.get();
		b_switch = !DI_switch.get();
		b_top = !DI_top.get();

		preset_switch = Robot.oi.elevatorStick.getRawButton(1);
		preset_scale = Robot.oi.elevatorStick.getRawButton(2);

		if (!move_switch && preset_switch)
			move_switch = true;
		if (!move_top && preset_scale)
			move_top = true;

		halt = Robot.oi.driverStick.getRawButton(11);

		SmartDashboard.putBoolean("b_bottom", b_bottom);
		SmartDashboard.putBoolean("b_switch", b_switch);
		SmartDashboard.putBoolean("b_top", b_top);
		SmartDashboard.putBoolean("preset_switch", preset_switch);
		SmartDashboard.putBoolean("preset_scale", preset_scale);
	}

	public void drive() {
		updateButtons();

		if (b_switch)
			if (elv.get() > 0)
				state = 1;
			else if (elv.get() < 0)
				state = 0;

		if (!halt) {
			if (move_switch) {
				if (b_switch)
					move_switch = false;
				else if (state == 0)
					elv.set(preset_speed);
				else if (state == 1)
					elv.set(-preset_speed);

				return;
			}
			if (move_top) {
				if (b_top)
					move_top = false;
				else
					elv.set(preset_speed);

				return;
			}
		} else
			move_switch = move_top = false;

		double y = Robot.oi.elevatorStick.getY();
		SmartDashboard.putNumber("y val", y);

		if (b_bottom && y < 0)
			return;
		if (b_top && y > 0)
			return;

		elv.set(y);
	}
}
