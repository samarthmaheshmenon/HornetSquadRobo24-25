package eclipse_sample.Archives.Autonomous.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@Autonomous(name = "Red CLOSE MG")
public class AutonomousCloseIntakeRedMG extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        double TURN_SPEED_MODIFIER = 1;
        double STRAFE_SPEED_MODIFIER = 1;
        double FORWARDS_SPEED_MODIFIER = 1;

        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        RelicRecoveryVuMark vuMark;
        AutonomousUtil.AllianceColor color = AutonomousUtil.getColorToDislodge(this, AutonomousUtil.AllianceColor.Blue, robot);

        waitForStart();

        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        robot.relicMecanism.swingElbowStayPinched();
        robot.intakeMecanism.deployFoldoutIntake();
        robot.jewelSwatter.removeJewelOfColor(color);

        robot.relicMecanism.swingAwayFromWall();

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = robot.vuforiaRelicRecoveryGetter.getPattern();
        }

        double targetAngle = -90;

        double closestPosition = 27;
        switch (vuMark) {
            case CENTER:
                robot.driveTrain.moveToInches(closestPosition + 6.75, .25);
                break;
            case LEFT:
                robot.driveTrain.moveToInches(closestPosition + 13.5, .25);
                break;
            case RIGHT:
            default:
                robot.driveTrain.moveToInches(closestPosition, .25);
                break;
        }
        robot.driveTrain.park();
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.driveTrain.moveToInches(2.5, .25 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.deployFoldoutIntake();
        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.setIntakePowersOverride(-1);
        robot.driveTrain.moveToInches(4, .15 * FORWARDS_SPEED_MODIFIER);

        robot.driveTrain.moveToInches(-10, .15 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.stopIntake();
        robot.relicMecanism.storeServos();

        targetAngle = 90;
        robot.driveTrain.gyroTurn(.05, targetAngle);

        robot.intakeMecanism.intake();
        robot.driveTrain.moveToInches(25, .25);
        robot.driveTrain.moveToInches(-20, .25);

        targetAngle = -90;

        switch (vuMark) {
            case CENTER:
                robot.driveTrain.gyroTurn(.05, targetAngle - 25);
                break;
            case LEFT:
                robot.driveTrain.gyroTurn(.05, targetAngle - 30);
                break;
            case RIGHT:
            default:
                robot.driveTrain.gyroTurn(.05, targetAngle + 30);
                break;
        }

        robot.driveTrain.moveToInches(15, .5 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.outtakeFully();
        robot.intakeMecanism.setIntakePowers(-1);

        robot.driveTrain.moveToInches(3, .15 * FORWARDS_SPEED_MODIFIER);
        robot.driveTrain.moveToInches(-8, .15 * FORWARDS_SPEED_MODIFIER);

        robot.intakeMecanism.stopIntake();
        robot.relicMecanism.storeServos();

    }
}
