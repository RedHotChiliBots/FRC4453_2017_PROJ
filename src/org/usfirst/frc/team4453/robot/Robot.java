
package org.usfirst.frc.team4453.robot;

import org.usfirst.frc.team4453.library.Vision;
import org.usfirst.frc.team4453.robot.commands.AutoNoOp;
import org.usfirst.frc.team4453.robot.commands.AutoStratPrimary;
import org.usfirst.frc.team4453.robot.subsystems.Chassis;
import org.usfirst.frc.team4453.robot.subsystems.Bubbler;
import org.usfirst.frc.team4453.robot.subsystems.GearGrabber;
import org.usfirst.frc.team4453.robot.subsystems.Shooter;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	
	// subsystems
	public static Chassis chassis;
	public static Shooter shooter;
	public static Bubbler collector;
	public static GearGrabber geargrabber;

	// public static Accel accel;
	public static AHRS ahrs;
	public static OI oi;

	private static Command autonomousCommand;
	private static SendableChooser<Command> autoChooser;

	private double start_time;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		// initialize Sensors - do before subsystems to support references
		try {
			ahrs = new AHRS(SPI.Port.kMXP, (byte) 200);
		}
		catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		}

		// initialize Subsystems
		chassis = new Chassis();
		shooter = new Shooter();
		collector = new Bubbler();
		geargrabber = new GearGrabber();

		// initialize OI last to guarantee any required subsystems are
		// initialized first
		oi = new OI();

		// display subsystems on dashboard
		SmartDashboard.putData(chassis);
		SmartDashboard.putData(shooter);
		SmartDashboard.putData(collector);
		SmartDashboard.putData(geargrabber);

		// initialize autonomous command chooser //TODO add all the auto commands
		autoChooser = new SendableChooser<Command>();
		autoChooser.addDefault("NoOp", new AutoNoOp());
		autoChooser.addObject("Hopper-Boiler-Gear", new AutoStratPrimary()); 
		SmartDashboard.putData("Auto Mode", autoChooser);
		autonomousCommand = null;

		// initialize Smartdashboard
		start_time = Timer.getFPGATimestamp();

		SmartDashboard.putString("Vison Status", "N/A");

		Robot.ahrs.zeroYaw();

		telemetry();
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	@Override
	public void disabledInit() {
		chassis.stop();
		shooter.stop();
		collector.stop();
	}

	@Override
	public void disabledPeriodic() {
		chassis.stop();
		shooter.stop();
		collector.stop();
		
		telemetry();
		Vision.update();
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		// schedule the autonomous command from chooser
		autonomousCommand = autoChooser.getSelected();
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		telemetry();
		Vision.update();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		chassis.disable();
		chassis.stop();
		shooter.stop();
		collector.stop();

		// accel.setCalcErrors(false);
		// ahrs.setCalcErrors(false);
		// ahrs.setYawOffset(Math.toRadians(SmartDashboard.getNumber("YawOffset",
		// 0.0)));

		// Command tiltReset = new TiltReset();
		// tiltReset.start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		telemetry();
		Vision.update();
		Scheduler.getInstance().run();
	}

	/**
	 * 
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	private void telemetry() {

		/* Calculate/display effective update rate in hz */
		double delta_time = Timer.getFPGATimestamp() - start_time;
		double update_count = ahrs.getUpdateCount();
		if (update_count > 0) {
			double avg_updates_per_sec = delta_time / update_count;
			if (avg_updates_per_sec > 0.0) {
				SmartDashboard.putNumber("IMU_EffUpdateRateHz", 1.0 / avg_updates_per_sec);
			}
		}

		// NavX Info.
		SmartDashboard.putBoolean("IMU_Connected", ahrs.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", ahrs.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", ahrs.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", ahrs.getPitch());
		SmartDashboard.putNumber("IMU_Roll", ahrs.getRoll());
		SmartDashboard.putNumber("IMU_CompassHeading", ahrs.getCompassHeading());
		SmartDashboard.putNumber("IMU_FusedHeading", ahrs.getFusedHeading());
		SmartDashboard.putNumber("IMU_TotalYaw", ahrs.getAngle());
		SmartDashboard.putNumber("IMU_YawRateDPS", ahrs.getRate());
		SmartDashboard.putNumber("IMU_Accel_X", ahrs.getWorldLinearAccelX());
		SmartDashboard.putNumber("IMU_Accel_Y", ahrs.getWorldLinearAccelY());
		SmartDashboard.putBoolean("IMU_IsMoving", ahrs.isMoving());
		SmartDashboard.putBoolean("IMU_IsRotating", ahrs.isRotating());
		SmartDashboard.putNumber("Velocity_X", ahrs.getVelocityX());
		SmartDashboard.putNumber("Velocity_Y", ahrs.getVelocityY());
		SmartDashboard.putNumber("Displacement_X", ahrs.getDisplacementX());
		SmartDashboard.putNumber("Displacement_Y", ahrs.getDisplacementY());
		SmartDashboard.putNumber("IMU_Temp_C", ahrs.getTempC());
		AHRS.BoardYawAxis yaw_axis = ahrs.getBoardYawAxis();
		SmartDashboard.putString("YawAxisDirection", yaw_axis.up ? "Up" : "Down");
		SmartDashboard.putNumber("YawAxis", yaw_axis.board_axis.getValue());
	}
}
