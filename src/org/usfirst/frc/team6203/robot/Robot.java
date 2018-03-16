
package org.usfirst.frc.team6203.robot;

import org.usfirst.frc.team6203.robot.commands.AutoTest;
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

	public static DigitalOutput arduino1, arduino2, arduino3, arduino4;

	// chassis
	public static SpeedControllerGroup m_left, m_right;
	public static DifferentialDrive drive;
	public static Victor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
	public static Chassis chassis;

	// intake
	public static Spark m_intakeDropperMotor;
	public static Victor m_intakeMotorM, m_intakeMotorS;
	public static Intake intake;

	// elevator
	public static Victor elevatorMotor;
	public static DigitalInput DI_bottom, DI_switch, DI_scale, DI_top;
	public static Elevator elevator;

//	 public static Encoder left_encoder;
//	 public static Encoder right_encoder;
//	 
//	 public static PIDController left_PID_controller;
//	 public static PIDController right_PID_controller;

	// public static Ultrasonic ultrasonic;

	int robotPos;
	int switchPos;
	
	Command autonomousCommand;
	SendableChooser<Integer> chooser;


	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		// Instantiate subsystems
		oi = new OI();

		// chassis
		leftFrontMotor = new Victor(RobotMap.leftMotorF);
		leftBackMotor = new Victor(RobotMap.leftMotorB);
		rightFrontMotor = new Victor(RobotMap.rightMotorF);
		rightBackMotor = new Victor(RobotMap.rightMotorB);

		m_left = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
		m_right = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

		drive = new DifferentialDrive(m_left, m_right);
		chassis = new Chassis();
		
		//Encoders
		//left_encoder = new Encoder(RobotMap.left_encoder_channelA, RobotMap.left_encoder_channelB);
		//right_encoder = new Encoder(RobotMap.right_encoder_channelA, RobotMap.right_encoder_channelB);
		
		//left_encoder.setDistancePerPulse(Constants.kDistancePerPulse);
		//right_encoder.setDistancePerPulse(Constants.kDistancePerPulse);
		
		//PIDControllers
//		left_PID_controller = new PIDController(0, 0, 0, left_encoder, m_left);
//		right_PID_controller = new PIDController(0, 0, 0, right_encoder, m_right);
		
//		left_PID_controller.setAbsoluteTolerance(0.2);
//		right_PID_controller.setAbsoluteTolerance(0.2);

		// intake
		m_intakeMotorM = new Victor(RobotMap.intakeMotorM);
		m_intakeMotorS = new Victor(RobotMap.intakeMotorS); 
		m_intakeDropperMotor = new Spark(RobotMap.intakeDropperMotor);
		intake = new Intake();

		// elevator
		elevatorMotor = new Victor(RobotMap.elevatorMotor);

		// Instantiate limit switches
		DI_bottom = new DigitalInput(RobotMap.DI_bottom);
		DI_switch = new DigitalInput(RobotMap.DI_switch);
		DI_top = new DigitalInput(RobotMap.DI_top);
		elevator = new Elevator();

		// extra

		// axisCam = CameraServer.getInstance();
		// axisCam.addAxisCamera("axis cam", Constants.IP);
		// axisCam.startAutomaticCapture();

		usbCam = CameraServer.getInstance();
		usbCam.startAutomaticCapture();

		imu = new ADIS16448_IMU();

		// encoder = new Encoder(RobotMap.encoder_channelA,
		// RobotMap.encoder_channelB);

		arduino1 = new DigitalOutput(8);
		arduino2 = new DigitalOutput(9);
		arduino3 = new DigitalOutput(6);
		arduino4 = new DigitalOutput(7);

		chooser = new SendableChooser<Integer>();
		chooser.addObject("left", 0);
		chooser.addDefault("middle", 1);
		chooser.addObject("right", 2);
		SmartDashboard.putData("Autonomous Selector", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	boolean fdisable = false;

	public void disabledInit() {
		if (fdisable) {
			arduino3.set(true);
			arduino4.set(false);
		} else {
			arduino3.set(false);
			arduino4.set(false);
		}
	}

	@Override
	public void disabledPeriodic() {

		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */


	public void autonomousInit() {

		arduino3.set(true);
		arduino4.set(true);

		robotPos = 2;
		
		double start = System.currentTimeMillis();

		String gameData;
		do
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		while (gameData.length() == 0 && System.currentTimeMillis() - start < 2000); // keep going until we get data or
																						// give up at 2 sec

		if (gameData.length() == 0)
			gameData = "L welp lets take a guess fellas";

		switchPos = gameData.charAt(0) == 'L' ? 2 : 0;
		
		Robot.imu.calibrate();

//		autonomousCommand = new TimedAutoRoutine(robotPos, switchPos);
		autonomousCommand = new AutoTest();
		
		autonomousCommand.start();
			
	}

	public void autonomousPeriodic() {
		arduino3.set(true);
		arduino4.set(true);
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		fdisable = true;
		// intake.drop = false;
		// intake.dropped_time = -1;
		Drive drive_command = new Drive();
		drive_command.start();

		Drive.slow = false;
	}

	/**
	 * This function is called periodically during operator control
	 */

	boolean pressed9 = false, on9 = false;

	public void teleopPeriodic() {
		arduino1.set(Robot.oi.elevatorStick.getRawButton(1));
		arduino2.set(Robot.oi.elevatorStick.getRawButton(3));

		if (Robot.oi.driverStick.getRawButton(9) && !pressed9) {
			pressed9 = true;
			on9 = !on9;
		} else {
			pressed9 = false;
		}

		arduino3.set(false);
		arduino4.set(on9);

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
