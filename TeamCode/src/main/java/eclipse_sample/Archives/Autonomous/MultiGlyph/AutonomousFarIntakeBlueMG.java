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
@Autonomous(name = "Blue FAR Special")
public class AutonomousFarIntakeBlueMG extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Red, robot);
        waitForStart();
        double startTime = getRuntime();


        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.jewelSwatter.removeJewelOfColor(color);

        AutonomousUtil.driveRobotOffRamp(robot, AutonomousUtil.AllianceColor.Blue);

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double FAR_DISTANCE = 8.25;
        double MIDDLE_DISTANCE = 5.5;
        double CLOSE_DISTANCE = 4;

        double FAR_US_DISTANCE = 86;
        double MIDDLE_US_DISTANCE = 69;
        double CLOSE_US_DISTANCE = 54;

        double FAR_FACING_US_DISTANCE = 82;
        double MIDDLE_FACING_US_DISTANCE = 66;

        double moveToPositionPower = .2;
        double targetAngle = 0;

        telemetry.addData("VuMark", vuMark);
        telemetry.update();

        switch (vuMark) {
            case RIGHT:
                robot.driveTrain.encoderStrafeToInches(FAR_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(FAR_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
            case CENTER:
                robot.driveTrain.encoderStrafeToInches(MIDDLE_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(MIDDLE_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
            default:
            case LEFT:
                robot.driveTrain.encoderStrafeToInches(CLOSE_DISTANCE, moveToPositionPower, targetAngle);
                robot.driveTrain.autoRightDistanceSensor(CLOSE_US_DISTANCE, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
                break;
        }

        robot.driveTrain.park();

        robot.relicMecanism.swingElbowUp();
        sleep(500);
        robot.driveTrain.moveToInches(-4, .4);
        robot.driveTrain.moveToInches(4, .6);
        robot.driveTrain.gyroTurn(.1, targetAngle);
        robot.driveTrain.autoRightDistanceSensor(FAR_US_DISTANCE - 5, .5 * moveToPositionPower, targetAngle, DistanceUnit.CM, 2);
        robot.driveTrain.park();

        targetAngle = 180 - 155;

        robot.driveTrain.gyroTurn(.1, targetAngle);
        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInchesCoast(45, .75);
        robot.driveTrain.moveToInches(-20, .4);
        targetAngle = 180;
        robot.relicMecanism.swingAwayFromWall();

        robot.driveTrain.gyroTurn(.1, targetAngle);
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.driveTrain.park();
        robot.driveTrain.autoWallDistanceSensor(45, .45, DistanceUnit.CM, 20);
        robot.driveTrain.autoWallDistanceSensor(45, .2, DistanceUnit.CM, 2);

        if (vuMark == RelicRecoveryVuMark.RIGHT) {
            robot.driveTrain.autoLeftDistanceSensor(MIDDLE_FACING_US_DISTANCE, .15, targetAngle, DistanceUnit.CM, 1);
        } else {
            robot.driveTrain.autoLeftDistanceSensor(FAR_FACING_US_DISTANCE, .15, targetAngle, DistanceUnit.CM, 1);
        }

        robot.driveTrain.gyroTurn(.05, targetAngle - 15);

        robot.driveTrain.moveToInches(4, .15);
        robot.intakeMecanism.outtakeFully();

        robot.slamDunker.dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed * 3);
        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() + 150);

        robot.intakeMecanism.setIntakePowers(-.5, .5);
        sleep(500);

        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() - 150);

        robot.intakeMecanism.setIntakePowers(-1);
        robot.slamDunker.dunkMotor.setPower(0);
        robot.driveTrain.moveToInches(6, .15);

        robot.driveTrain.moveToInches(-8, .20);
    }
}
