package eclipse_sample.Testers;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;

@Disabled
@TeleOp (name = "Joint3 Tester")
public class ServoJointTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive()) {
            arm.updateTelemetry();
        }
    }
}
