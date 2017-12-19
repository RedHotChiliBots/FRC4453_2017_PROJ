package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class BubblerStop extends InstantCommand {

	public BubblerStop() {
		requires(Robot.collector);
	}

	@Override
	protected void execute() {
		Robot.collector.stop();
	}
}
