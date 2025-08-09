package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.Robot;
@Disabled
@TeleOp (name = "Robot TeleOp")
public class RobotTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this);
        //Joint3 joint3 = new Joint3(this, UniConstTwo.armJoint3, UniConstTwo.joint3Pot);

        waitForStart();
        //robot.initialize();
        while (opModeIsActive()){
            //robot.brakeAll();
            robot.updateAll();
        }
    }
}
