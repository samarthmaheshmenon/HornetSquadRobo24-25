package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;

@Disabled
@TeleOp(name = "Torque TeleOp")
public class torque_tester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double power = 0;
        double inc = 0.01;
        int map = 1;

        telemetry.addLine("Ready to go!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            //arm.joint3.setPower(.5);
            if(this.gamepad1.a) {
                power += inc;
            }
            else if(this.gamepad1.b){
                power -= inc;
            }

            if (this.gamepad1.x) {
                power = 0;
            }

            if (this.gamepad1.dpad_up) {
                map = 1;
                arm.joint2.setPower(0);
                arm.joint3.setPower(0);
                arm.joint4.setPower(0);
            }
            if (this.gamepad1.dpad_right) {
                map = 2;
                arm.joint1.setPower(0);
                arm.joint3.setPower(0);
                arm.joint4.setPower(0);
            }
            if (this.gamepad1.dpad_down) {
                map = 3;
                arm.joint1.setPower(0);
                arm.joint2.setPower(0);
                arm.joint4.setPower(0);
            }
            if (this.gamepad1.dpad_left) {
                map = 4;
                arm.joint1.setPower(0);
                arm.joint2.setPower(0);
                arm.joint3.setPower(0);
            }

            if(this.gamepad1.y){
                if (map == 1) {
                    arm.joint1.setPower(power);
                }else if (map == 2) {
                    arm.joint2.setPower(power);
                }else if (map == 3) {
                    arm.joint3.setPower(power);
                }else if (map == 4) {
                    arm.joint4.setPower(power);
                }
            }
            else{
                arm.joint1.setPower(0);
                arm.joint2.setPower(0);
                arm.joint3.setPower(0);
                arm.joint4.setPower(0);
            }

            telemetry.addData("Joint", map);
            telemetry.addData("Power:", power);
            telemetry.update();
            Thread.sleep(100);
        }
    }
}