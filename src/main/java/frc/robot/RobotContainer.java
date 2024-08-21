// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import dev.doglog.DogLog;
import dev.doglog.DogLogOptions;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.shooter.ShooterIO;
import frc.robot.subsystems.swerve.SwerveIO;
import frc.robot.subsystems.telescope.TelescopeIO;

public class RobotContainer {
    private CommandXboxController xbox;
    private Mechanism2d robotMech;
    private SwerveIO swerve;
    private IntakeIO intake;
    private ShooterIO shooter;
    private TelescopeIO telescope;
    public RobotContainer() {
        DriverStation.silenceJoystickConnectionWarning(true);
        xbox = new CommandXboxController(2);
        robotMech = new Mechanism2d(1.372, 1.2192);
        swerve = SwerveIO.getInstance();
        intake = IntakeIO.getInstance();
        shooter = ShooterIO.getInstance();
        telescope = TelescopeIO.getInstance();
        swerve.setDefaultCommand(swerve.angleCentric(xbox));
        configureButtons();
        configureAuto();
    }

    private void configureButtons() {
        Trigger rightStickUp = new Trigger(() -> xbox.getRightY() < -0.5);
        Trigger rightStickDown = new Trigger(() -> xbox.getRightY() > 0.5);
        Trigger onBlue = new Trigger(() -> 
            DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue);

        xbox.a().onTrue(swerve.resetGyro());
        rightStickUp.and(onBlue).or(rightStickDown.and(onBlue.negate()))
            .onTrue(swerve.targetAngle(Rotation2d.fromDegrees(0)));
        rightStickUp.and(onBlue.negate()).or(rightStickDown.and(onBlue))
            .onTrue(swerve.targetAngle(Rotation2d.fromDegrees(180)));
        xbox.rightStick().onTrue(swerve.targetAngle(Rotation2d.fromDegrees(-90)));
        xbox.leftBumper().onTrue(swerve.targetAngle(Rotation2d.fromDegrees(90)));
        xbox.x().onTrue(intake.extend()).onFalse(intake.handoff());
        xbox.b().whileTrue(shooter.aim(() -> swerve.getState().pose().getTranslation()).alongWith(telescope.extend(0.1)));
        xbox.b().onFalse(telescope.retract());
    }

    public Runnable configureLogging() {
        DogLogOptions homeOptions = new DogLogOptions(true, true, true, true, 1000);
        DogLogOptions compOptions = new DogLogOptions(false, true, true, true, 1000);
        DogLog.setEnabled(true);
        DogLog.setPdh(new PowerDistribution());
        DogLog.setOptions(homeOptions);

        SmartDashboard.putData("Robot Mech", robotMech);
        robotMech.getRoot("Intake Root", 1, 0.1).append(intake.INTAKE_MECH);
        robotMech.getRoot("Telescope Root", 0.483, 0.1524).append(telescope.TELESCOPE_MECH);
        telescope.TELESCOPE_MECH.append(shooter.SHOOTER_MECH);

        return () -> {
            DogLog.setOptions(
                DriverStation.isFMSAttached() ? compOptions : homeOptions
            );
            swerve.log();
            intake.log();
            shooter.log();
            telescope.log();
        };
    }

    private void configureAuto() {
        SendableChooser<Command> chooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", chooser);
        RobotModeTriggers.autonomous().whileTrue(Commands.deferredProxy(chooser::getSelected));
    }
}
