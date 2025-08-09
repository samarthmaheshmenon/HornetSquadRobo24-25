package eclipse_sample.Archives.Testers;

/**
 * Created by Yazan on 7/12/2018.
 */
/*
Simple Autonomous code designed to navigate a pre programmed maze layout without sensors.
Uses preprogrammed distances similar to Autonomous movement to VuForia pattern in Relic Recovery
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;


@Disabled
@Autonomous(name = "YAZAN_AUTO!")
public class AutonomousSummerChallenge extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, false, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready!");

        while (!isStarted()) {
            telemetry.addLine("RTG!");
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()) {
            robot.driveTrain.setAll(.5);
            //robot.driveTrain.moveToPositionInches(36,1);
            telemetry.addLine("moved");
            telemetry.update();
        /*robot.driveTrain.turnRelative(90, .5);
        robot.driveTrain.moveToInches(144, .5);
        robot.driveTrain.turnRelative(90, .5);
        robot.driveTrain.moveToInches(144, .5);
        robot.driveTrain.turnRelative(90, .5);
        robot.driveTrain.moveToInches(108, .5);*/
        }

    }
}
