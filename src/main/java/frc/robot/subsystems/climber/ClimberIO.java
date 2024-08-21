package frc.robot.subsystems.climber;

import dev.doglog.DogLog;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ClimberIO extends SubsystemBase {
    private static ClimberIO instance;
    public static synchronized ClimberIO getInstance() {
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
        ClimberState state = getState();
        DogLog.log("Climber/Current Extension", state.currentExtension);
        DogLog.log("Climber/Target Extension", state.targetExtension);
    }

    public abstract Command extend();
    public abstract Command retract();
    public abstract ClimberState getState();

    public static class ClimberState {
        public double currentExtension;
        public double targetExtension;
    }
}

