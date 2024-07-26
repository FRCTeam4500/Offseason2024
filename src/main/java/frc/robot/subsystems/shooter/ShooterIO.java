package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
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

    @Override
    public void log(LogAccess table) {
        ShooterState state = getState();
        table.put("Current Tilt", state.currentTilt);
        table.put("Target Tilt", state.targetTilt);
        table.put("Current Loader Speed", state.currentLoaderSpeed);
        table.put("Target Loader Speed", state.targetLoaderSpeed);
        table.put("Current Shooter Speed", state.currentShooterSpeed);
        table.put("Target Shooter Speed", state.targetShooterSpeed);
        SHOOTER_MECH.setAngle(state.currentTilt );
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
