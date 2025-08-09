package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.PRobot;

@Disabled
@TeleOp (name = "All Tester")
public class AllTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        PRobot robot = new PRobot(this);
        //Joint3 joint3 = new Joint3(this, UniConstTwo.armJoint3, UniConstTwo.joint3Pot);
        waitForStart();
        //robot.initialize();
        while (opModeIsActive()){
            if (gamepad2.a){
                while (gamepad2.a){
                    robot.drivetrain.setLeft(.4);
                }
                robot.drivetrain.setLeft(0);
            }
            else if (gamepad2.b){
                while (gamepad2.b){
                    robot.drivetrain.setRight(.4);
                }
                robot.drivetrain.setRight(0);
            }
            else if (gamepad2.x){
                while (gamepad2.x){
                    robot.liftModule.CustomTestLift();
                }
                robot.liftModule.zeroPower();
            }
            else if (gamepad2.y) {
                while (gamepad2.x) {
                    robot.liftModule.CustomTestLift();
                }
                robot.liftModule.zeroPower();
            }
            else if (gamepad2.right_bumper){
                robot.liftModule.setServo(.73);
            }
            else if (gamepad2.left_bumper){
                robot.liftModule.setServo(0);
            }
            else if (gamepad2.dpad_up){
                robot.scoringArm.joint1.setPower(.2);
            }
            else if (gamepad2.dpad_left){
                robot.scoringArm.joint2.setPower(.2);
            }
            else if (gamepad2.dpad_right){
                robot.scoringArm.joint3.setPower(.2);
            }
            else if (gamepad2.dpad_down){
                robot.scoringArm.joint4.setPower(.2);
            }
            else if (gamepad2.left_stick_button){
                // i had to leave before doing the jewel swatter part but do that and its done
            }
        }
    }
}
