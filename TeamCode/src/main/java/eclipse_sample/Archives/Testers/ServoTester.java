package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@TeleOp(name = "Servo Tester")
public class ServoTester extends LinearOpMode {

    Servo servo, servo1;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get(UniversalConstants.jewelGimbleElbow);
        servo1 = hardwareMap.servo.get(UniversalConstants.jewelGimbleWrist);

        servo.setPosition(.5);
        servo1.setPosition(.5);
        Controller lastGamepad1 = new Controller(gamepad1);
        Controller lastGamepad2 = new Controller(gamepad2);

        waitForStart();

        while (opModeIsActive()) {
            lastGamepad1.update();
            lastGamepad2.update();

            if (lastGamepad1.AOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() + .01, 0, 1));
            } else if (lastGamepad1.YOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() - .01, 0, 1));
            } else if (lastGamepad1.dpadUpOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() + .05, 0, 1));
            } else if (lastGamepad1.dpadDownOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() - .05, 0, 1));
            } else if (lastGamepad1.leftBumperOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() + .1, 0, 1));
            } else if (lastGamepad1.rightBumperOnce()) {
                servo.setPosition(Range.clip(servo.getPosition() - .1, 0, 1));
            }
            if (lastGamepad2.AOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() + .01, 0, 1));
            } else if (lastGamepad2.YOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() - .01, 0, 1));
            } else if (lastGamepad2.dpadUpOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() + .05, 0, 1));
            } else if (lastGamepad2.dpadDownOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() - .05, 0, 1));
            } else if (lastGamepad2.leftBumperOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() + .1, 0, 1));
            } else if (lastGamepad2.rightBumperOnce()) {
                servo1.setPosition(Range.clip(servo1.getPosition() - .1, 0, 1));
            }
            telemetry.addData("Servo Bottom Position", servo.getPosition());
            telemetry.addData("Servo Top Position", servo1.getPosition());
            telemetry.update();

        }
    }
}
