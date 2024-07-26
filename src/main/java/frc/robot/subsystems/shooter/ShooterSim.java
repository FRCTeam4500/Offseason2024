package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.utilities.ExtendedMath;

public class ShooterSim extends ShooterIO {
    private ShooterState shooterState;
    private State currentTilt;
    private State targetTilt;
    private TrapezoidProfile profile;
    public ShooterSim() {
        shooterState = new ShooterState();
        currentTilt = new State(-15, 0);
        targetTilt = new State(-15, 0);
        profile = new TrapezoidProfile(SHOOTER_CONSTRAINTS);
    }

    @Override
    public Command aim(DoubleSupplier angle) {
        return Commands.runOnce(
            () -> targetTilt.position = angle.getAsDouble()
        ).andThen(Commands.run(() -> {
            targetTilt.position = angle.getAsDouble();
            currentTilt = profile.calculate(
                0.02,
                currentTilt,
                targetTilt
            );
        }, this)).until(() -> ExtendedMath.within(currentTilt.position, targetTilt.position, 1));
    }

    @Override
    public Command aim(Supplier<Translation2d> pose) {
        return aim(() -> {
            Translation2d speakerPose = new Translation2d(
                DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue ?
                -0.5 : 16.5, 5.9
            );
            return ANGLE_MAP.get(pose.get().getDistance(speakerPose));
        });
    }

    @Override
    public Command load(double speed) {
        return Commands.runOnce(() -> {
            shooterState.currentLoaderSpeed = speed;
            shooterState.targetLoaderSpeed = speed;
        });
    }

    @Override
    public Command shooter(double speed) {
        return Commands.runOnce(() -> {
            shooterState.currentShooterSpeed = speed;
            shooterState.targetShooterSpeed = speed;
        });
    }

    @Override
    public ShooterState getState() {
        shooterState.currentTilt = currentTilt.position;
        shooterState.targetTilt = targetTilt.position;
        return shooterState;
    }
}
