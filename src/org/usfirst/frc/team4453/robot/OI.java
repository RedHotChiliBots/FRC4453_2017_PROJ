package org.usfirst.frc.team4453.robot;

import org.usfirst.frc.team4453.robot.commands.BubblerStart;
import org.usfirst.frc.team4453.robot.commands.GrabberDown;
import org.usfirst.frc.team4453.robot.commands.GrabberGrab;
import org.usfirst.frc.team4453.robot.commands.GrabberRelease;
import org.usfirst.frc.team4453.robot.commands.GrabberUp;
import org.usfirst.frc.team4453.robot.commands.AutoShooterControl;
import org.usfirst.frc.team4453.robot.commands.ShooterControlWithJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick drive1Controller;
	public XboxController drive2Controller;

	public Button shooterManualOverride;
	public Button collectorCollect;
	
	public Button grabberGrab;
	public Button grabberRelease;
	public Button grabberUp;
	public Button grabberDown;

	public OI() {
		System.out.println("OI starting...");

		drive1Controller = new Joystick(RobotMap.FIRST_CONTROLLER);
		drive2Controller = new XboxController(RobotMap.SECOND_CONTROLLER);
		
		shooterManualOverride = new JoystickButton(drive2Controller, RobotMap.RIGHT_BUMPER);
		
		shooterManualOverride.whileHeld(new ShooterControlWithJoystick());
		shooterManualOverride.whenReleased(new AutoShooterControl());
		
		collectorCollect = new JoystickButton(drive1Controller, RobotMap.TRIGGER_1);
		collectorCollect.whileHeld(new BubblerStart());
		
		grabberUp = new JoystickButton(drive1Controller, RobotMap.BUTTON_3);
		grabberUp.whenActive(new GrabberUp());
		
		grabberDown = new JoystickButton(drive1Controller, RobotMap.BUTTON_2);
		grabberDown.whenActive(new GrabberDown());
		
		grabberGrab = new JoystickButton(drive1Controller, RobotMap.BUTTON_5);
		grabberGrab.whenPressed(new GrabberGrab());
		
		grabberRelease = new JoystickButton(drive1Controller, RobotMap.BUTTON_4);
		grabberRelease.whenPressed(new GrabberRelease());
		
		System.out.println("OI running.");
	}

	// Joysticks and Triggers

	// for driving
	public double getLeftYDriveStick() {
		double leftY = drive1Controller.getRawAxis(RobotMap.Y_AXIS);
		return (Math.abs(leftY) < 0.15 ? 0.0 : -leftY);
	}

	public double getLeftXDriveStick() {
		double leftX = drive1Controller.getRawAxis(RobotMap.X_AXIS);
		return (Math.abs(leftX) < 0.15 ? 0.0 : -leftX);
	}

	public double getThrottleDrive() {
		double speedValue = drive1Controller.getRawAxis(RobotMap.THROTTLE_AXIS);
		return ((-speedValue + 1.0) / 2.0);
	}

	// for shooting
	public double getRightYShooterStick() {
		double rightY = drive2Controller.getRawAxis(RobotMap.RIGHT_Y_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
	public double getRightXShooterStick() {
		double rightY = drive2Controller.getRawAxis(RobotMap.RIGHT_X_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
	public double getLeftXShooterStick() {
		double rightY = drive2Controller.getRawAxis(RobotMap.LEFT_X_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
	public double getLeftYShooterStick() {
		double rightY = drive2Controller.getRawAxis(RobotMap.LEFT_Y_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
	public double getLeftShooterTrigger() {
		double rightY = drive2Controller.getRawAxis(RobotMap.LEFT_TRIGGER_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
	public double getRightShooterTrigger() {
		double rightY = drive2Controller.getRawAxis(RobotMap.RIGHT_TRIGGER_AXIS);
		return (Math.abs(rightY) < 0.15 ? 0.0 : rightY);
	}
}
