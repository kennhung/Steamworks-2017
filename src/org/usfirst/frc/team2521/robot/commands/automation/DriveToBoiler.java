package org.usfirst.frc.team2521.robot.commands.automation;

import org.usfirst.frc.team2521.robot.Robot;

import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 * This command drives to the correct spot to shoot from automatically.
 */
public class DriveToBoiler extends PIDCommand {
	private static final double P = 0.003;
	private static final double I = 0;
	private static final double D = 0;

	private static final double DISTANCE_SETPOINT = 35;
	private static final double DISTANCE_ERROR_THRESHOLD = 3;

	public DriveToBoiler() {
		super(P, I, D);
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
		setSetpoint(DISTANCE_SETPOINT);
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(DISTANCE_SETPOINT - Robot.sensors.getRearUltraInches()) < DISTANCE_ERROR_THRESHOLD;
	}

	@Override
	protected final void usePIDOutput(double output) {
		double value = -Math.abs(output);
		Robot.drivetrain.setLeft(value);
		Robot.drivetrain.setRight(value);
	}

	@Override
	protected double returnPIDInput() {
		return Robot.sensors.getRearUltraInches();
	}
}
