package eclipse_sample.Archives.Autonomous.SensorArm;

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
@Autonomous(name = "Blue FAR Color")
public class AutonomousFarIntakeBlueColor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        double TURN_SPEED_MODIFIER = 1;
        double STRAFE_SPEED_MODIFIER = 1;
        double FORWARDS_SPEED_MODIFIER = 1;

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

        robot.relicMecanism.swingAwayFromWall();

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }
        robot.driveTrain.moveToInches(3, .25 * FORWARDS_SPEED_MODIFIER);
        robot.driveTrain.gyroTurn(.05 * TURN_SPEED_MODIFIER, -90);
        robot.driveTrain.autoWallDistanceSensor(100, .25, DistanceUnit.CM, 5);

        double targetAngle = 180;

        robot.driveTrain.gyroTurn(.05 * TURN_SPEED_MODIFIER, targetAngle);

        robot.driveTrain.autoWallDistanceSensor(28, .15 * FORWARDS_SPEED_MODIFIER, DistanceUnit.CM);

        robot.relicMecanism.swingAwayFromWall();

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }


        robot.driveTrain.gyroTurn(.05 * TURN_SPEED_MODIFIER, targetAngle);

        double power = .1;
        double distance = UniversalConstants.distanceBlueODS;
        DistanceUnit unit = DistanceUnit.CM;

        switch (vuMark) {
            case LEFT:
                robot.driveTrain.swingColorDistanceDown();
                robot.driveTrain.strafeToDistanceLeftCoast(power * STRAFE_SPEED_MODIFIER, distance, targetAngle, unit);
                robot.driveTrain.translateBy(0, -power * STRAFE_SPEED_MODIFIER, 0);
                robot.driveTrain.swingColorDistanceUp();
                robot.driveTrain.encoderStrafeToInches(1.5, power * STRAFE_SPEED_MODIFIER, targetAngle);
                robot.driveTrain.swingColorDistanceDown();
            case CENTER:
                robot.driveTrain.swingColorDistanceDown();
                robot.driveTrain.strafeToDistanceLeftCoast(power * STRAFE_SPEED_MODIFIER, distance, targetAngle, unit);
                robot.driveTrain.translateBy(0, -power * STRAFE_SPEED_MODIFIER, 0);
                robot.driveTrain.swingColorDistanceUp();
                robot.driveTrain.encoderStrafeToInches(1.5, power * STRAFE_SPEED_MODIFIER, targetAngle);
                robot.driveTrain.swingColorDistanceDown();
            case RIGHT:
            default:
                robot.driveTrain.park();
                robot.driveTrain.gyroTurn(.05 * TURN_SPEED_MODIFIER, targetAngle);
                robot.driveTrain.swingColorDistanceDown();
                robot.driveTrain.park();
                robot.driveTrain.strafeToDistanceLeft(.07 * STRAFE_SPEED_MODIFIER, distance, targetAngle, unit);
                robot.driveTrain.park();
                break;
        }

        robot.driveTrain.storeColorDistance();

        robot.driveTrain.moveToInches(-3, .15 * FORWARDS_SPEED_MODIFIER);
        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.intake();
        sleep(50);
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.setIntakePowers(.5, -.5);
        sleep(500);
        robot.intakeMecanism.setIntakePowers(-.5, -.5);
        robot.driveTrain.moveToInches(7, .15 * FORWARDS_SPEED_MODIFIER);

        robot.driveTrain.moveToInches(-8, .15 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.stopIntake();
        robot.relicMecanism.storeServos();

        stop();
    }
}
