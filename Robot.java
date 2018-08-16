/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4183.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private WPI_TalonSRX leftFront;
	private WPI_TalonSRX rightFront;
	private WPI_TalonSRX leftRear;
	private WPI_TalonSRX rightRear;
	
	private long lastTime = 0;
	private long totalTime = 0;
	
	private Joystick joystick = new Joystick(0);
	
	private DifferentialDrive drive;
	private SpeedControllerGroup leftMotorGroup;
	private SpeedControllerGroup rightMotorGroup;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		leftFront = new WPI_TalonSRX(11);
		leftFront.setInverted(true);
		
		rightFront = new WPI_TalonSRX(12);

		leftRear = new WPI_TalonSRX(13);
		leftRear.setInverted(true);
		
		rightRear = new WPI_TalonSRX(14);
		
		
		leftMotorGroup = new SpeedControllerGroup(leftFront, leftRear);
		rightMotorGroup = new SpeedControllerGroup(rightFront, rightRear);
		
		drive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
		drive.setSafetyEnabled(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		boolean val = joystick.getButton(ButtonType.values()[4]);
		
		//double joystick.getRawAxis(PS4Constants.LEFT_STICK_X.getValue());
		
		if (val) {
			long temp = System.currentTimeMillis();
			long dt = temp - lastTime;
			lastTime = temp;
			
			totalTime += dt;
			
			double v = 1 / (1 + Math.pow(Math.E, -(totalTime - 10)));
			
			drive.arcadeDrive(v, 0);
		} else {
			drive.arcadeDrive(0, 0);
			
			totalTime = 0;
		}
	}
	
	@Override
	public void disabledInit() {
		drive.arcadeDrive(0, 0);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
