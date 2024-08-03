package frc.robot.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.function.BooleanConsumer;
import edu.wpi.first.util.function.FloatConsumer;
import edu.wpi.first.util.function.FloatSupplier;
import edu.wpi.first.util.sendable.SendableBuilder;

public class EZSendableBuilder implements SendableBuilder {
    private HashMap<String, Runnable> pushToNT;
    private HashMap<String, Runnable> grabFromNT;
    private HashMap<String, Runnable> pushToFile;
    private String dir;
    private String name;
    public boolean logToNT;
    public boolean logToFile;
    private NetworkTable table;
    private DataLog log;
    private List<AutoCloseable> closeables;

    public EZSendableBuilder(String name, String dir, DataLog file) {
        this.dir = dir;
        this.name = name;
        if (!this.dir.endsWith("/")) {
            this.dir += "/";
        }
        if (!this.dir.startsWith("EZLogger/")) {
            this.dir = "EZLogger/" + this.dir;
        }
        table = NetworkTableInstance.getDefault().getTable(this.dir + name);
        table.getEntry(".name").setString(name);
        log = file;
        pushToNT = new HashMap<>();
        grabFromNT = new HashMap<>();
        pushToFile = new HashMap<>();
        closeables = new ArrayList<>();
    }

    @Override
    public void close() throws Exception {
        for (AutoCloseable closeable : closeables) {
            closeable.close();
        }
    }

    @Override
    public void setSmartDashboardType(String type) {
        publishConstString(".type", type);
    }

    @Override
    public void setActuator(boolean value) {
        publishConstBoolean(".actuator", value);
    }

    @Override
    public void setSafeState(Runnable func) {}

    @Override
    public void addBooleanProperty(String key, BooleanSupplier getter, BooleanConsumer setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setBoolean(getter.getAsBoolean());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getBoolean(false));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "boolean");
            log.appendBoolean(index, getter.getAsBoolean(), 0);
        });
    }

    @Override
    public void publishConstBoolean(String key, boolean value) {
        if (logToNT) {
            table.getEntry(key).setBoolean(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "boolean");
            log.appendBoolean(index, value, 0);
        }
    }

    @Override
    public void addIntegerProperty(String key, LongSupplier getter, LongConsumer setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setInteger(getter.getAsLong());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getInteger(0));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "int64");
            log.appendInteger(index, getter.getAsLong(), 0);
        });
    }

    @Override
    public void publishConstInteger(String key, long value) {
        if (logToNT) {
            table.getEntry(key).setInteger(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "int64");
            log.appendInteger(index, value, 0);
        }
    }

    @Override
    public void addFloatProperty(String key, FloatSupplier getter, FloatConsumer setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setFloat(getter.getAsFloat());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getFloat(0));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "float");
            log.appendFloat(index, getter.getAsFloat(), 0);
        });
    }

    @Override
    public void publishConstFloat(String key, float value) {
        if (logToNT) {
            table.getEntry(key).setFloat(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "float");
            log.appendFloat(index, value, 0);
        }
    }

    @Override
    public void addDoubleProperty(String key, DoubleSupplier getter, DoubleConsumer setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setDouble(getter.getAsDouble());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getDouble(0));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "double");
            log.appendDouble(index, getter.getAsDouble(), 0);
        });
    }

    @Override
    public void publishConstDouble(String key, double value) {
        if (logToNT) {
            table.getEntry(key).setDouble(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "double");
            log.appendDouble(index, value, 0);
        }
    }

    @Override
    public void addStringProperty(String key, Supplier<String> getter, Consumer<String> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setString(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getString(""));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "string");
            log.appendString(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstString(String key, String value) {
        if (logToNT) {
            table.getEntry(key).setString(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "string");
            log.appendString(index, value, 0);
        }
    }

    @Override
    public void addBooleanArrayProperty(String key, Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setBooleanArray(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getBooleanArray(new boolean[0]));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "boolean[]");
            log.appendBooleanArray(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstBooleanArray(String key, boolean[] value) {
        if (logToNT) {
            table.getEntry(key).setBooleanArray(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "boolean[]");
            log.appendBooleanArray(index, value, 0);
        }
    }

    @Override
    public void addIntegerArrayProperty(String key, Supplier<long[]> getter, Consumer<long[]> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setIntegerArray(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getIntegerArray(new long[0]));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "int64[]");
            log.appendIntegerArray(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstIntegerArray(String key, long[] value) {
        if (logToNT) {
            table.getEntry(key).setIntegerArray(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "int64[]");
            log.appendIntegerArray(index, value, 0);
        }
    }

    @Override
    public void addFloatArrayProperty(String key, Supplier<float[]> getter, Consumer<float[]> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setFloatArray(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getFloatArray(new float[0]));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "float[]");
            log.appendFloatArray(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstFloatArray(String key, float[] value) {
        if (logToNT) {
            table.getEntry(key).setFloatArray(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "float[]");
            log.appendFloatArray(index, value, 0);
        }
    }

    @Override
    public void addDoubleArrayProperty(String key, Supplier<double[]> getter, Consumer<double[]> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setDoubleArray(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getDoubleArray(new double[0]));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "double[]");
            log.appendDoubleArray(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstDoubleArray(String key, double[] value) {
        if (logToNT) {
            table.getEntry(key).setDoubleArray(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "double[]");
            log.appendDoubleArray(index, value, 0);
        }
    }

    @Override
    public void addStringArrayProperty(String key, Supplier<String[]> getter, Consumer<String[]> setter) {
        pushToNT.put(key, () -> {
            table.getEntry(key).setStringArray(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getEntry(key).getStringArray(new String[0]));
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, "string[]");
            log.appendStringArray(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstStringArray(String key, String[] value) {
        if (logToNT) {
            table.getEntry(key).setStringArray(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, "string[]");
            log.appendStringArray(index, value, 0);
        }
    }

    @Override
    public void addRawProperty(String key, String typeString, Supplier<byte[]> getter, Consumer<byte[]> setter) {
        pushToNT.put(key, () -> {
            table.getRawTopic(key).publish(typeString).accept(getter.get());
        });
        grabFromNT.put(key, () -> {
            setter.accept(table.getRawTopic(key).subscribe(typeString, new byte[0]).get());
        });
        pushToFile.put(key, () -> {
            int index = log.start(dir + name + key, typeString);
            log.appendRaw(index, getter.get(), 0);
        });
    }

    @Override
    public void publishConstRaw(String key, String typeString, byte[] value) {
        if (logToNT) {
            table.getRawTopic(key).publish(typeString).accept(value);
        }
        if (logToFile) {
            int index = log.start(dir + name + key, typeString);
            log.appendRaw(index, value, 0);
        }
    }

    @Override
    public BackendKind getBackendKind() {
        return BackendKind.kUnknown;
    }

    @Override
    public boolean isPublished() {
        return true;
    }

    @Override
    public void update() {
        if (logToFile) {
            for (Runnable runnable : pushToFile.values()) {
                runnable.run();
            }
        }
        if (logToNT) {
            for (Runnable runnable : grabFromNT.values()) {
                runnable.run();
            }
            for (Runnable runnable : pushToNT.values()) {
                runnable.run();
            }
        }
    }

    @Override
    public void clearProperties() {
        pushToFile.clear();
        grabFromNT.clear();
        pushToNT.clear();
    }

    @Override
    public void addCloseable(AutoCloseable closeable) {
        closeables.add(closeable);
    }
    
}
