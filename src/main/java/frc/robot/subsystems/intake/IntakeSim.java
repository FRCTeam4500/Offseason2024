package frc.robot.subsystems.intake;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.utilities.ExtendedMath;

public class IntakeSim extends IntakeIO {
    private IntakeState intakeState;
    private TrapezoidProfile profile;
    private State currentState;
    private State targetState;
    public IntakeSim() {
        intakeState = new IntakeState();
        profile = new TrapezoidProfile(CONSTRAINTS);
        currentState = new State(135, 0);
        targetState = new State(135, 0);
    }

    @Override
    public Command extend() {
        return Commands.runOnce(() -> {
                targetState.position = -15;
                intakeState.targetSpeed = 5;
                intakeState.currentSpeed = 5;
            }, this
        ).andThen(Commands.run(() -> {
            currentState = profile.calculate(
                0.2,
                currentState,
                targetState
            );
        }).until(
            () -> ExtendedMath.within(currentState.position, targetState.position, 1)
        ));
    }

    @Override
    public Command handoff() {
        return Commands.runOnce(() -> {
                targetState.position = 135;
            }, this
        ).andThen(Commands.run(() -> {
            currentState = profile.calculate(
                0.2,
                currentState,
                targetState
            );
        }).until(
            () -> ExtendedMath.within(currentState.position, targetState.position, 1)
        ).andThen(Commands.runOnce(() -> {
            intakeState.currentSpeed = -5;
            intakeState.targetSpeed = -5;
        })).andThen(
            Commands.waitSeconds(0.5)
        ).andThen(Commands.runOnce(() -> {
            intakeState.currentSpeed = 0;
            intakeState.targetSpeed = 0;
        })));
    }

    @Override
    public IntakeState getState() {
        intakeState.currentTilt = currentState.position;
        intakeState.targetTilt = targetState.position;
        return intakeState;
    }
}
