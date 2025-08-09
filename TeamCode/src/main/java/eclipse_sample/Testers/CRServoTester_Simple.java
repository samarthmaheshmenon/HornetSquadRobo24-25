package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotModules.VEX393Servo;

@Disabled
@TeleOp(name = "Servo Tester Simple")
public class CRServoTester_Simple extends LinearOpMode {
    VEX393Servo servoLeft;
    VEX393Servo servoRight;
    DcMotor motorServo;
//    Vex393Driver sensor;
    @Override
    public void runOpMode() throws InterruptedException{
        servoLeft = new VEX393Servo(hardwareMap.crservo.get("left"),1,1);
        servoRight = new VEX393Servo(hardwareMap.crservo.get("right"),1,1);
        motorServo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        //double angle =.getPosition();
        while (opModeIsActive()) {
            servoLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            servoRight.setDirection(DcMotorSimple.Direction.FORWARD);
            if (gamepad2.a) {
                servoLeft.setPower(.5);
            } else if (gamepad2.b) {
                servoLeft.setPower(-.5);
            } else {
                servoLeft.setPower(0);
            }

            if (gamepad2.x) {
                servoRight.setPower(-.5);
            } else if (gamepad2.y) {
                servoRight.setPower(.5);
            } else {
                servoRight.setPower(0);
            }

        }
    }
}
