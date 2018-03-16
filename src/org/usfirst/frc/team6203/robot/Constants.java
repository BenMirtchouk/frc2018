package org.usfirst.frc.team6203.robot;

public class Constants {
	
	public static final String IP = "10.62.3.52";
	
	//Chassis

	public static final double kDistancePerPulse = Math.PI * 4;

	public static final double m_DriveBaseOutput = 1.0;
	public static final double m_DriveBaseSlowOutput = 0.5;

	
	//Elevator
	public static final double m_ElevatorMaxSpeed = 1.0;
	public static final double m_ElevatorPresetSpeed = .45;

	//Intake
	public static final double m_IntakeMaxSpeed = 0.8;
	public static final double m_IntakeDropperMaxSpeed = .5;
	public static final double m_IntakeFullDropTime = 1000; // ms
}
