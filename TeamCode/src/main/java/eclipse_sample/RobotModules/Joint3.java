package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Joint3 {
    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    public DcMotorEx motor;
    private Joint2 joint2;
    private double degreesPerTick = 0.02951894846;
    private double proportion = .008;
    ElapsedTime elapsedTime;
    private double lastPosition;
    private double threshold = 2;
    private boolean stiction = false;
    private double gravityIncrement;
    private double pLoopPower;
    private double gravityPower;
    private boolean isLoaded = false;
    private double stictionPower;
    private double absoluteAngle;
    private double normalPower;
    PIDCoefficients coefficients;

    public Joint3(LinearOpMode l, Joint2 j2){
        linearOpMode = l;
        joint2 = j2;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = linearOpMode.hardwareMap;
        motor = (DcMotorEx)hardwareMap.dcMotor.get(UniConstTwo.armJoint3);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        lastPosition = getAbsoluteAngle();
        gravityIncrement = .15;
        coefficients = new PIDCoefficients(10,.05,6);
        motor.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
    }

    public void setAngle(double target){
        motor.setTargetPosition(convertToTicks(target));
    }

    public void setRelativeAngle(double target){
        motor.setTargetPosition(convertToRelativeTicks(target));
    }

    public void setAngle(double target, double joint4Angle){
        absoluteAngle = getAbsoluteAngle();

        //SET LOAD
        if(isLoaded){
            gravityIncrement = .019 + (Math.cos(Math.toRadians(joint4Angle)) * .001);
        }
        else{
            //In order to find the coefficient to multiply the cosine of joint 4, we got the difference
            //between the torque needed to run joint 3 to 0 degrees while joint 4 was at 0 and at 90
            gravityIncrement = .019 + (Math.cos(Math.toRadians(joint4Angle)) * .001);
        }

        //SET ERROR
        double error = absoluteAngle - target;
        //telemetry.addData("Error: ", error);

        //SET POWER
        gravityPower = - Math.cos(Math.toRadians(absoluteAngle)) * gravityIncrement;
        pLoopPower = error * proportion;
        normalPower = error/Math.abs(error) * Math.min(Math.abs(pLoopPower + gravityPower), .8);
        stictionPower = error/Math.abs(error) * Math.min(Math.abs(normalPower * 3), .8);


        //CHECK STICTION
        //43 milliseconds is from the OS time slice we measured on the robot phones
        if(elapsedTime.time() >= 43){
            if((lastPosition - absoluteAngle) == 0 && Math.abs(error) > threshold){
                stiction = true;
            }
            else{ stiction = false;}
            elapsedTime.reset();
        }

        //STICTION
        if(false){
            setPower(stictionPower);
            //telemetry.addData("Total Power 3: ", stictionPower);
        }

        // MAIN P LOOP
        else {
            setPower(normalPower);
            //telemetry.addData("Total Power 3: ", normalPower);
        }
        //telemetry.addData("Gravity Compensation 3: ", gravityPower);
        //telemetry.addData("P Loop Power 3: ", pLoopPower);
        lastPosition = absoluteAngle;
    }

    public double getAngle(){
        return(((double)motor.getCurrentPosition() * degreesPerTick) -163.83);
    }

    public double getAbsoluteAngle(){
        return(((double)motor.getCurrentPosition() * degreesPerTick) -163.83 + joint2.getAbsoluteAngle());
    }

    public int convertToTicks(double angle){
        return(int)((angle + 163.83 - joint2.getAbsoluteAngle())/degreesPerTick);
    }

    public int convertToRelativeTicks(double angle){
        return(int)((angle + 163.83)/degreesPerTick);
    }

    public void setPower(double power){
        motor.setPower(power);
    }
}
