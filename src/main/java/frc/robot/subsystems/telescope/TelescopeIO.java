package frc.robot.subsystems.telescope;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import dev.doglog.DogLog;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class TelescopeIO extends SubsystemBase {
    private static TelescopeIO instance;
    public static synchronized TelescopeIO getInstance() {
        if (instance == null) {
            if (RobotBase.isReal()) {
                instance = null;
            } else {
                instance = new TelescopeSim();
            }
        }
        return instance;
    }

    public final Constraints TELESCOPE_CONSTRAINTS = new Constraints(1, 2);
    public final MechanismLigament2d TELESCOPE_MECH = new MechanismLigament2d("Telescope", 0.4064, 105);
    public void log() {
        TelescopeState state = getState();
        DogLog.log("Telescope/Current Extension", state.currentExtension);
        DogLog.log("Telescope/Target Extension", state.targetExtension);
        TELESCOPE_MECH.setLength(0.4064 + state.currentExtension);
    }

    public abstract Command extend(double extension);
    public abstract Command retract();
    public abstract TelescopeState getState();

    public static class TelescopeState {
        public double currentExtension;
        public double targetExtension;
    }
}
