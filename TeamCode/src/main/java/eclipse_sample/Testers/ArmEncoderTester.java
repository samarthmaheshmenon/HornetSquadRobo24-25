package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.Joint1;
@Disabled
@TeleOp(name = "Arm Encoder Tester")
public class ArmEncoderTester extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException{
        Joint1 joint1 = new Joint1(this, 0, DcMotor.ZeroPowerBehavior.FLOAT);
        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("Joint 1: ", joint1.getAngle());
            telemetry.update();
        }
    }
}
