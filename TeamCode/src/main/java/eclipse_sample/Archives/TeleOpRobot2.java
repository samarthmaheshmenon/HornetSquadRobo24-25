package eclipse_sample.Archives;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@TeleOp(name = "Tele Op")
public class TeleOpRobot2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, false, false, false, DcMotor.ZeroPowerBehavior.FLOAT);

        robot.driveTrain.setAll(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();
        robot.relicMecanism.swingElbowUp();
        robot.jewelSwatter.zeroSwatter();
        robot.intakeMecanism.deployFoldoutIntake();
        while (opModeIsActive()) {
            robot.updateAll();
        }
        robot.relicMecanism.swingElbowUp();
    }
}