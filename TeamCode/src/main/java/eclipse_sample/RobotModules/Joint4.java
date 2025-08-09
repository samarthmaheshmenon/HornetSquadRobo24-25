package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Joint4 {
    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    public DcMotorEx motor;
    private Joint3 joint3;
    private double proportion = .005;
    double currentAngle;
    String name;
    private long lastTime;
    ElapsedTime elapsedTime;
    private double degreesPerTick = .3125;
    public PIDCoefficients coefficients;

    public Joint4(LinearOpMode l, Joint3 j3){
        linearOpMode = l;
        joint3 = j3;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = linearOpMode.hardwareMap;
        motor = (DcMotorEx)hardwareMap.dcMotor.get(UniConstTwo.armJoint4);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        coefficients = new PIDCoefficients(100,0,7);
        motor.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
    }

    public void setAngle(double target){
        motor.setTargetPosition(convertToTicks(target));
    }

    public void setRelativeAngle(double target){
        motor.setTargetPosition(convertToRelativeTicks(target));
    }

    public double getAngle(){
        return(motor.getCurrentPosition()*degreesPerTick + 125);
    }

    public double getAbsoluteAngle(){
        return(getAngle() + joint3.getAbsoluteAngle());
        //+ joint3.getAbsoluteAngle());
    }

    public void setPower(double power){
        motor.setPower(power);
    }

    public int convertToTicks(double angle){
        return(int)((angle - 125 - joint3.getAbsoluteAngle())/degreesPerTick);
    }

    public int convertToRelativeTicks(double angle){
        return(int)((angle - 125)/degreesPerTick);
    }
}
//    public void setAngle(double target){
//
//        //CHECK ANGLE DOES NOT EXCEED MAXIMUM
//        if(P.getVoltage() < .28){
//            target = getAbsoluteAngle() - 3;
//            telemetry.addLine("WARNING: EXCEEDING JOINT 4 RANGE");
//        }
//        else if(P.getVoltage() > 3.3){
//            target = getAbsoluteAngle()+ 3;
//            telemetry.addLine("WARNING: EXCEEDING JOINT 4 RANGE");
//        }
//
//        //SET LOAD
//        if(isLoaded){ gravityIncrement = .27; }
//        else{ gravityIncrement = .15; }
//
//        //SET ERROR
//        double error = getAbsoluteAngle() - target;
//
//        //SET POWER
//        //Angle from joint 4 to center of gravity of the intake is 21
//        gravityPower = - Math.cos(Math.toRadians(getAbsoluteAngle()+21)) * gravityIncrement;
//        pLoopPower = error * proportion;
//
//        //CHECK STICTION
//        //43 milliseconds is from the OS time slice we measured on the robot phones
//        if(elapsedTime.time() >= 43){
//            if((lastPosition - getAbsoluteAngle()) == 0 && Math.abs(error) > threshold){
//                stiction = true;
//            }
//            else{ stiction = false;}
//            elapsedTime.reset();
//        }
//
//        //STICTION
//        if(false){
//            //setPower(error/Math.abs(error) * .25);
//            setPower(pLoopPower + gravityPower * 3);
//            //telemetry.addData("Total Power: ", error/Math.abs(error) * .25);
//            //telemetry.addData("Total Power 4: ", pLoopPower + gravityPower * 3);
//        }
//
//        // MAIN P LOOP
//        else {
//            //CHECK POWER VALUE
//            if(Math.abs(pLoopPower + gravityPower) > .8){
//                setPower((error/Math.abs(error)) * .8);
//                //telemetry.addData("Total Power 4: ", error/Math.abs(error) * .8);
//            }
//
//            //P LOOP AND GRAVITY COMPENSATION
//            //Anti-gravity, 6.5 inches is center of gravity, 1 is weight in pounds, 59.12 is torque from vex393 motor
//            else{
//                setPower(pLoopPower + gravityPower);
//                //telemetry.addData("Total Power 4: ", pLoopPower + gravityPower);
//            }
//        }
//        //telemetry.addData("Gravity Compensation 4: ", gravityPower);
//        //telemetry.addData("P Loop Power 4: ", pLoopPower);
//        //telemetry.addData("Stiction 4: ", stiction);
//        lastPosition = getAbsoluteAngle();
//    }
//    //POWER IS BACKWARDS :(