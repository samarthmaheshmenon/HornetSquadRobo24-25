package eclipse_sample.Testers;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp (name = "Servo Tester")
public class dispenserMovertester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = this.hardwareMap.servo.get(UniConstTwo.dispenser);
        double position = 0;
        waitForStart();
        while (opModeIsActive()){
            if(gamepad2.dpad_right){
                 position += .01;
                servo.setPosition(position);
            }
            if(gamepad2.dpad_left){
                position -= .01;
                servo.setPosition(position);
            }
            telemetry.addData("Position: ", position);
            telemetry.update();
        }
    }
}
