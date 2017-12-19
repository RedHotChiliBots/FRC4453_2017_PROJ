package org.usfirst.frc.team4453.library;

import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

	private static NetworkTable table;

	// conversion constants
	private static final double MPI = 2.54 / 100.0; // Meters per inch

	// Camera constants
	private static final double FOV = 62.2; // FOV in degrees (Axis = 37.4; HD3000 = 68.5 (according to docs, we measured 45.329); Raspberry Pi Camera module v2.1 = 62.2 )

	// physical target size
	private static final double BOILERw = 15 * MPI; // Boiler width in meters
	private static final double GEARw = 8.25 * MPI; // Distance between centers of gear strips in meters

	private static final double CAMh = 36 * MPI;
	private static final double BOILERh = 97 * MPI; // Boiler height from floor (OVERALL height, not height of strips)

	private static final double noValue = -99.; // Resolution Y in pixels

	/**
	 * Called every so often to update Vision code, resets parameters for now.
	 */
	public static void update() {
		if (table == null) {
			System.out.println("Connecting to /Vision...");
			table = NetworkTable.getTable("/Vision");
		}
		else {
			table.putNumber("param:boilerHeight", BOILERh);
			table.putNumber("param:gearWidth", GEARw);
			table.putNumber("param:boilerWidth", BOILERw);
			table.putNumber("param:FOV", FOV);

			table.putNumber("param:VEL", 99);
			table.putNumber("param:G", Tilt.getG());

			NetworkTable.flush();
		}
	}

	/**
	 * Get the pixel position of the gear peg.
	 * 
	 * @return Position of target: {X, Y}, or {-99, -99} if not found.
	 */
	public static double[] getGearPos() {
		double[] tgtPos = {
				table.getNumber("gear:X", noValue), table.getNumber("gear:Y", noValue)
		};
		return tgtPos;
	}

	/**
	 * Get the pixel position of the boiler.
	 * 
	 * @return Position of target: {X, Y}, or {-99, -99} if not found.
	 */
	public static double[] getBoilerPos() {
		double[] tgtPos = {
				table.getNumber("boiler:X", noValue), table.getNumber("boiler:Y", noValue)
		};
		return tgtPos;
	}

	/**
	 * Returns if the boiler is visible.
	 * 
	 * @return true if found, false otherwise.
	 */
	public static boolean boilerVisible() {
		return table.getBoolean("boiler:Found", false);
	}

	/**
	 * Returns if the gear peg is visible.
	 * 
	 * @return true if found, false otherwise.
	 */
	public static boolean gearVisible() {
		return table.getBoolean("gear:Found", false);
	}

	/**
	 * Gets the distance (hypotenuse) to the gear peg.
	 * 
	 * @return Distance, in meters, to the target.
	 */
	public static double getGearDist() {
		return table.getNumber("gear:Distance", noValue);
	}

	/**
	 * Gets the distance (hypotenuse) to the boiler.
	 * 
	 * @return Distance, in meters, to the target.
	 */
	public static double getBoilerDist() {
		return table.getNumber("boiler:Distance", noValue);
	}

	/**
	 * Gets the angle of the gear peg from the center of the view.
	 * 
	 * @return Angle, in degrees, of the offset.;
	 */
	public static double getGearAngleOffset() {
		return table.getNumber("gear:angleOffset", noValue);
	}

	/**
	 * Gets the angle of the boiler from the center of the view.
	 * 
	 * @return Angle, in degrees, of the offset.;
	 */
	public static double getBoilerAngleOffset() {
		return table.getNumber("boiler:angleOffset", noValue);
	}

	public static double getMPI() {
		return MPI;
	}

	public static double getFOV() {
		return FOV;
	}

	public static double getCAMh() {
		return CAMh;
	}

	public static double getBOILERh() {
		return BOILERh;
	}

	/**
	 * Sets the current camera to be used. Should ideally be called before every
	 * use of vision.
	 * 
	 * @param name
	 *            The name of the camera (currently "boiler" or "gear")
	 * @return True, if the switch succeeded.
	 */
	public static boolean setCamera(String name, double timeout) {
		table.putString("param:setCamera", name);
		NetworkTable.flush();
		long startTime = System.nanoTime();
		while (table.getString("param:getCamera", "") != name) {
			if (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) > timeout) {
				return false;
			}
		}
		return true;
	}
}
