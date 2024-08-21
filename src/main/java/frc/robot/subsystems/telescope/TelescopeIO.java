package frc.robot.subsystems.telescope;

import dev.doglog.DogLog;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class TelescopeIO extends SubsystemBase {
    private static TelescopeIO instance;
    public static synchronized TelescopeIO getInstance() {
        if (instance == null) {
            if (RobotBase.isReal()) {
                instance = null;
            } else {
                instance = null;
            }
        }
        return instance;
    }

    public void log() {
        TelescopeState state = getState();
        DogLog.log("Telescope/Current Extension", state.currentExtension);
        DogLog.log("Telescope/Target Extension", state.targetExtension);
    }

    public abstract Command extend(double extension);
    public abstract Command retract();
    public abstract TelescopeState getState();

    public static class TelescopeState {
        public double currentExtension;
        public double targetExtension;
    }
}
