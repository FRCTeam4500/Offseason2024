package frc.robot.subsystems.telescope;

import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.Command;

public class TelescopeSim extends TelescopeIO {
    private TelescopeState currentState;
    private State currentExtension;
    private State targetExtension;
    
    @Override
    public Command extend(double extension) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'extend'");
    }

    @Override
    public Command retract() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retract'");
    }

    @Override
    public TelescopeState getState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }
    
}
