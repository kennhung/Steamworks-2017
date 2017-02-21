package org.usfirst.frc.team2521.robot.commands.automation;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.subsystems.Drivetrain;
import org.usfirst.frc.team2521.robot.subsystems.Sensors;


/**
 * This command drives to the gear drop-off automatically.
 */
public class DriveToGear extends DriveToBlob {
	private static final double P = 0.025;
	private static final double I = 0;
	private static final double D = 0;

	public DriveToGear(boolean onLeftSide) {
		super(P, I, D, onLeftSide);
	}

	@Override
	protected double getSlowSpeed() {
		return Drivetrain.SLOW_SPEED;
	}

	@Override
	protected void initialize() {
		Robot.sensors.setCVCamera(Sensors.Camera.FRONT);
	}

	@Override
	protected boolean isFinished() {
		return !Robot.sensors.getBlobFound();
	}
}
