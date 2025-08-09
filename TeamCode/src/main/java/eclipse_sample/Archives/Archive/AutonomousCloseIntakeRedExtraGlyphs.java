package eclipse_sample.Archives.Archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Red CLOSE Intake_2018OLD (85+)")
public class AutonomousCloseIntakeRedExtraGlyphs extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Blue, robot);
        double moveToPositionPower = .2;

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.jewelSwatter.removeJewelOfColor(color);
        AutonomousUtil.driveRobotOffRamp(robot, AutonomousUtil.AllianceColor.Red);

        robot.intakeMecanism.deployFoldoutIntake();

        robot.intakeMecanism.intake();
        sleep(100);
        robot.intakeMecanism.stopIntake();

        robot.driveTrain.gyroTurn(.2, 0);
        robot.driveTrain.gyroTurn(.05, 0);

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);

        double closestPosition = 3;
        double targetAngle = -90;

        switch (vuMark) {
            case CENTER:
                robot.driveTrain.moveToInches(closestPosition + 3, moveToPositionPower);
                break;
            case LEFT:
                robot.driveTrain.moveToInches(closestPosition + 7.5, moveToPositionPower);
                break;
            default:
            case RIGHT:
                robot.driveTrain.moveToInches(closestPosition - 2, moveToPositionPower);
                break;
        }

        robot.driveTrain.gyroTurn(.25, targetAngle);
        robot.driveTrain.gyroTurn(.05, targetAngle);

        targetAngle = -targetAngle;

        robot.driveTrain.moveToInches(5, .25);
        robot.intakeMecanism.outtake();
        sleep(250);
        robot.driveTrain.moveToInches(-10, .25);
        robot.intakeMecanism.stopIntake();
        robot.driveTrain.gyroTurn(.25, targetAngle);
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(22, .25);
        robot.driveTrain.moveToInches(-2, .25);
        robot.driveTrain.gyroTurn(.25, targetAngle + 25);
        robot.driveTrain.gyroTurn(.05, targetAngle + 25);
        robot.driveTrain.moveToInches(5, .25);
        robot.driveTrain.gyroTurn(.05, targetAngle);
        robot.driveTrain.moveToInches(-5, .25);
        robot.intakeMecanism.stopIntake();
        robot.driveTrain.moveToInches(-10, .25);
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(4, .25);
        robot.driveTrain.moveToInches(-14, .25);
        robot.driveTrain.gyroTurn(.25, targetAngle + 15);
        robot.driveTrain.gyroTurn(.05, targetAngle + 15);

        robot.intakeMecanism.stopIntake();
        robot.slamDunker.dunkSlowWithAutoStop();
        sleep(500);
        robot.slamDunker.retractDunkNoWait();
        robot.driveTrain.moveToInches(5, .5);
    }

}
