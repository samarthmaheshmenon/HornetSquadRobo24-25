package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Customizable Auto")
public class AutonomousCustomizable extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        double distance = 12, movePower = .25, turnPower = .2, angle = -90;
        //what worked:
        // red close worked as 12/.25/.2/-90

        Controller lastPad1 = new Controller(gamepad1);
        Controller lastPad2 = new Controller(gamepad2);
        while (!isStarted()) {
            lastPad1.update();
            lastPad2.update();
            if (lastPad1.dpadLeftOnce()) {
                distance += .25;
            } else if (lastPad1.dpadRightOnce()) {
                distance -= .25;
            }
            if (lastPad1.dpadUpOnce()) {
                movePower += .05;
            } else if (lastPad1.dpadDownOnce()) {
                movePower -= .05;
            }
            if (lastPad2.dpadLeftOnce()) {
                angle += 5;
            } else if (lastPad2.dpadRightOnce()) {
                angle -= 5;
            }
            if (lastPad2.dpadUpOnce()) {
                turnPower += .05;
            } else if (lastPad2.dpadDownOnce()) {
                turnPower -= .05;
            }
            telemetry.addData("Move Distance", distance);
            telemetry.addData("Move Power", movePower);
            telemetry.addData("Angle", angle);
            telemetry.addData("Turn Power", turnPower);
            telemetry.addLine("Ready to Go!");
            telemetry.update();
        }

        waitForStart();
        double startTime = getRuntime();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        robot.driveTrain.moveToInches(27, .15);
        robot.driveTrain.gyroTurn(.1, 0);

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);

        robot.driveTrain.moveToInches(distance, movePower);
        robot.driveTrain.gyroTurn(turnPower, angle);
        robot.scoreBlock();
        telemetry.addData("Time Taken", getRuntime() - startTime);
        telemetry.addData("Move Distance", distance);
        telemetry.addData("Move Power", movePower);
        telemetry.addData("Angle", angle);
        telemetry.addData("Turn Power", turnPower);
        telemetry.update();
        while(opModeIsActive()){
            idle();
        }
    }
}
