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
@Autonomous(name = "Blue CLOSE")
public class AutonomousCloseIntakeBlueEncoder extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Red, robot);
        double moveToPositionPower = .2;

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.relicMecanism.swingElbowUp();
        robot.jewelSwatter.removeJewelOfColor(color);
        AutonomousUtil.driveRobotOffRamp(robot, AutonomousUtil.AllianceColor.Blue);


        robot.driveTrain.gyroTurn(.05, 0);
        robot.driveTrain.gyroTurn(.05, 0);
        robot.driveTrain.gyroTurn(.05, 0);

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);

        double closestPosition = 4;
        double targetAngle = -90;

        switch (vuMark) {
            case CENTER:
                robot.driveTrain.moveToPositionInches(closestPosition - 6.5, .75);
                break;
            case RIGHT:
                robot.driveTrain.moveToPositionInches(closestPosition - 12.5, .75);
                break;
            case LEFT:
            default:
                robot.driveTrain.moveToPositionInches(closestPosition, .75);
                break;
        }

        robot.driveTrain.gyroTurn(.05, targetAngle);
        robot.driveTrain.gyroTurn(.05, targetAngle);
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.intake();
        sleep(250);
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.outtake();

        robot.driveTrain.moveToInches(3, moveToPositionPower);
        robot.driveTrain.moveToInches(-5, moveToPositionPower);
        robot.driveTrain.moveToInches(5, moveToPositionPower);
        robot.driveTrain.moveToInches(-10, moveToPositionPower);
        robot.driveTrain.moveToInches(8, moveToPositionPower);
        robot.driveTrain.moveToInches(-10, moveToPositionPower);
    }
}
