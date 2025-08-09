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
@Autonomous(name = "Red FAR")
public class AutonomousFarIntakeRedEncoder extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Blue, robot);

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.relicMecanism.swingElbowUp();
        robot.jewelSwatter.removeJewelOfColor(color);
        AutonomousUtil.driveRobotOffRamp(robot, AutonomousUtil.AllianceColor.Red);
        robot.driveTrain.moveToPositionInches(-4, .7);

        robot.driveTrain.gyroTurn(.15, 90);
        robot.driveTrain.gyroTurn(.05, 90);

        robot.jewelSwatter.wristServo.setPosition(UniversalConstants.jewelWristStored);
        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double closestPosition = 13;
        double moveToPositionPower = .2;
        switch (vuMark) {
            case LEFT:
                robot.driveTrain.moveToPositionInches(closestPosition + 12.75, .75);
                break;
            case CENTER:
                robot.driveTrain.moveToPositionInches(closestPosition + 6.5, .75);
                break;
            default:
            case RIGHT:
                robot.driveTrain.moveToPositionInches(closestPosition, .75);
                break;
        }

        robot.driveTrain.gyroTurn(.25, 0);
        robot.driveTrain.gyroTurn(.05, 0);
        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.intake();
        sleep(250);
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.outtake();
        robot.driveTrain.moveToInches(8, moveToPositionPower);
        robot.driveTrain.moveToInches(-5, moveToPositionPower);
        robot.driveTrain.moveToInches(5, moveToPositionPower);
        robot.driveTrain.moveToInches(-10, moveToPositionPower);
        robot.driveTrain.moveToInches(8, moveToPositionPower);
        robot.driveTrain.moveToInches(-5, moveToPositionPower);
    }
}