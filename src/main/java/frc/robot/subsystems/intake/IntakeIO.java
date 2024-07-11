package frc.robot.subsystems.intake;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.EZLogger.LogAccess;
import frc.robot.utilities.EZLogger.Loggable;

public abstract class IntakeIO extends SubsystemBase implements Loggable {
    private static IntakeIO instance;
    public static synchronized IntakeIO getInstance() {
        if (instance == null) {
            if (RobotBase.isReal()) {
                instance = null;
            } else {
                instance = new IntakeSim();
            }
        }
        return instance;
    }

    public final Constraints CONSTRAINTS = new Constraints(270, 250);

    @Override
    public void log(LogAccess table) {
        IntakeState state = getState();
        table.put("Current Tilt", state.currentTilt);
        table.put("Target Tilt", state.targetTilt);
        table.put("Current Speed", state.currentSpeed);
        table.put("Target Speed", state.targetSpeed);
    }

    public abstract Command extend();
    public abstract Command handoff();
    public abstract IntakeState getState();

    public static class IntakeState {
        public double currentTilt;
        public double targetTilt;
        public double currentSpeed;
        public double targetSpeed;
    }
}
