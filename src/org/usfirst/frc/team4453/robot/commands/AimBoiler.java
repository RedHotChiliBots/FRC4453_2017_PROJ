package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.library.Vision;
import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AimBoiler extends Command {

	boolean doTestVisible;

	public AimBoiler(boolean giveUpIfNotFound) {
		requires(Robot.chassis);
		requires(Robot.shooter);
		doTestVisible = giveUpIfNotFound;
	}

	@Override
	protected void initialize() {
		Vision.setCamera("boiler", 5);
		Robot.chassis.enableChassisPID(0);
	}

	@Override
	protected void execute() {
		if (Vision.boilerVisible()) {
			Robot.shooter.tiltSetAngle(Robot.shooter.getAimAngle(Vision.getBoilerDist()));
			Robot.shooter.yawSetAngle(Vision.getBoilerAngleOffset());
			if (Robot.shooter.yawHitLimit()) {
				Robot.chassis.chassisSetSetpoint(Robot.chassis.chassisGetHeading() + Vision.getBoilerAngleOffset());
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return Vision.getBoilerAngleOffset() < 1 && (doTestVisible ? Vision.boilerVisible() : true);
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
