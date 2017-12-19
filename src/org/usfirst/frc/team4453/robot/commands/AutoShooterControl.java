package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.library.Vision;
import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoShooterControl extends Command {

	public AutoShooterControl() {
		requires(Robot.shooter);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (Vision.boilerVisible()) {
			Robot.shooter.yawSetAngle(Vision.getBoilerAngleOffset());
			Robot.shooter.tiltSetAngle(Robot.shooter.getAimAngle(Vision.getBoilerDist()));
			if(Robot.oi.drive2Controller.getBumper(Hand.kLeft))
			{
				if(Vision.getBoilerAngleOffset() < 2)
				{
					Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, 0);
					Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble, 0);
					if(Math.abs(Robot.shooter.shooterGetSpeed() - Robot.shooter.getAimSpeed(Vision.getBoilerDist())) < 10)
					{
						Robot.shooter.shooterFire(Robot.shooter.getAimSpeed(Vision.getBoilerDist()));
					}
					else
					{
						Robot.shooter.shooterSpinup(Robot.shooter.getAimSpeed(Vision.getBoilerDist()));
					}
				}
				else
				{
					if(Vision.getBoilerAngleOffset() < 0)
					{
						Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, (-Vision.getBoilerAngleOffset()-2.0) / 10.0);
						Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble, 0);
					}
					else
					{
						Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, 0);
						Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble,(Vision.getBoilerAngleOffset()-2.0) / 10.00);
					}
				}
			}
			else
			{
				Robot.shooter.stop();
				Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, 0);
				Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble, 0);
			}
		}
		else
		{
			if(Robot.oi.drive2Controller.getBumper(Hand.kLeft))
			{
				Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, 1);
				Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble, 1);
			}
			else
			{
				Robot.oi.drive2Controller.setRumble(RumbleType.kLeftRumble, 0);
				Robot.oi.drive2Controller.setRumble(RumbleType.kRightRumble, 0);
			}
		}
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
