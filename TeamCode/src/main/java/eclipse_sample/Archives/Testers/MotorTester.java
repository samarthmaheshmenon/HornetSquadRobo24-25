package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@TeleOp(name = "Slam Dunk Accurate Mover")
public class MotorTester extends LinearOpMode {
    DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.dcMotor.get(UniversalConstants.slamDunker);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (motor.getCurrentPosition() != 0) {
            idle();
        }
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(0);
        Controller lastGamepad1 = new Controller(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(1);
            if (lastGamepad1.AOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() + 25, -2000, 2000));
            } else if (lastGamepad1.YOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() - 25, -2000, 2000));
            } else if (lastGamepad1.dpadUpOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() + 100, -2000, 2000));
            } else if (lastGamepad1.dpadDownOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() - 100, -2000, 2000));
            } else if (lastGamepad1.leftBumperOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() + 250, -2000, 2000));
            } else if (lastGamepad1.rightBumperOnce()) {
                motor.setTargetPosition(Range.clip(motor.getCurrentPosition() - 250, -2000, 2000));
            }

            telemetry.addData("Position", motor.getCurrentPosition());
            telemetry.addData("Target", motor.getTargetPosition());
            telemetry.update();
            lastGamepad1.update();

        }
    }
}
