package frc.robot.subsystems.shooter;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.EZLogger.LogAccess;
import frc.robot.utilities.EZLogger.Loggable;

public abstract class ShooterIO extends SubsystemBase implements Loggable {
    private static ShooterIO instance;
    public static synchronized ShooterIO getInstance() {
        if (instance == null) {
            if (RobotBase.isReal()) {
                instance = null;
            } else {
                instance = null;
            }
        }
        return instance;
    }

    @Override
    public void log(LogAccess table) {
        ShooterState state = getState();
        table.put("Current Tilt", state.currentTilt);
        table.put("Target Tilt", state.targetTilt);
        table.put("Current Loader Speed", state.currentLoaderSpeed);
        table.put("Target Loader Speed", state.targetLoaderSpeed);
        table.put("Current Shooter Speed", state.currentShooterSpeed);
        table.put("Target Shooter Speed", state.targetShooterSpeed);
    }

    public abstract Command aim(Supplier<Translation2d> translation);
    public abstract Command amp();
    public abstract Command sourceIntake();
    public abstract Command subwoofer();
    public abstract Command handoff();
    public abstract Command eject();
    public abstract Command shoot();
    public abstract ShooterState getState();

    public static class ShooterState {
        public double currentTilt;
        public double targetTilt;
        public double currentLoaderSpeed;
        public double targetLoaderSpeed;
        public double currentShooterSpeed;
        public double targetShooterSpeed;
    }
}
