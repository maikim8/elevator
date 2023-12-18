package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.AutoLog;

public interface ElevatorIO {
    @AutoLog
    public static class elevatorIOInputs {

    }
    
    public default void updateInputs(elevatorIOInputs inputs) {
    }
    
    public default void setVoltage(double volts) {
    }

    public default double getPosition(double elevatorPosition) {
        return 0.0;
    }

    public default double getAngleRadsPerSec(double angleRads) {
        return 0.0;
    }

    public default void setPIDConstants(double p, double i, double d, double ff) {}

    public default void setSetpoint(double setpoint){
    }

    public default boolean atSetpoint(){
        return false;
    }

    public default void setBrake(boolean brake) {}

    public default void runPID() {
    }

    public default void setP(double p) {}
    
    public default void setI(double i) {}

    public default void setD(double d) {}

    public default void setFF(double FF) {}

    public default double getP() { return 0.0; }

    public default double getI() { return 0.0; }

    public default double getD() { return 0.0; }

    public default double getFF() { return 0.0; }

}
