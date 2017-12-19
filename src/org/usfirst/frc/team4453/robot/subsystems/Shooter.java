package org.usfirst.frc.team4453.robot.subsystems;

import org.usfirst.frc.team4453.library.Tilt;
import org.usfirst.frc.team4453.robot.RobotMap;
import org.usfirst.frc.team4453.robot.commands.AutoShooterControl;
import org.usfirst.frc.team4453.robot.commands.ShooterControlWithJoystick;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class Shooter extends Subsystem {

	// Shooter
	private static CANTalon shooter;
	private static Solenoid shooterGateO;
	private static Solenoid shooterGateC;
	// Shooter Constants
	private static final int SHOOTER_ENCODER_PULSES_PER_REV = 12;
	private static final double SHOOTER_SPINUP_SPEED = 500; // TODO: Fix this.
	private static final double[][] SHOOTER_TABLE = {{175 + 17.5, .852, 33}, 
													 {52 + 17.5, .77, 21},
													 {17.5, .70, 11.3}};
	// Tilt
	private static CANTalon tilt;
	// Tilt Constants
	private static final int TILT_ENCODER_PULSES_PER_REV = 560;	// NeveRest20 = 560 counts per rev
	private static final double TILT_LEAD_SCREW_PITCH = 0.25;
	private static final double TILT_ENCODER_DIST_PER_PULSE = TILT_LEAD_SCREW_PITCH / TILT_ENCODER_PULSES_PER_REV;
	private static final double TILT_MIN = Tilt.calcSide(0.0) / TILT_ENCODER_DIST_PER_PULSE;
	private static final double TILT_MAX = Tilt.calcSide(50.0) / TILT_ENCODER_DIST_PER_PULSE;

	// Jaw
	private static CANTalon yaw;
	// Jaw Constants
	private static final int YAW_ENCODER_PULSES_PER_REV = 560;	// NeveRest20 = 560 counts per rev
	private static final double YAW_GEARING_RATIO = 10.5 / 1.15;	// Motor revolutions per yaw rotation. TODO: Figure this out.
	private static final double YAW_PULSES_PER_DEGREE = (YAW_ENCODER_PULSES_PER_REV / 360) * YAW_GEARING_RATIO;
	private static final double YAW_MIN = -45 * YAW_PULSES_PER_DEGREE;
	private static final double YAW_MAX = 45 * YAW_PULSES_PER_DEGREE;

	// Initialize your subsystem here
	public Shooter() {
		System.out.println("Shooter starting...");

		// Shooter Setup.
		shooter = new CANTalon(RobotMap.SHOOTER_MOTOR);
		shooter.changeControlMode(TalonControlMode.Speed);
		shooter.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooter.configEncoderCodesPerRev(SHOOTER_ENCODER_PULSES_PER_REV);
		shooter.setPID(9, 0.001, 0.8, 0, 0, 0, 0);
		shooter.enable();
		shooterGateO = new Solenoid(RobotMap.SHOOTER_OPEN);
		shooterGateC = new Solenoid(RobotMap.SHOOTER_CLOSE);

		// Tilt Setup.
		tilt = new CANTalon(RobotMap.TILT_MOTOR);
		tilt.changeControlMode(TalonControlMode.Position);
		tilt.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		tilt.configEncoderCodesPerRev(TILT_ENCODER_PULSES_PER_REV);
		tilt.setPID(1, 0, 0, 0, 0, 0, 0); // TODO: set this.
//		tilt.setForwardSoftLimit(TILT_MAX);
//		tilt.enableForwardSoftLimit(true);
//		tilt.setReverseSoftLimit(TILT_MIN);
//		tilt.enableReverseSoftLimit(true);
		tilt.enable();

		// Swivel Setup.
		yaw = new CANTalon(RobotMap.YAW_MOTOR);
		yaw.changeControlMode(TalonControlMode.Position);
		yaw.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		yaw.configEncoderCodesPerRev(YAW_ENCODER_PULSES_PER_REV);
		yaw.setPID(1, 0, 0, 0, 0, 0, 0); // TODO: and this.
//		yaw.setForwardSoftLimit(YAW_MAX);
//		yaw.enableForwardSoftLimit(true);
//		yaw.setReverseSoftLimit(YAW_MIN);
//		yaw.enableReverseSoftLimit(true);
		yaw.enable();

		// Setup LiveWindow for Test Mode
		LiveWindow.addActuator("Shooter", "Shooter CIM", shooter);
		LiveWindow.addActuator("Tilt", "Master CIM", tilt);
		LiveWindow.addActuator("Yaw", "Master CIM", yaw);

		System.out.println("Shooter is running");
	}

	@Override
	public void initDefaultCommand() {
//		setDefaultCommand(new AutoShooterControl());
		setDefaultCommand(new ShooterControlWithJoystick());
	}

	public double tiltGetDist() {
		return tilt.getPosition() * TILT_ENCODER_DIST_PER_PULSE;
	}

	public void tiltSetDist(double dist) {
		tilt.setPosition(dist / TILT_ENCODER_DIST_PER_PULSE);
	}

	public double tiltGetAngle() {
		return Tilt.calcAngle(tiltGetDist());
	}

	public void tiltSetAngle(double angle) {
		tiltSetSetPoint(Tilt.calcSide(angle));
	}

	public double tiltGetSetPoint() {
		return tilt.getSetpoint();
	}

	public void tiltSetSetPoint(double setPoint) {
		tilt.changeControlMode(TalonControlMode.Position);
		tilt.setSetpoint(setPoint);
	}

	public boolean tiltGetLowerLimit() {
		return tilt.isRevLimitSwitchClosed();
	}

	public void tiltLower() {
		tilt.changeControlMode(TalonControlMode.PercentVbus);
		tilt.set(1.0);
	}

	public void tiltRaise() {
		tilt.changeControlMode(TalonControlMode.PercentVbus);
		tilt.set(-1.0);
	}

	public void tiltStop() {
		tilt.changeControlMode(TalonControlMode.PercentVbus);
		tilt.set(0.0);
	}

	public void tiltReset() {
		while (!tiltGetLowerLimit()) {
			tiltLower();
		}

		tiltStop();
		tiltSetDist(0);
	}

	public double shooterGetSpeed() {
		return shooter.getSpeed();
	}

	public void shooterSetSpeed(double spd) {
		shooter.set(spd);
	}

	public void shooterSpinup(double speed) {
		shooterSetSpeed(speed);
	}

	public void shooterFire(double speed) {
		shooterGateO.set(true);
		shooterGateC.set(false);
		shooterSetSpeed(speed);
	}

	public void shooterStopFire() {
		shooterGateO.set(false);
		shooterGateC.set(true);
	}
	
	public void shooterStop() {
		shooterStopFire();
		shooterSetSpeed(0.0);
	}

	public void yawSetAngle(double angle) {
		yaw.changeControlMode(TalonControlMode.Position);
		yaw.set(angle * YAW_PULSES_PER_DEGREE);
	}
	
	public double yawGetSetpoint() {
		return yaw.getSetpoint() / YAW_PULSES_PER_DEGREE;
	}

	public double yawGetAngle() {
		return yaw.getPosition() / YAW_PULSES_PER_DEGREE;
	}

	public boolean yawHitLimit() {
		return yaw.isRevLimitSwitchClosed() | yaw.isFwdLimitSwitchClosed();
	}
	
	public void yawStop()
	{
		yaw.changeControlMode(TalonControlMode.PercentVbus);
		yaw.set(0);
	}

	public boolean yawIsReady(double tol) {
		return Math.abs(yaw.get() - yawGetAngle()) < tol;
	}

	public boolean shooterIsReady(double tol) {
		return Math.abs(shooterGetSpeed() - SHOOTER_SPINUP_SPEED) < tol; // For later, when we have encoders..
	}

	public void stop()
	{
		shooterStop();
		yawStop();
		tiltStop();
	}
	
	public double getAimSpeed(double dist) {
		double[] lower = {
				0, 0, 0
		};
		double[] upper = {
				10000000, 0, 0
		};
		for (int i = 0; i < SHOOTER_TABLE.length; i++) {
			if (lower[0] > SHOOTER_TABLE[i][0] && SHOOTER_TABLE[i][0] <= dist) {
				lower = SHOOTER_TABLE[i];
			}
			if (upper[0] < SHOOTER_TABLE[i][0] && SHOOTER_TABLE[i][0] >= dist) {
				upper = SHOOTER_TABLE[i];
			}
		}

		double lowerbias = (upper[0] - dist) / (upper[0] - lower[0]);
		double upperbias = (dist - lower[0]) / (upper[0] - lower[0]);

		return (lower[1] * lowerbias + upper[1] * upperbias);
	}

	public double getAimAngle(double dist) {
		double[] lower = {
				0, 0, 0
		};
		double[] upper = {
				10000000, 0, 0
		};
		for (int i = 0; i < SHOOTER_TABLE.length; i++) {
			if (lower[0] > SHOOTER_TABLE[i][0] && SHOOTER_TABLE[i][0] <= dist) {
				lower = SHOOTER_TABLE[i];
			}
			if (upper[0] < SHOOTER_TABLE[i][0] && SHOOTER_TABLE[i][0] >= dist) {
				upper = SHOOTER_TABLE[i];
			}
		}

		double lowerbias = (upper[0] - dist) / (upper[0] - lower[0]);
		double upperbias = (dist - lower[0]) / (upper[0] - lower[0]);

		return (lower[2] * lowerbias + upper[2] * upperbias);
	}
}
