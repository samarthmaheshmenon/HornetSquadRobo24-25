package eclipse_sample.Archives.Autonomous.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Blue CLOSE MG 3")
public class AutonomousCloseIntakeBlueMGWeird extends LinearOpMode {

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
        robot.intakeMecanism.deployFoldoutIntake();
        robot.jewelSwatter.removeJewelOfColor(color);

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double targetAngle = 90;

        double closestPosition = -27;
        switch (vuMark) {
            case CENTER:
                robot.driveTrain.moveToInches(closestPosition - 6.75, .25);
                break;
            case RIGHT:
                robot.driveTrain.moveToInches(closestPosition - 13.5, .25);
                break;
            case LEFT:
            default:
                robot.driveTrain.moveToInches(closestPosition, .25);
                break;
        }
        robot.driveTrain.park();
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.deployFoldoutIntake();
        robot.driveTrain.park();
        robot.driveTrain.moveToInches(-1, .4);
        robot.relicMecanism.swingElbowStayPinched();
        sleep(500);
        robot.driveTrain.moveToInches(-3, .5);
        robot.driveTrain.moveToInches(5, .6);

        targetAngle = 90;
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(25, .65);
        robot.driveTrain.moveToInches(-20, .65);

        targetAngle = -90;

        robot.driveTrain.gyroTurn(.05, targetAngle);

        switch (vuMark) {
            case CENTER:
            case RIGHT:
                //left 1 column
                robot.driveTrain.encoderStrafeToInches(3, .25);
                break;
            case LEFT:
            default:
                //right 1 column
                robot.driveTrain.encoderStrafeToInches(-3, .25);
                break;
        }

        robot.driveTrain.gyroTurn(.05, targetAngle);
        robot.driveTrain.moveToInches(4, .5 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.outtakeFully();

        robot.slamDunker.dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed * 3);
        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() + 150);

        robot.intakeMecanism.setIntakePowers(-.5, .5);
        sleep(500);

        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() - 150);

        robot.intakeMecanism.setIntakePowers(-1);
        robot.slamDunker.dunkMotor.setPower(0);
        robot.driveTrain.moveToInches(-10, .15);

        targetAngle = 90;
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(35, .65);
        robot.driveTrain.moveToInches(-25, .65);

        targetAngle = -90;

        robot.driveTrain.gyroTurn(.05, targetAngle);

        switch (vuMark) {
            case CENTER:
                //right 2 columns
                robot.driveTrain.encoderStrafeToInches(-6.5, .25);
                break;
            case RIGHT:
                //left 1 column
                robot.driveTrain.encoderStrafeToInches(3, .25);
                break;
            case LEFT:
            default:
                //right 1 column
                robot.driveTrain.encoderStrafeToInches(-3, .25);
                break;
        }

        robot.driveTrain.gyroTurn(.05, targetAngle);
        robot.driveTrain.moveToInches(13, .5 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.outtakeFully();

        robot.slamDunker.dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed * 3);
        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() + 150);

        robot.intakeMecanism.setIntakePowers(-.5, .5);
        sleep(500);

        robot.slamDunker.dunkMotor.setTargetPosition(robot.slamDunker.dunkMotor.getTargetPosition() - 150);

        robot.intakeMecanism.setIntakePowers(-1);
        robot.slamDunker.dunkMotor.setPower(0);
        robot.driveTrain.moveToInches(-8, .15);

        robot.relicMecanism.swingElbowUp();
    }
}
