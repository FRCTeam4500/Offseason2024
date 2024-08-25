package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.climber.ClimberIO;
import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.shooter.ShooterIO;
import frc.robot.subsystems.swerve.SwerveIO;
import frc.robot.subsystems.telescope.TelescopeIO;

public class Superstructure {
    private SwerveIO swerve;
    private IntakeIO intake;
    private ShooterIO shooter;
    private TelescopeIO telescope;
    private ClimberIO climber;
    private Mechanism2d robotMech;
    private Supplier<Translation2d> poseSupplier;

    public Superstructure(
        SwerveIO swerve,
        IntakeIO intake, 
        ShooterIO shooter, 
        TelescopeIO telescope, 
        ClimberIO climber
    ) {
        this.swerve = swerve;
        this.intake = intake;
        this.shooter = shooter;
        this.telescope = telescope;
        this.climber = climber;
        robotMech = new Mechanism2d(1.372, 1.2192);
        poseSupplier = () -> swerve.getState().pose().getTranslation();
        configureMech();
    }

    private void configureMech() {
        robotMech.getRoot("Intake Root", 1, 0.1).append(intake.INTAKE_MECH);
        robotMech.getRoot("Telescope Root", 0.483, 0.1524).append(telescope.TELESCOPE_MECH);
        telescope.TELESCOPE_MECH.append(shooter.SHOOTER_MECH);
        SmartDashboard.putData("Robot Mech", robotMech);
    }

    public void log() {
        intake.log();
        shooter.log();
        telescope.log();
        // climber.log();
    }

    public void setDefaultSwerveCommand(CommandXboxController xbox) {
        swerve.setDefaultCommand(swerve.angleCentric(xbox));
    }

    public Command turnToSpeaker(CommandXboxController xbox) {
        return swerve.speakerCentric(xbox).withName("Speaker Centric Drive");
    }

    public Command startIntaking() {
        return intake.extend().withName("Start Intaking");
    }

    public Command handoff() {
        return shooter.aim(() -> -10)
            .alongWith(telescope.retract())
            .andThen(intake.handoff())
            .withName("Handoff");
    }

    public Command aimSpeaker() {
        return shooter.load(5)
            .andThen(
                shooter.aim(poseSupplier)
                .alongWith(telescope.extend(0.1))
            ).withName("Ready Speaker Shot");
    }

    public Command stow() {
        return telescope.retract()
            .alongWith(shooter.aim(() -> 0))
            .andThen(shooter.load(0))
            .andThen(shooter.shooter(0))
            .andThen(intake.handoff())
            .withName("Stow");
    }
}
