package frc.robot.subsystems.telescope;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.utilities.ExtendedMath;

public class TelescopeSim extends TelescopeIO {
    private TelescopeState currentState;
    private State currentExtension;
    private State targetExtension;
    private TrapezoidProfile profile;

    public TelescopeSim() {
        currentState = new TelescopeState();
        currentExtension = new State();
        targetExtension = new State();
        profile = new TrapezoidProfile(TELESCOPE_CONSTRAINTS);
    }

    @Override
    public Command extend(double extension) {

        return Commands.runOnce(
            () -> targetExtension.position = extension
        ).andThen(Commands.run(() ->{
            currentExtension = profile.calculate(
                0.02, 
                currentExtension, 
                targetExtension
            );
        }, this)).until(() -> ExtendedMath.within(
            currentExtension.position, targetExtension.position, 0.01
        ));
    }

    @Override
    public Command retract() {
        return extend(0);
    }

    @Override
    public TelescopeState getState() {
        currentState.currentExtension = currentExtension.position;
        currentState.targetExtension = targetExtension.position;
        return currentState;
    }
}
