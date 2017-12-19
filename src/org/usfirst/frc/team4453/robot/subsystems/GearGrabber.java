package org.usfirst.frc.team4453.robot.subsystems;

import org.usfirst.frc.team4453.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearGrabber extends Subsystem {

	Solenoid grabberE;
	Solenoid grabberR;
	Solenoid tipperE;
	Solenoid tipperR;

	public GearGrabber() {
		super("GearGrabber");

		grabberR = new Solenoid(RobotMap.GEAR_GRAB);
		grabberE = new Solenoid(RobotMap.GEAR_RELEASE);
		tipperR = new Solenoid(RobotMap.GEAR_TIPUP);
		tipperE = new Solenoid(RobotMap.GEAR_TIPDOWN);

	}

	@Override
	public void initDefaultCommand() {
		// Not used.
	}

	public void grab() {
		grabberE.set(false);
		grabberR.set(true);
	}

	public void release() {
		grabberR.set(false);
		grabberE.set(true);
	}

	public void tipUp() {
		tipperE.set(false);
		tipperR.set(true);
	}

	public void tipDown() {
		tipperR.set(false);
		tipperE.set(true);
	}
}
