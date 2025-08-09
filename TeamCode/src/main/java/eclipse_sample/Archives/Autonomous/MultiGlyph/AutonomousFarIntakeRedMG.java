package eclipse_sample.Archives.Autonomous.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Red FAR Multi Glpyh")
public class AutonomousFarIntakeRedMG extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.Settings settings = AutonomousUtil.getStartingSettings(this, AutonomousUtil.AllianceColor.Blue, robot);
        AutonomousUtil.AllianceColor color = settings.allianceColor;

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.relicMecanism.swingAwayFromWall();
        robot.jewelSwatter.removeJewelOfColor(color);

        robot.driveTrain.autoWallDistanceSensor(30, .15, DistanceUnit.CM);

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);
        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double FAR_DISTANCE = 7.50;
        double CLOSE_DISTANCE = 3;
        double MIDDLE_DISTANCE = 4.75;
        double FAR_US_DISTANCE = 90;
        double CLOSE_US_DISTANCE = 55;
        double MIDDLE_US_DISTANCE = 71;

        double moveToPositionPower = .2;
        double targetAngle = 0;
        switch (vuMark) {
            case LEFT:
                robot.driveTrain.encoderStrafeToInches(FAR_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(FAR_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
            case CENTER:
                robot.driveTrain.encoderStrafeToInches(MIDDLE_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(MIDDLE_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
            default:
            case RIGHT:
                robot.driveTrain.encoderStrafeToInches(CLOSE_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(CLOSE_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
        }
        robot.driveTrain.park();

        robot.driveTrain.gyroTurn(.25, 0);
        robot.driveTrain.gyroTurn(.05, 0);
        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.intake();
        sleep(200);
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.setIntakePowers(.5, -.5);
        sleep(500);
        robot.intakeMecanism.setIntakePowers(-1);
        robot.driveTrain.moveToInches(4, .15);

        robot.driveTrain.moveToInches(-8, .15);
        robot.intakeMecanism.stopIntake();

        switch (vuMark) {
            case LEFT:
                robot.driveTrain.moveToInches(-2, .25);
                break;
            case CENTER:
                robot.driveTrain.encoderStrafeToInches(FAR_DISTANCE - MIDDLE_DISTANCE, moveToPositionPower, targetAngle);
                break;
            default:
            case RIGHT:
                robot.driveTrain.encoderStrafeToInches(FAR_DISTANCE - CLOSE_DISTANCE, moveToPositionPower, targetAngle);
                break;
        }
        robot.driveTrain.autoRightDistanceSensor(FAR_US_DISTANCE, .3, targetAngle, DistanceUnit.CM, 2);
        robot.driveTrain.park();

        targetAngle = settings.turnAngle;

        robot.driveTrain.gyroTurn(.1, targetAngle);
        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(settings.distance, .65);
        robot.driveTrain.moveToInches(-(settings.distance - 20), .65);

        targetAngle = 0;
        robot.driveTrain.gyroTurn(.1, targetAngle);

        robot.driveTrain.autoWallDistanceSensor(35, .35, DistanceUnit.CM, 20);
        robot.driveTrain.autoWallDistanceSensor(35, .15, DistanceUnit.CM);

        robot.driveTrain.gyroTurn(.05, targetAngle);

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            robot.driveTrain.encoderStrafeToInches(-5, moveToPositionPower, targetAngle);
            robot.driveTrain.autoRightDistanceSensor(MIDDLE_US_DISTANCE + 6, .5, targetAngle, DistanceUnit.CM, 5);
        } else {
            robot.driveTrain.autoRightDistanceSensor(FAR_US_DISTANCE + 6, .5, targetAngle, DistanceUnit.CM, 3);
        }

        robot.driveTrain.gyroTurn(.05, -15);

        robot.driveTrain.moveToInches(4, .15);
        robot.intakeMecanism.outtakeFully();

        robot.slamDunker.dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed * 3);
        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() + 75);

        robot.intakeMecanism.setIntakePowers(.5, -.5);
        sleep(500);
        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() - 75);
        robot.intakeMecanism.setIntakePowers(-1);
        robot.slamDunker.dunkMotor.setPower(0);
        robot.driveTrain.moveToInches(6, .15);

        robot.driveTrain.moveToInches(-6, .25);
    }
}
