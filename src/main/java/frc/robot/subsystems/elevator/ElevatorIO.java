public interface elevatorIO {
    @AutoLog
    public static class elevatorIOInputs {

    }
    
    public default void updateInputs(elevatorIOInputs inputs) {
    }
    
    public default void setVoltage(double volts) {
    }

    public default double getPosition(double elevatorPosition) {
        return elevatorPosition;
    }

    public default double getAngleRadsPerSec(double angleRads) {
    }

    public default void setSetpoint(double setpoint){
    }

    public default void runPID() {
    }

    public default void setP(double p) {}
    
    public default void setI(double i) {}

    public default void setD(double d) {}

    public default void setFF(double FF) {}

    public default double getP() { return P; }

    public default double getI() { return I; }

    public default double getD() { return D; }

    public default double getFF() { return FF; }

}
