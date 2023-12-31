package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

import static frc.robot.Constants.Elevator.*;
import static frc.robot.Constants.Elevator.ElevatorPhysicalConstants.ELEVATOR_PID;

public class Elevator extends SubsystemBase {
    private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
    
    private LoggedDashboardNumber p = new LoggedDashboardNumber("Elevator/P", ELEVATOR_PID[0]);
    private LoggedDashboardNumber i = new LoggedDashboardNumber("Elevator/I", ELEVATOR_PID[1]);
    private LoggedDashboardNumber d = new LoggedDashboardNumber("Elevator/D", ELEVATOR_PID[2]);
    private LoggedDashboardNumber ff = new LoggedDashboardNumber("Elevator/FF", ELEVATOR_PID[3]);
   // Additional mechanism elements
    private MechanismLigament2d ElevatorArm;
    private MechanismLigament2d ElevatorPlatform;

    private double setpoint = 0;

    private final ElevatorIO io;

    // Create a Mechanism2d visualization of the elevator
    private MechanismLigament2d ElevatorMechanism;

    // Constructor
    public Elevator(ElevatorIO io) {
        this.io = io;
        
        /* If you want to add this, add it at line 131
         * Usually, the mechanism visualizations should only be for moving parts
         */
        // Creating additional MechanismLigament2d instances to represent different parts of the mechanism
        //These instances can be used for visualization on SmartDashboard
        ElevatorArm = new MechanismLigament2d("ElevatorArm", 5, 18, 5, new Color8Bit(Color.kGreen));
        //the dimensions (length,width,height) and the color is customizable. Currently it's green and yellow for parallel universe FRC 1257 theme with the snails and stuff
        ElevatorPlatform = new MechanismLigament2d("ElevatorPlatform", 5, 36, 5, new Color8Bit(Color.kPurple));
        SmartDashboard.putData(getName(), this);
    
    }

    public double highSetpoint() {
        return io.ELEVATOR_MAX_HEIGHT;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Elevator", inputs);

        ElevatorMechanism.setLength(io.getDistance());

        // Update the PID constants if they have changed
        if (p.get() != io.getP()) 
            io.setP(p.get());
        
        if (i.get() != io.getI())
            io.setI(i.get());
        
        if (d.get() != io.getD())
            io.setD(d.get());
        
        if (ff.get() != io.getFF())
            io.setFF(ff.get());
        
        // Log Inputs
        Logger.getInstance().processInputs("Elevator", inputs);
    }

    public void setVoltage(double motorVolts) {
        // limit the elevator if its past its limit
        if (io.getDistance() > io.ELEVATOR_MAX_HEIGHT && motorVolts > 0) {
            motorVolts = 0;
        } else if (io.getDistance() < io.ELEVATOR_MIN_HEIGHT && motorVolts < 0) {
            motorVolts = 0;
        }

        io.setVoltage(motorVolts);
    }

    public void moveUp(double speed) {
        move(Math.abs(speed));
    }

    public void moveDown(double speed) {
        move(-Math.abs(speed));
    }
    
    public void move(double speed) {
        setVoltage(speed * 12);
    }

    public void startPID() {
        io.startPIDControl();
    }

    public void runPID() {
        io.goToSetpoint(setpoint);
    }

    public void setPID(double setpoint) {
        this.setpoint = setpoint;
    }

    public boolean atSetpoint() {
        return Math.abs(io.getDistance() - setpoint) < ELEVATOR_TOLERANCE;
    }

    public void setMechanism(MechanismLigament2d mechanism) {
        ElevatorMechanism = mechanism;
    }

    public MechanismLigament2d append(MechanismLigament2d mechanism) {
        return ElevatorMechanism.append(mechanism);
    }

    public MechanismLigament2d getElevatorMechanism() {
        return new MechanismLigament2d("Elevator", 5, 36, 5, new Color8Bit(Color.kOrange));
    }

    public Command PIDCommand(double setpoint) {
        return new FunctionalCommand(
            () -> setPID(setpoint), 
            () -> runPID(), 
            (stop) -> move(0), 
            this::atSetpoint, 
            this
        );
    }
}