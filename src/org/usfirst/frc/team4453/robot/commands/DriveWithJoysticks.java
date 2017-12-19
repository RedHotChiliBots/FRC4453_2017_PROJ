
package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * DriveWithjoystick - gets commands from the joysticks defined in OI to send to
 * the chassis method to drive the robot.
 * 
 * Requires the chassis. Only command that can be running for the chassis.
 * 
 */
public class DriveWithJoysticks extends Command {

	public DriveWithJoysticks() {
		requires(Robot.chassis);
	}

	@Override
	protected void initialize() {
		System.out.println("DriveWithJoystick");
	}

	@Override
	protected void execute() {
		double yCmd = Robot.oi.getLeftYDriveStick();
		double xCmd = Robot.oi.getLeftXDriveStick();
		double zCmd = Robot.oi.getThrottleDrive();
		Robot.chassis.arcadeDrive(yCmd * zCmd, xCmd * zCmd);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
