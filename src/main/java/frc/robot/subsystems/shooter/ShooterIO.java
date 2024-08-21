package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import dev.doglog.DogLog;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterIO extends SubsystemBase {
    private static ShooterIO instance;
    public static synchronized ShooterIO getInstance() {
        if (instance == null) {
            if (RobotBase.isReal()) {
                instance = null;
            } else {
                instance = new ShooterSim();
            }
        }
        return instance;
    }

    protected final Constraints SHOOTER_CONSTRAINTS = new Constraints(180, 360);
    protected static final InterpolatingDoubleTreeMap ANGLE_MAP = new InterpolatingDoubleTreeMap();
    public static final double FF_OFFSET = 26.5;
    public final MechanismLigament2d SHOOTER_MECH = new MechanismLigament2d("Shooter", 0.3, -38);
    static {
        ANGLE_MAP.put(1.37, -44.428);
        ANGLE_MAP.put(1.74, -36.71);
        ANGLE_MAP.put(2.14, -26.43);
        ANGLE_MAP.put(2.8, -21.29);
        ANGLE_MAP.put(3.25, -18.71);
        ANGLE_MAP.put(3.75, -16.14);
        ANGLE_MAP.put(4.4, -14.34);
    }

    public void log() {
        ShooterState state = getState();
        DogLog.log("Shooter/Current Tilt", state.currentTilt);
        DogLog.log("Shooter/Target Tilt", state.targetTilt);
        DogLog.log("Shooter/Current Loader Speed", state.currentLoaderSpeed);
        DogLog.log("Shooter/Target Loader Speed", state.targetLoaderSpeed);
        DogLog.log("Shooter/Current Shooter Speed", state.currentShooterSpeed);
        DogLog.log("Shooter/Target Shooter Speed", state.targetShooterSpeed);
        SHOOTER_MECH.setAngle(state.currentTilt - 105);
    }

    public abstract Command aim(DoubleSupplier angle);
    public abstract Command aim(Supplier<Translation2d> pose);
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
