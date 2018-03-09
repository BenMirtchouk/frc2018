
package org.usfirst.frc.team6203.robot;

import org.usfirst.frc.team6203.robot.commands.Drive;
import org.usfirst.frc.team6203.robot.subsystems.ADIS16448_IMU;
import org.usfirst.frc.team6203.robot.subsystems.Chassis;
import org.usfirst.frc.team6203.robot.subsystems.Elevator;
import org.usfirst.frc.team6203.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;

	public static CameraServer axisCam;
	public static CameraServer usbCam;

	public static ADIS16448_IMU imu;

	public static DigitalOutput pong_l, pong_r;

	//chassis
	public static SpeedControllerGroup m_left, m_right;
	public static DifferentialDrive drive;
	public static Victor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
	public static Chassis chassis;

	//intake
	public static Spark m_intakeDropperMotor;
	public static Victor m_intakeMotorM, m_intakeMotorS;
	public static Intake intake;

	//elevator
	public static Victor elevatorMotor;
	public static DigitalInput DI_bottom, DI_switch, DI_scale, DI_top;
	public static Elevator elevator;

	// public static Encoder encoder;

	// public static Ultrasonic ultrasonic;

	int robotPos;
	SendableChooser<Integer> chooser;
	int switchPos;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		// Instantiate subsystems
		oi = new OI();

		//chassis
		leftFrontMotor = new Victor(RobotMap.leftMotorF);
		leftBackMotor = new Victor(RobotMap.leftMotorB);
		rightFrontMotor = new Victor(RobotMap.rightMotorF);
		rightBackMotor = new Victor(RobotMap.rightMotorB);

		m_left = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
		m_right = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

		drive = new DifferentialDrive(m_left, m_right);
		chassis = new Chassis();

		//intake
		m_intakeMotorM = new Victor(RobotMap.intakeMotorM);
		m_intakeMotorS = new Victor(RobotMap.intakeMotorS); // invert?
		m_intakeDropperMotor = new Spark(RobotMap.intakeDropperMotor);
		intake = new Intake();

		//elevator
		elevatorMotor = new Victor(RobotMap.elevatorMotor);

		// Instantiate limit switches
		DI_bottom = new DigitalInput(RobotMap.DI_bottom);
		DI_switch = new DigitalInput(RobotMap.DI_switch);
		DI_top = new DigitalInput(RobotMap.DI_top);
		elevator = new Elevator();

		//extra

		axisCam = CameraServer.getInstance();
		axisCam.addAxisCamera("axis cam", Constants.IP);
		axisCam.startAutomaticCapture();

		usbCam = CameraServer.getInstance();
		usbCam.startAutomaticCapture();

		imu = new ADIS16448_IMU();

		// encoder = new Encoder(RobotMap.encoder_channelA,
		// RobotMap.encoder_channelB);

		pong_l = new DigitalOutput(8);
		pong_r = new DigitalOutput(9);

		chooser = new SendableChooser<Integer>();
		chooser.addDefault("middle", 1);
		chooser.addObject("left", 0);
		chooser.addObject("right", 2);
		SmartDashboard.putData("Autonomous Selector", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		pong_l.set(Robot.oi.elevatorStick.getX() > .5);
		pong_r.set(Robot.oi.elevatorStick.getX() < -.5);

		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code
	 * to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	double autoStart;

	public void autonomousInit() {
		robotPos = chooser.getSelected();
		double start = System.currentTimeMillis();

		String gameData;
		do
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		while (gameData.length() == 0 && System.currentTimeMillis() - start < 2000); //keep going until we get data or give up at 2 sec

		if (gameData.length() == 0)
			gameData = "L welp lets take a guess fellas";

		switchPos = gameData.charAt(0) == 'L' ? 0 : 2;
		autoStart = System.currentTimeMillis();
	}

	//lr prefix means when the robot is on the left/right (these are mirrors so are handled similarly)
	//c prefix means when the robot is in the center

	//e after lr means when the robot is on the same side as the switch
	//n after lr means when the robot is on a different side from the switch

	final double shootSpeed = 0.5; //what speed to eject from intake at
	final double raiseSpeed = 0.5; //what speed to raise elevator at

	final double just_move_forward_phase1 = 5000; //how long to move forward for
	final double just_move_forward_phase1_forwardSpeed = 0.5; //what speed to go forward at

	final double lr_e_phase1 = 4000; //how long to move forward for
	final double lr_e_phase1_forwardSpeed = 0.5; //what speed to go forward at

	final double lr_e_phase2 = 1000; //how long to raise elevator for 

	final double lr_e_phase3 = 500; //how long to eject from intake for

	final double lr_n_phase1 = 2500; //how long to move to other side
	final double lr_n_phase1_slowWheel_Speed = 0.3; //what speed to do this tilt at (slow wheel)
	final double lr_n_phase1_fastWheel_Speed = 0.5; //what speed to do this tilt at (fast wheel)

	final double lr_n_phase2 = 2500; //how long to move tilted to correct angle

	final double lr_n_phase3 = 1000; //how long to raise elevator for 

	final double lr_n_phase4 = 500; //how long to eject from intake for

	public void autonomousPeriodic() {
		double time = System.currentTimeMillis();

		switch (robotPos) {
		case 1: //center
			if (time - autoStart < just_move_forward_phase1)
				drive.tankDrive(just_move_forward_phase1_forwardSpeed, just_move_forward_phase1_forwardSpeed);
			else drive.tankDrive(0, 0);

			break;
		case 0: //left
		case 2: //right
			if (switchPos == robotPos) { //switch is on the same side as the robot
				if (time - autoStart < lr_e_phase1) {
					drive.tankDrive(lr_e_phase1_forwardSpeed, lr_e_phase1_forwardSpeed);

					if (time - autoStart < lr_e_phase2) {
						elevatorMotor.set(raiseSpeed);
					} else {
						elevatorMotor.set(0);
					}
				} else if (time - autoStart - lr_e_phase1 < lr_e_phase3) {
					drive.tankDrive(0, 0);
					intake.setIntakeSpeed(shootSpeed);
				} else {
					intake.setIntakeSpeed(0);
				}
			} else { //oh no
				if (time - autoStart < lr_n_phase1) {
					if (robotPos == 0) {
						drive.tankDrive(lr_n_phase1_fastWheel_Speed, lr_n_phase1_slowWheel_Speed);
					} else {
						drive.tankDrive(lr_n_phase1_slowWheel_Speed, lr_n_phase1_fastWheel_Speed);
					}
					
					
					if (time - autoStart < lr_n_phase3) {
						elevatorMotor.set(raiseSpeed);
					} else {
						elevatorMotor.set(0);
					}
				} else if (time - autoStart - lr_n_phase1 < lr_n_phase2) {
					if (robotPos == 2) {
						drive.tankDrive(lr_n_phase1_fastWheel_Speed, lr_n_phase1_slowWheel_Speed);
					} else {
						drive.tankDrive(lr_n_phase1_slowWheel_Speed, lr_n_phase1_fastWheel_Speed);
					}

				} else if (time - autoStart - lr_n_phase1 - lr_n_phase2 < lr_n_phase3) {
					drive.tankDrive(0, 0);
					intake.setIntakeSpeed(shootSpeed);
				} else {
					intake.setIntakeSpeed(0);
				}
			}
			break;

		}

	}

	@Override
	public void teleopInit() {
		Drive drive_command = new Drive();
		drive_command.start();
		Drive.slow = false;
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		pong_l.set(Robot.oi.elevatorStick.getX() > .5);
		pong_r.set(Robot.oi.elevatorStick.getX() < -.5);

		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

		LiveWindow.run();
	}
}
