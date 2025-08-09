package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@Autonomous(name = "Vuforia Trial Demo")
public class VuforiaTrialTester extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("RTG");
        waitForStart();
        RelicRecoveryVuMark vuMark = null;
        while (opModeIsActive()) {
            robot.vuforiaRelicRecoveryGetter.activateTrackables();


            while (robot.vuforiaRelicRecoveryGetter.getPattern() == RelicRecoveryVuMark.LEFT) {
                robot.driveTrain.setAll(.05);
                if (robot.vuforiaRelicRecoveryGetter.getPattern() == null) {
                    robot.driveTrain.park();
                    break;
                }
            }
                robot.driveTrain.park();


                while (robot.vuforiaRelicRecoveryGetter.getPattern() == RelicRecoveryVuMark.RIGHT) {
                    robot.driveTrain.setAll(-.05);
                    if (robot.vuforiaRelicRecoveryGetter.getPattern() == null) {
                        robot.driveTrain.park();
                        break;
                    }
                }
                robot.driveTrain.park();
            }
        }
    }
