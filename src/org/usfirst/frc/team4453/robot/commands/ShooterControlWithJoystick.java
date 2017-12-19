
package org.usfirst.frc.team4453.robot.commands;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 * DriveWithjoystick - gets commands from the joysticks defined in OI to send to
 * the shooter method to drive the robot.
 * 
 * Requires the shooter. Only command that can be running for the shooter.
 * 
 */
public class ShooterControlWithJoystick extends Command {

	private final double MAX_TILT_RATE = 0.01;
	private final double MAX_YAW_RATE = 0.01;
	private final double MAX_SPEED = 1000; // TODO
	
	
	public ShooterControlWithJoystick() {
		requires(Robot.shooter);
	}

	@Override
	protected void initialize() {
		System.out.println("ShooterControlWithJoystick");
	}

	@Override
	protected void execute() {
		double tiltCmd = Robot.shooter.tiltGetAngle();
		tiltCmd += Robot.oi.getRightYShooterStick() * MAX_TILT_RATE;
		Robot.shooter.tiltSetAngle(tiltCmd);
		
		double yawCmd = Robot.shooter.yawGetSetpoint();
		yawCmd += Robot.oi.getRightXShooterStick() * MAX_YAW_RATE;
		Robot.shooter.yawSetAngle(yawCmd);
		
		if(Robot.oi.drive2Controller.getBumper(Hand.kLeft))
		{
			Robot.shooter.shooterFire(MAX_SPEED * Robot.oi.getRightShooterTrigger());
		}
		else
		{
			Robot.shooter.shooterStopFire();
			Robot.shooter.shooterSetSpeed(MAX_SPEED * Robot.oi.getRightShooterTrigger());
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
