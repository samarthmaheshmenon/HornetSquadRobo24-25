package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

;

public class Joint1 {
    public LinearOpMode linearOpMode;
    public Telemetry telemetry;
    public DcMotorEx one, two;
    public double power;
    public double degreesPerTick = 0.03339517625*2;
    private double proportion = .006;
    private double gravityIncrement;
    private double pLoopPower;
    private double gravityPower;
    private double angleChange;
    private boolean isLoaded = false;
    private double absoluteAngle;
    private double normalPower;
    private PIDCoefficients coefficients;
    ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    double lastTime = 0;

    public Joint1(LinearOpMode l, double motorPower, DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        linearOpMode = l;
        telemetry = linearOpMode.telemetry;
        HardwareMap hardwareMap = linearOpMode.hardwareMap;
        coefficients = new PIDCoefficients(12,.5,12);
        one = (DcMotorEx)hardwareMap.dcMotor.get(UniConstTwo.baseJoint1);
        two = (DcMotorEx)hardwareMap.dcMotor.get(UniConstTwo.baseJoint2);
        one.setZeroPowerBehavior(zeroPowerBehavior);
        two.setZeroPowerBehavior(zeroPowerBehavior);
        one.setDirection(DcMotorSimple.Direction.REVERSE);
        two.setDirection(DcMotorSimple.Direction.REVERSE);
        one.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        two.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        one.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        two.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        power = motorPower;
        one.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
        two.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
    }

    public void setAngle(double target){
        setTargetPosition(convertToTicks(target));
    }

    public void setAngle(double target, double joint2Angle, double joint3Angle, double joint4Angle){
        lastTime = elapsedTime.time();
        absoluteAngle = getAngle();

        //SET LOAD
        if(isLoaded){
            gravityIncrement = .055 + (Math.cos(Math.toRadians(joint2Angle)) * .008)+ (Math.cos(Math.toRadians(joint3Angle)) * .013) + (Math.cos(Math.toRadians(joint4Angle)) * .005);
        }
        else{
            gravityIncrement = .055 + (Math.cos(Math.toRadians(joint2Angle)) * .008)+ (Math.cos(Math.toRadians(joint3Angle)) * .013) + (Math.cos(Math.toRadians(joint4Angle)) * .005);
        }
        telemetry.addData("After 3 Cosines: ", elapsedTime.time() - lastTime);
        //SET ERROR
        double error = target - absoluteAngle;


        //SET POWER
        gravityPower = Math.cos(Math.toRadians(absoluteAngle)) * gravityIncrement;
        telemetry.addData("After 1 more Cosine: ", elapsedTime.time() - lastTime);
        pLoopPower = error * proportion;
        normalPower = pLoopPower + gravityPower;
        setPower(normalPower);
        telemetry.addData("After setting power: ", elapsedTime.time() - lastTime);
        //telemetry.addData("Total Power 1: ", normalPower);
        //telemetry.addData("Gravity Compensation 1: ", gravityPower);
        //telemetry.addData("P Loop Power 1: ", pLoopPower);
    }

    public double getAngle(){
        return(((double)one.getCurrentPosition() * degreesPerTick) - 28.98);
    }

    public int convertToTicks(double angle){return(int)((angle + 28.98) / degreesPerTick);}

    public void setBehavior(DcMotor.ZeroPowerBehavior behavior){
        one.setZeroPowerBehavior(behavior);
        two.setZeroPowerBehavior(behavior);
    }

    public void setPower(double power){
        one.setPower(power);
        two.setPower(power);
    }

    public void setMode(DcMotor.RunMode mode){
        one.setMode(mode);
        two.setMode(mode);
    }

    public void setTargetPosition(int position){
        one.setTargetPosition(position);
        two.setTargetPosition(position);
    }
}


