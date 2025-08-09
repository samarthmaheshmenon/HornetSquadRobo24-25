package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp(name = "Individual Wheel Tester", group = "RobotTeleOp")
public class WheelTester extends LinearOpMode {
    DcMotor lf, lb, rf, rb, in;

    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("lf");
        rf = hardwareMap.dcMotor.get("rf");
        lb = hardwareMap.dcMotor.get("lb");
        rb = hardwareMap.dcMotor.get("rb");
        in = hardwareMap.dcMotor.get("in");
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                lf.setPower(1);
            } else {
                lf.setPower(0);
            }
            if (gamepad1.b) {
                lb.setPower(1);
            } else {
                lb.setPower(0);
            }
            if (gamepad1.x) {
                rf.setPower(1);
            } else {
                rf.setPower(0);
            }
            if (gamepad1.y) {
                rb.setPower(1);
            } else {
                rb.setPower(0);
            }

            if (gamepad2.dpad_down) {
                in.setPower(1);
            } else if (gamepad2.dpad_up) {
                in.setPower(-1);
            } else {
                in.setPower(0);
            }
        }
    }
}
