package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveTime extends Command {

	private double seconds;

	public AutoDriveTime(double drivetime) {
		requires(Robot.chassis);
		seconds = drivetime;
	}

	@Override
	protected void initialize() {
		setTimeout(seconds);
	}

	@Override
	protected void execute() {
		Robot.chassis.arcadeDrive(0.75, 0.0);
		// The robot will drive until "setTimeout" runs out of time
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut();
	}

	@Override
	protected void end() {
		Robot.chassis.stop();
	}

	@Override
	protected void interrupted() {
	}
}
