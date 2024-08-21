package frc.robot.subsystems.intake;

import dev.doglog.DogLog;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IntakeIO extends SubsystemBase {
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
    public final MechanismLigament2d INTAKE_MECH = new MechanismLigament2d("Intake", 0.3, 135);

    public void log() {
        IntakeState state = getState();
        DogLog.log("Intake/Current Tilt", state.currentTilt);
        DogLog.log("Intake/Target Tilt", state.targetTilt);
        DogLog.log("Intake/Current Speed", state.currentSpeed);
        DogLog.log("Intake/Target Speed", state.targetSpeed);
        INTAKE_MECH.setAngle(state.currentTilt);
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
