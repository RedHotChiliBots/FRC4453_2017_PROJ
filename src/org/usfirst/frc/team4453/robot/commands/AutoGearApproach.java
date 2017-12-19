package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.library.Vision;
import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoGearApproach extends Command {

	private double minDist = 0;

	public AutoGearApproach(double dist) {
		requires(Robot.chassis);
		minDist = dist;
	}

	@Override
	protected void initialize() {
		Vision.setCamera("gear", 5);
		Robot.chassis.enableChassisPID(0.25);
	}

	@Override
	protected void execute() {
		Robot.chassis.chassisSetSetpoint(Vision.getGearAngleOffset() + Robot.chassis.chassisGetHeading());
	}

	@Override
	protected boolean isFinished() {
		return !Vision.gearVisible() || Vision.getGearDist() < minDist;
	}

	@Override
	protected void end() {
		Robot.chassis.disableChassisPID();
		Robot.chassis.stop();
	}

	@Override
	protected void interrupted() {
		Robot.chassis.disableChassisPID();
		Robot.chassis.stop();
	}
}
