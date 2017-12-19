package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {

	private double setpoint;

	public AutoTurn(double angle) {
		requires(Robot.chassis);
		setpoint = Robot.chassis.chassisGetHeading() + angle;
	}

	@Override
	protected void initialize() {
		Robot.chassis.chassisSetSetpoint(setpoint);
		Robot.chassis.enableChassisPID(0);
	}

	@Override
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return Robot.chassis.onTarget();
	}

	@Override
	protected void end() {
		Robot.chassis.disableChassisPID();
	}

	@Override
	protected void interrupted() {
		Robot.chassis.disableChassisPID();
	}
}
