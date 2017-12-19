package org.usfirst.frc.team4453.library;

import org.usfirst.frc.team4453.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Library class is a collection of utility functions used throughout the
 * robot code.
 */

public class Tilt {

	private static final double MPI = 2.54 / 100.0; // Meters per inch
	private static final double G = 9.80665; // acceleration due to gravity in meters per second
	/*
	 * Tilt Robot constants
	 */
	private static final double TILT_TRI_SIDE_A = 6.7268120235; // from pivot to base mount
	private static final double TILT_TRI_SIDE_B = 5.25; // from pivot to screw mount
	private static final double TILT_TRI_SIDE_C = 2.48175566; // lead screw length at zero distance
	private static final double TILT_TRI_ANGLE = 48.01278750; // angle at zero distance

	/*
	 * Tilt Equation constants
	 */
	private static final double TILT_TRI_FACTOR_1 = Math.pow(TILT_TRI_SIDE_A, 2) + Math.pow(TILT_TRI_SIDE_B, 2);
	private static final double TILT_TRI_FACTOR_2 = 2 * (TILT_TRI_SIDE_A * TILT_TRI_SIDE_B);

	// make the constructor private, so no one can instantiate this class
	private Tilt() {
	}

	/**
	 * Convert a tilt angle to the length of the tilt lead screw.
	 * 
	 * @param angle
	 *            double - tilt angle (degrees)
	 * @return double - tilt lead screw length (inches)
	 */
	public static double calcSide(double angle) {
		return (Math.sqrt(TILT_TRI_FACTOR_1 - (TILT_TRI_FACTOR_2 * Math.cos(Math.toRadians(angle + TILT_TRI_ANGLE)))) - TILT_TRI_SIDE_C);
	}

	/**
	 * Convert the tilt lead screw distance to tilt angle
	 * 
	 * @param dist
	 *            double - tilt lead screw distance (inches)
	 * @return double - tilt angle (degrees)
	 */
	public static double calcAngle(double dist) {
		return (Math.toDegrees(Math.acos((TILT_TRI_FACTOR_1 - Math.pow(dist + TILT_TRI_SIDE_C, 2)) / TILT_TRI_FACTOR_2)) - TILT_TRI_ANGLE);
	}

	public static double calcShootAngle(boolean test) {
		// All values must be in meters for physics calculations
		double shootingAngle = 0; // TODO = Vision.getShootAngle();
		double robotAngle;
		if (test) {
			robotAngle = 0.0;
		}
		else {
			robotAngle = Robot.ahrs.getPitch(); // Get pitch from the AHRS
		}

		// displays important values on the dashboard
		if (!test) {
			SmartDashboard.putNumber("Shoot Deg", shootingAngle);
			SmartDashboard.putNumber("Robot Deg", robotAngle);
		}

		double calcShootAngle = shootingAngle - robotAngle;
		SmartDashboard.putNumber("calcShootAngle", calcShootAngle);
		return calcShootAngle;
	}

	/*
	 * Used for testing
	 */

	public static double getMPI() {
		return MPI;
	}

	public static double getG() {
		return G;
	}
}
