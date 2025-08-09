package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.VuforiaRelicRecoveryGetter;

@Disabled
@Autonomous(name = "Vuforia Tester")
public class VuforiaTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaRelicRecoveryGetter vuforiaRelicRecoveryGetter = new VuforiaRelicRecoveryGetter(this);
        waitForStart();
        vuforiaRelicRecoveryGetter.activateTrackables();
        while (opModeIsActive()) {
            vuforiaRelicRecoveryGetter.updateTelemetry();
            telemetry.update();
        }
    }
}
