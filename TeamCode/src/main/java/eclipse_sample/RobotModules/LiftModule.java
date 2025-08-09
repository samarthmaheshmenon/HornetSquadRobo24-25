package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftModule {
    private LinearOpMode linearOpMode;
    public DcMotor liftMotor;
    private Telemetry telemetry;
    private Servo servo;
    private boolean lastAButtonState = false;
    private double position = 0;

    // creates an object of the lift module with all properties
    public LiftModule(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        linearOpMode = l;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = l.hardwareMap;
        liftMotor = hardwareMap.dcMotor.get(UniConstTwo.liftMotor);
        servo = hardwareMap.servo.get(UniConstTwo.liftServo);
        liftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Takes gamepad inputs to control lift in TeleOp
    public void updateByGamepad() {
        double power = 0;
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // ensures that lift does not surpass certain encoder values
        if (linearOpMode.gamepad2.dpad_up && (liftMotor.getCurrentPosition() < 7950)) {
            power = 1;
        }
        if (linearOpMode.gamepad2.dpad_down && (liftMotor.getCurrentPosition() > 0)) {
            power = -1;
        }
        liftMotor.setPower(power);

        if(linearOpMode.gamepad2.dpad_right){
            position = .73;
        }
        if(linearOpMode.gamepad2.dpad_left){
            position = 0;
        }
        if(linearOpMode.gamepad2.a && !lastAButtonState){
            if(servo.getController().getPwmStatus() == ServoController.PwmStatus.DISABLED) {
                servo.getController().pwmEnable();
            } else {
                servo.getController().pwmDisable();
            }
        }
        servo.setPosition(position);
        lastAButtonState = linearOpMode.gamepad2.a;
    }

    public void setServo(double pos){
        servo.setPosition(pos);
    }
    public void CustomTestLift() {
        // ensures that lift does not surpass certain encoder values
        int power;
        power = 0;
        if (linearOpMode.gamepad2.x && (liftMotor.getCurrentPosition() < 8400)) {
            power = 1;
        }
        if (linearOpMode.gamepad2.y && (liftMotor.getCurrentPosition() > 25)) {
            power = -1;
        }
        liftMotor.setPower(power);
    }

    public void zeroPower(){
        liftMotor.setPower(0);
    }
    // This method is used for the lift reset class
    public void resetLift(){
        double power = 0;
        if (linearOpMode.gamepad2.dpad_up){
            power = .5;
        }
        if (linearOpMode.gamepad2.dpad_down){
            power = -.5;
        }
        liftMotor.setPower(power);
    }
    public void actuallyResetLift(){
        liftMotor.setTargetPosition(75);
    }

    // Will add lift position to telemetry values
    public void updateTelemetry() {
        telemetry.addData("Current Position: ", liftMotor.getCurrentPosition());
        telemetry.addData("Current Servo Position: ", servo.getPosition());
        telemetry.update();
    }


    // calls update gamepad and telemetry in one method
    public void updateAll() {
        updateByGamepad();
        updateTelemetry();
    }

    public void moveRotations(int rotations) {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(rotations * 1120);
        while (liftMotor.isBusy()) {
            liftMotor.setPower(.5);
        }
    }

    public void deploy(){
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(8880);
        while (liftMotor.isBusy()){
            liftMotor.setPower(.5);
        }
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveToPosition(int position) {
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(position);
        while (linearOpMode.opModeIsActive() && Math.abs(liftMotor.getCurrentPosition() - position) >= 30) {
            liftMotor.setPower(.7);
        }
    }

    public void nonBlockMoveToPosition(int position) {
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(position);
        //while (linearOpMode.opModeIsActive() && Math.abs(liftMotor.getCurrentPosition() - position) >= 30) {
            liftMotor.setPower(.3);
        //}
    }


}
