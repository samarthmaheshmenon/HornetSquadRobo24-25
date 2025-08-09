package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.SlamDunker;

@Disabled
@TeleOp(name = "Slam Dunk Tester")
public class SlamDunkTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SlamDunker slamDunker = new SlamDunker(this);

        waitForStart();

        while (opModeIsActive()) {
            slamDunker.updateByGamepad();
            slamDunker.updateTelemetry();
            telemetry.update();
        }
    }
}
