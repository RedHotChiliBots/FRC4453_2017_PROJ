package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveDistance extends Command {
	private double dist;

	public AutoDriveDistance(double dist) {
		this.dist = dist;
		requires(Robot.chassis);
	}

	@Override
	protected void initialize() {
		Robot.chassis.driveDistance(dist);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return Robot.chassis.isDistanceOnTarget(0.01);
	}

	@Override
	protected void end() {
		Robot.chassis.stop();
	}

	@Override
	protected void interrupted() {
		Robot.chassis.stop();
	}
}
