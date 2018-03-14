package org.usfirst.frc.team6203.robot.commands;

import org.usfirst.frc.team6203.robot.Robot;
import org.usfirst.frc.team6203.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Command {

	public static boolean slow;
	public boolean slow_pressed = false;
	public static boolean drop_intake = false;

	public Drive() {
		requires(Robot.chassis);
	}

	protected void initialize() {
		slow = false;
	}

	private void updateButtons() {
		boolean slow_curr = Robot.oi.driverStick.getRawButton(RobotMap.slowspeed);
		if (!slow_pressed && slow_curr)
			slow = !slow;

		slow_pressed = slow_curr;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		updateButtons();

		//		Robot.chassis.tankDrive();
		Robot.chassis.arcadeDrive();
		Robot.elevator.drive();
		Robot.intake.drive();
	}

	final static double Kp = 0.03;

	public static void driveStraight(double x) {
		double angle = Robot.imu.getAngleZ();
		Robot.drive.arcadeDrive(x, -angle * Kp);
	}

	public static void stop() {
		Robot.drive.tankDrive(0, 0);
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
