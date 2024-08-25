package frc.robot.utilities;

import java.util.ArrayList;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlertSystem {
    private AlertSystem() {}
    private static ArrayList<Alert> errors = new ArrayList<>();
    private static ArrayList<Alert> warnings = new ArrayList<>();
    private static ArrayList<Alert> info = new ArrayList<>();

    static {
        SmartDashboard.putData("Alerts", new Sendable() {
            @Override
            public void initSendable(SendableBuilder builder) {
                builder.setSmartDashboardType("Alerts");
                builder.addStringArrayProperty("errors", () -> getText(errors), null);
                builder.addStringArrayProperty("warnings", () -> getText(warnings), null);
                builder.addStringArrayProperty("info", () -> getText(info), null);
            }
        });
    }

    public static void addAlert(Alert alert) {
        switch (alert.level) {
            case Error:
                errors.add(alert);
                break;
            case Warning:
                warnings.add(alert);
                break;
            case Info:
                info.add(alert);
                break;
        }
    }

    public static void removeAlert(Alert alert) {
        switch (alert.level) {
            case Error:
                errors.remove(alert);
                break;
            case Warning:
                warnings.remove(alert);
                break;
            case Info:
                info.remove(alert);
                break;
        }
    }

    private static String[] getText(ArrayList<Alert> alerts) {
        return alerts.stream()
            .map(alert -> alert.text)
            .toArray(String[]::new);
    }

    public static record Alert(String text, AlertLevel level) {};

    public static enum AlertLevel {
        Error, Warning, Info
    }
}