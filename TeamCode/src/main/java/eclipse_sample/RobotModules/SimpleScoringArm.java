package eclipse_sample.RobotModules;

import static org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo.seg1;
import static org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo.seg2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SimpleScoringArm {

    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    public Joint1 joint1;
    public Joint2 joint2;
    public Intake intake;
    public Joint3 joint3;
    public Joint4 joint4;
    public double save1, save2, save3, save4;
    public double angle1, angle2, angle3, angle4;
    public boolean joint1Exists = true;
    public boolean joint2Exists = true;
    public boolean joint3Exists = true;
    public boolean joint4Exists = true;
    public boolean intakeExists = true;

    public double joint1Power, joint2Power, joint3Power, joint4Power;
    private double tolerance1 = 30;
    private double tolerance2 = 30;
    private double tolerance3 = 40;
    private double tolerance4 = 100;

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    public SimpleScoringArm(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        linearOpMode = l;
        HardwareMap hardwareMap = l.hardwareMap;
        telemetry = l.telemetry;
        if(joint1Exists){
            joint1 = new Joint1(linearOpMode, .2, zeroPowerBehavior);
            angle1 = joint1.getAngle();
        }
        if(joint2Exists){
            joint2 = new Joint2(linearOpMode, .1, zeroPowerBehavior, joint1, joint3, joint4);
            angle2 = joint2.getAngle();
        }
        if(joint3Exists){
            joint3 = new Joint3(linearOpMode, joint2);
            angle3 = joint3.getAngle();
        }
        if(joint4Exists){
            joint4 = new Joint4(linearOpMode, joint3);
            angle4 = joint4.getAngle();
        }
        if(intakeExists){
            intake = new Intake(linearOpMode);
        }

        status("Globals Initialized");
    }

    public void updateTelemetry()
    {
        telemetry.addData("Angle 1: ", joint1.getAngle());
        telemetry.addData("Angle 2: ", joint2.getAngle());
        telemetry.addData("Angle 3: ", joint3.getAngle());
        telemetry.addData("Angle 4: ", joint4.getAngle());
        telemetry.addData("Joint 1 Encoder Difference: ", Math.abs(joint1.one.getCurrentPosition() - joint1.two.getCurrentPosition()));
//        telemetry.addData("Joint 1 Error: ", joint1.one.getTargetPosition() - joint1.one.getCurrentPosition());
//        telemetry.addData("Joint 2 Error: ", joint2.motor.getTargetPosition() - joint2.motor.getCurrentPosition());
//        telemetry.addData("Joint 3 Error: ", joint3.motor.getTargetPosition() - joint3.motor.getCurrentPosition());
//        telemetry.addData("Joint 4 Error: ", joint4.motor.getTargetPosition() - joint4.motor.getCurrentPosition());
//        //telemetry.update();
    }

    public boolean checkPos()
    {
        checkTelemPos();
        if(joint1.one.getTargetPosition() + tolerance1 > joint1.one.getCurrentPosition() && joint1.one.getTargetPosition() - tolerance1 < joint1.one.getCurrentPosition()){
            if(joint2.motor.getTargetPosition() + tolerance2 > joint2.motor.getCurrentPosition() && joint2.motor.getTargetPosition() - tolerance2 < joint2.motor.getCurrentPosition()){
                if(joint3.motor.getTargetPosition() + tolerance3 > joint3.motor.getCurrentPosition() && joint3.motor.getTargetPosition() - tolerance3 < joint3.motor.getCurrentPosition()){
                    return true;
                }
            }
        }
        return false;
        //telemetry.update();
    }

    public void checkTelemPos(){
        if(joint1.one.getTargetPosition() + tolerance1 > joint1.one.getCurrentPosition() && joint1.one.getTargetPosition() - tolerance1 < joint1.one.getCurrentPosition())
            telemetry.addLine("Joint 1 in position");
        else
            telemetry.addData("Joint 1 difference: ", joint1.one.getTargetPosition() - joint1.one.getCurrentPosition());
        if(joint2.motor.getTargetPosition() + tolerance2 > joint2.motor.getCurrentPosition() && joint2.motor.getTargetPosition() - tolerance2 < joint2.motor.getCurrentPosition())
            telemetry.addLine("Joint 2 in position");
        else
            telemetry.addData("Joint 2 difference: ", joint2.motor.getTargetPosition() - joint2.motor.getCurrentPosition());
        if(joint3.motor.getTargetPosition() + tolerance3 > joint3.motor.getCurrentPosition() && joint3.motor.getTargetPosition() - tolerance3 < joint3.motor.getCurrentPosition())
            telemetry.addLine("Joint 3 in position");
        else
            telemetry.addData("Joint 3 difference: ", joint3.motor.getTargetPosition() - joint3.motor.getCurrentPosition());
    }

    public boolean check4(){
        if(joint4.motor.getTargetPosition() + tolerance4 > joint4.motor.getCurrentPosition() && joint4.motor.getTargetPosition() - tolerance4 < joint4.motor.getCurrentPosition()){
            return true;
        }
        return false;
    }

    public void getTargetPositions() {
        telemetry.addData("Joint 1 Target: ", joint1.one.getTargetPosition());
        telemetry.addData("Joint 1 Current: ", joint1.one.getCurrentPosition());
        telemetry.addData("Joint 3 Target: ", joint3.motor.getTargetPosition());
        telemetry.addData("Joint 3 Current: ", joint3.motor.getCurrentPosition());
    }

    public void disable(){
        joint1.one.setMotorDisable();
        joint1.two.setMotorDisable();
        joint2.motor.setMotorDisable();
        joint3.motor.setMotorDisable();
        joint4.motor.setMotorDisable();
    }

    public void enable(){
        joint1.one.setMotorEnable();
        joint1.two.setMotorEnable();
        joint2.motor.setMotorEnable();
        joint3.motor.setMotorEnable();
        joint4.motor.setMotorEnable();
    }

    public void setAllPower(double power){
        joint1.setPower(power/2);
        joint2.setPower(power);
        joint3.setPower(power);
    }

    public void updateByGamepad(){
        joint3Power = (linearOpMode.gamepad2.left_stick_y) / 3;
        joint4Power = (linearOpMode.gamepad2.right_stick_y) / 2;

        //UPDATE INTAKE
        if(intakeExists){
            intake.update();
        }

        //SAVE METHODS
        if (linearOpMode.gamepad2.right_bumper){
            savePosition();
        }
        if (linearOpMode.gamepad2.left_bumper){
            moveToSave();
        }

        //UPDATE JOINT 1
        if(joint1Exists && joint2Exists && joint3Exists && joint4Exists){

        }
    }

    public void updateAll(){
        updateByGamepad();
        updateTelemetry();
    }

    public void setBehavior(DcMotor.ZeroPowerBehavior behavior){
        joint1.setBehavior(behavior);
        joint2.setBehavior(behavior);
    }

    public boolean opModeIsActive(){
        return linearOpMode.opModeIsActive();
    }

    public void setMode(DcMotor.RunMode mode){
        joint1.setMode(mode);
        joint2.setMode(mode);
    }

    public void setMotorPower(double power){
        joint1.setPower(power);
        //joint2.setPower(power);
    }

    public void setServoPower(double power){
        //joint3.setPower(power);
        joint4.setPower(power);
    }

    public void moveToPosition(double x, double y){
        double angle1;
        double angle3;
        joint2.setAngle(180, joint3.getAbsoluteAngle(), joint4.getAbsoluteAngle());
        joint4.setAngle(180);
        angle1 = Math.toDegrees(Math.atan(y/x) + Math.acos((-1*(seg2*seg2)+(x*x)+(y*y)+(seg1*seg1))/(2*Math.sqrt((x*x) + (y*y))*seg1)));
        angle3 = Math.toDegrees(Math.acos(((seg2*seg2)+(seg1*seg1)-((x*x)+(y*y)))/(2*seg1*seg2)));
        joint1.setAngle(angle1, joint2.getAbsoluteAngle(), joint3.getAbsoluteAngle(), joint4.getAbsoluteAngle());
        joint3.setAngle(angle3, joint4.getAbsoluteAngle());
    }

    public void savePosition(){
        save1 = joint1.getAngle();
        save2 = joint2.getAngle();
        save3 = joint3.getAngle();
        save4 = joint4.getAngle();
    }

    public void moveToSave(){
        joint1.setAngle(save1, joint2.getAbsoluteAngle(), joint3.getAbsoluteAngle(), joint4.getAbsoluteAngle());
        joint2.setAngle(save2, joint3.getAbsoluteAngle(), joint4.getAbsoluteAngle());
        joint3.setAngle(save3, joint4.getAbsoluteAngle());
        joint4.setAngle(save4);
    }

    public double getX(){
        return (14.75 * Math.toDegrees(Math.cos(joint1.getAngle()))) + (14.79 * Math.toDegrees(Math.cos(joint2.getAngle()))) + (13.91 * Math.toDegrees(Math.cos(joint3.getAngle()))) + (13.82 * Math.toDegrees(Math.cos(joint4.getAngle())));
    }

    public double getY(){
        return (14.75 * Math.toDegrees(Math.sin(joint1.getAngle()))) + (14.79 * Math.toDegrees(Math.sin(joint2.getAngle()))) + (13.91 * Math.toDegrees(Math.sin(joint3.getAngle()))) + (13.82 * Math.toDegrees(Math.sin(joint4.getAngle())));
    }

    public void moveForward(double distance){
        moveToPosition(getX() + distance, getY());
    }

    public void moveUp(double distance){
        moveToPosition(getX(), getY() + distance);
    }

    public void resetAngles(){
        angle1 = joint1.getAngle();
        angle2 = joint2.getAngle();
        angle3 = joint3.getAngle();
        angle4 = joint4.getAngle();
    }
}