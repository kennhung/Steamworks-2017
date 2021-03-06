package org.usfirst.frc.team2521.robot.commands.automation;

import org.usfirst.frc.team2521.robot.Robot;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static org.usfirst.frc.team2521.robot.subsystems.Sensors.Camera;

/**
 * This command drives to the gear drop-off automatically.
 */
public class DriveToGear extends PIDCommand {
	private static final double P = 0.015;
	private static final double I = 0;
	private static final double D = 0;

	private static final double CAMERA_PROJ_PLANE_DISTANCE = 216.226;

	private static final double GEAR_SLOW_SPEED = 0.3;

	/** true if we're straight-on */
	private boolean oriented;
	private boolean hasFoundBlob;

	public DriveToGear() {
		super(P, I, D);
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
		Robot.sensors.setCVCamera(Camera.Type.FRONT);
		Robot.camera.setRunning(true);
	}

	@Override
	protected void execute() {
		// Angle between camera line of sight and target
		Double offsetX = Robot.sensors.getCVOffsetX();
		if (offsetX == null) return;

		double targetAngle = Math.atan(offsetX / CAMERA_PROJ_PLANE_DISTANCE);

		// Convert angle to degrees
		targetAngle *= 180 / Math.PI;

		// Consider ourselves oriented when our angle error is less than 20
		oriented = Math.abs(targetAngle) < 10;

		targetAngle += Robot.sensors.getNavxAngle();
		setSetpoint(targetAngle);
	}

	@Override
	protected boolean isFinished() {
		return hasFoundBlob && !Robot.sensors.hasFoundBlob();
	}

	@Override
	protected final void usePIDOutput(double output) {
		if (Robot.DEBUG) {
			SmartDashboard.putNumber("Drive to gear output", output);
			SmartDashboard.putBoolean("Has found blob", hasFoundBlob);
			SmartDashboard.putBoolean("Oriented", oriented);
		}

		if (Robot.sensors.hasFoundBlob()) {
			hasFoundBlob = true;
			// If we are already oriented, drive straight
			if (oriented) {
				Robot.drivetrain.setLeft(GEAR_SLOW_SPEED);
				Robot.drivetrain.setRight(GEAR_SLOW_SPEED);
			} else if (output > 0) {
				Robot.drivetrain.setLeft(output);
			} else {
				Robot.drivetrain.setRight(-output);
			}
		} else if (hasFoundBlob) {
			Robot.drivetrain.setLeft(GEAR_SLOW_SPEED);
			Robot.drivetrain.setRight(GEAR_SLOW_SPEED);
		}
	}

	@Override
	protected double returnPIDInput() {
		return Robot.sensors.getNavxAngle();
	}

	@Override
	protected void end() {
		Robot.camera.setRunning(false);
	}
}
