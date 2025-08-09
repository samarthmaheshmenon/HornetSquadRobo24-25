package eclipse_sample.Archives.Autonomous.EncoderOnly;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Blue FAR")
public class AutonomousFarIntakeBlueEncoder extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Red, robot);

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.relicMecanism.swingElbowUp();
        robot.jewelSwatter.removeJewelOfColor(color);
        AutonomousUtil.driveRobotOffRamp(robot, AutonomousUtil.AllianceColor.Blue);
        robot.driveTrain.moveToPositionInches(2, .25);

        robot.driveTrain.gyroTurn(.15, 90);
        robot.driveTrain.gyroTurn(.05, 90);

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);
        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double closestPosition = 8.5;
        double moveToPositionPower = .2;
        switch (vuMark) {
            case RIGHT:
                robot.driveTrain.moveToPositionInches(closestPosition + 12, .75);
                break;
            case CENTER:
                robot.driveTrain.moveToPositionInches(closestPosition + 6, .75);
                break;
            case LEFT:
            default:
                robot.driveTrain.moveToPositionInches(closestPosition, .75);
                break;
        }

        robot.driveTrain.gyroTurn(.25, 180);
        robot.driveTrain.gyroTurn(.05, 180);
        robot.driveTrain.moveToInches(3, moveToPositionPower);
        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.intake();
        sleep(250);
        robot.intakeMecanism.outtake();
        sleep(1000);
        robot.driveTrain.moveToInches(3, moveToPositionPower);
        robot.driveTrain.moveToInches(-5, moveToPositionPower);
        robot.driveTrain.moveToInches(5, moveToPositionPower);
        robot.driveTrain.moveToInches(-10, moveToPositionPower);
        robot.driveTrain.moveToInches(8, moveToPositionPower);
        robot.driveTrain.moveToInches(-5, moveToPositionPower);
    }
}
