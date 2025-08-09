package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.VEX393Servo;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@TeleOp(name = "CRServoTester")
public class CRServoTester extends LinearOpMode {
    VEX393Servo servoL, servoR;
    @Override
    public void runOpMode() throws InterruptedException {
        servoL = new VEX393Servo(hardwareMap.crservo.get(UniversalConstants.intakeMotorLeft), 1, 1);
        servoR = new VEX393Servo(hardwareMap.crservo.get(UniversalConstants.intakeMotorRight), 1, 1);
        Controller controller1 = new Controller(gamepad1);
        Controller controller2 = new Controller(gamepad2);
        waitForStart();
        while(opModeIsActive()){
            controller1.update();
            controller2.update();
            if(controller1.dpadUpOnce()){
                servoL.setPower(servoL.getPower()+.01);
            } else if(controller1.dpadDownOnce()) {
                servoL.setPower(servoL.getPower()-.01);
            }
            if(controller2.dpadUpOnce()){
                servoR.setPower(servoR.getPower()+.01);
            } else if(controller2.dpadDownOnce()) {
                servoR.setPower(servoR.getPower()-.01);
            }
            telemetry.addData("Left", servoL.getPower());
            telemetry.addData("Right", servoR.getPower());
            telemetry.update();
        }
    }
}
