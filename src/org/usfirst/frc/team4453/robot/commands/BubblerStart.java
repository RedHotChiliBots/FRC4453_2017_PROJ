package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BubblerStart extends Command {

	public BubblerStart() {
		requires(Robot.collector);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.collector.collect();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.collector.stop();
	}

	@Override
	protected void interrupted() {
		Robot.collector.stop();
	}
}
