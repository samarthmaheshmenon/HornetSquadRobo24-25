package eclipse_sample.Testers;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;

@TeleOp(name = "Ticks Tester")
public class TicksTester extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive()){
            arm.disable();
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.joint1.setAngle(0);
            arm.joint2.setAngle(0);
            arm.joint3.setAngle(0);
            arm.joint4.setAngle(0);
            telemetry.addData("Check Position: ", arm.checkPos());
            telemetry.addData("Joint 1: ", arm.joint1.getAngle());
            telemetry.addData("Joint 2: ", arm.joint2.getAngle());
            telemetry.addData("Joint 3: ", arm.joint3.getAngle());
            telemetry.addData("Joint 4: ", arm.joint4.getAngle());
            telemetry.update();
        }
    }
}