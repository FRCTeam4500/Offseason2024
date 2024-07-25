package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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

    protected final InterpolatingDoubleTreeMap ANGLE_MAP = new InterpolatingDoubleTreeMap();

    public double getBestAngle(Translation2d currentPose) {
        Translation2d speakerPose = new Translation2d(
            DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue ?
            -0.5 : 16.5, 5.9
        );
        return ANGLE_MAP.get(speakerPose.getDistance(currentPose));
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

    public abstract Command aim(DoubleSupplier angle);
    public abstract Command load(double speed);
    public abstract Command shooter(double speed);
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
