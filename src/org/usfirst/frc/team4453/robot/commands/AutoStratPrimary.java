package org.usfirst.frc.team4453.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutoStratPrimary extends CommandGroup {

	public AutoStratPrimary() {
		// TODO: Make these values real.
		addSequential(new AutoTurn(-99.0)); // Turn to front of hopper.
		addSequential(new AutoDriveDistance(-99.0)); // Drive to front of
														// hopper.
		addSequential(new AutoTurn(-99.0)); // Turn to hopper.
		addSequential(new AutoDriveDistance(-99.0)); // Hit the hopper to get
														// fuel.
		addSequential(new WaitCommand(2.5)); // Wait for the fuel to fall in.
		addSequential(new AutoShoot()); // Fire fuel.
		addSequential(new AutoDriveDistance(-99.0)); // Drive to gear hook.
		addSequential(new AutoTurn(-99.0)); // Turn to gear hook.
		addSequential(new AutoGearApproach(0.25)); // Approach gear.
		addSequential(new AutoDriveDistance(0.25)); // Put gear on.
		addSequential(new GrabberRelease()); // Release
		addSequential(new AutoDriveDistance(-0.25)); // Pull back.

	}
}
