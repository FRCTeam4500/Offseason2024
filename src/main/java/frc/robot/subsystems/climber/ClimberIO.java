package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.EZLogger.LogAccess;
import frc.robot.utilities.EZLogger.Loggable;

public abstract class ClimberIO extends SubsystemBase implements Loggable {
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

    @Override
    public void log(LogAccess table) {
        ClimberState state = getState();
        table.put("Current Extension", state.currentExtension);
        table.put("Target Extension", state.targetExtension);
    }

    public abstract Command extend();
    public abstract Command retract();
    public abstract ClimberState getState();

    public static class ClimberState {
        public double currentExtension;
        public double targetExtension;
    }
}

