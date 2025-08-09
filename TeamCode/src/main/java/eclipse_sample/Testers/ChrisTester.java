package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.VEX393Servo;

@TeleOp(name = "Chris Tester")
public class ChrisTester extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        VEX393Servo S1 = new VEX393Servo(hardwareMap.crservo.get("left"),1,1);
        VEX393Servo S2 = new VEX393Servo(hardwareMap.crservo.get("right"),1,1);
        waitForStart();
        while (opModeIsActive()){
            if (gamepad2.left_stick_y > .3){
                S1.setPower(.5);
            }
            else if (gamepad2.left_stick_y < .3){
                S1.setPower(-.5);
            }
            if (gamepad2.right_stick_y > .3){
                S2.setPower(1);
            }
            else if (gamepad2.right_stick_y < .3){
                S2.setPower(-1);
            }
        }
    }
}