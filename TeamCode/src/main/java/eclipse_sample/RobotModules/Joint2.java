package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

public class Joint2 {
    public LinearOpMode linearOpMode;
    public Telemetry telemetry;
    public DcMotorEx motor;
    private Joint1 joint1;
    private Joint3 joint3;
    private Joint4 joint4;
    private double degreesPerTick = 0.05696202531;
    private Map<String, Boolean> states = new HashMap<>();
    private double threshold = 2;
    private boolean isMoving = false;
    private double position;
    private double proportion = .017;
    private double lastPosition;
    ElapsedTime elapsedTime;
    private boolean stiction = false;
    private double gravityIncrement;
    private double pLoopPower;
    private double gravityPower;
    private boolean isLoaded = false;
    private double stictionPower;
    private double absoluteAngle;
    private double normalPower;
    PIDCoefficients coefficients;
    double lastTime = 0;

    public Joint2(LinearOpMode l, double motorPower, DcMotor.ZeroPowerBehavior zeroPowerBehavior, Joint1 j1, Joint3 j3, Joint4 j4) {
        linearOpMode = l;
        joint1 = j1;
        joint3 = j3;
        joint4 = j4;
        telemetry = linearOpMode.telemetry;
        HardwareMap hardwareMap = linearOpMode.hardwareMap;
        motor = (DcMotorEx)hardwareMap.dcMotor.get(UniConstTwo.armJoint2);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        states.put("STOWED", true);
        states.put("STICTION", false);
        states.put("MOVING", false);
        states.put("HOLDING", false);
        lastPosition = getAbsoluteAngle();
        elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        coefficients = new PIDCoefficients(20,2,7);
        motor.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
    }

    public void setAngle(double target){
        motor.setTargetPosition(convertToTicks(target));
        //telemetry.addData("Difference: ", elapsedTime.time() - lastTime);
    }

    public void setRelativeAngle(double target){
        motor.setTargetPosition(convertToRelativeTicks(target));
    }

    public void setAngle(double target, double joint3Angle, double joint4Angle){
        absoluteAngle = getAbsoluteAngle();

        //SET LOAD
        if(isLoaded){
            gravityIncrement = .03 + (Math.cos(Math.toRadians(joint3Angle)) * .01) + (Math.cos(Math.toRadians(joint4Angle)) * .0001);
        }
        else{
            gravityIncrement = .03 + (Math.cos(Math.toRadians(joint3Angle)) * .01) + (Math.cos(Math.toRadians(joint4Angle)) * .0001);
        }

        //SET ERROR
        double error = target - absoluteAngle;


        //SET POWER
        gravityPower = Math.cos(Math.toRadians(absoluteAngle)) * gravityIncrement;
        pLoopPower = error * proportion;
        normalPower = pLoopPower + gravityPower;
        stictionPower = normalPower * 2;

        //CHECK STICTION
        //43 milliseconds is from the OS time slice we measured on the robot phones
        if(elapsedTime.time() >= 43){
            if((lastPosition - absoluteAngle) == 0 && Math.abs(error) > threshold){
                stiction = true;
            }
            else{ stiction = false;}
            elapsedTime.reset();
        }

        //STICTION: Pretty much unnessescary for this joint
        if(false){
            setPower(stictionPower);
            //telemetry.addData("Total Power 2: ", stictionPower);
        }

        // MAIN P LOOP
        else {
            setPower(normalPower);
            //telemetry.addData("Total Power 2: ", normalPower);
        }
        //telemetry.addData("Gravity Compensation 2: ", gravityPower);
        //telemetry.addData("P Loop Power 2: ", pLoopPower);
        //telemetry.addData("Stiction 2: ", stiction);
        lastPosition = absoluteAngle;
    }

    public double getAngle(){
        return(((double)motor.getCurrentPosition() * degreesPerTick) + 170.83); //+ joint1.getAngle());
    }

    public double getAbsoluteAngle(){
        return(((double)motor.getCurrentPosition() * degreesPerTick)+170.83+joint1.getAngle()); //+ joint1.getAngle());
    }

    public int convertToTicks(double angle){
        return(int)((angle - 170.83 - joint1.getAngle())/degreesPerTick);
    }

    public int convertToRelativeTicks(double angle){
        return(int)((angle - 170.83)/degreesPerTick);
    }

    public void hold(double angle){
    }

    public void setBehavior(DcMotor.ZeroPowerBehavior behavior){
        motor.setZeroPowerBehavior(behavior);
    }

    public void setPower(double power){
        motor.setPower(power);
    }

    public void setMode(DcMotor.RunMode mode){
        motor.setMode(mode);
    }

    public void updateAll(double target) {
        if(getAngle() < target - threshold || getAngle() > target + threshold) {
            if(isMoving){
                //setAngle(target);
                telemetry.addLine("MOVING");
            }
            else{
                position = getAngle();
                stiction(target);
                telemetry.addLine("STICTION");
            }
        }
        else{
            hold(target);
            telemetry.addLine("HOLDING");
        }
    }

    public void stiction(double target){
        if(getAngle() > target){
            setPower(1);
        }
        else if(getAngle() < target){
            setPower(-1);
        }
        if(!(position > getAngle() - .1) || !(position < getAngle() + .1)){
            isMoving = true;
        }
    }
}