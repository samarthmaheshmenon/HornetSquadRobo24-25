package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.IntakeMecanism;

@Disabled
@TeleOp(name = "Intake_2018OLD Tester")
public class IntakeTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        IntakeMecanism intakeMecanism = new IntakeMecanism(this);

        waitForStart();

        while (opModeIsActive()) {
            intakeMecanism.updateByGamepad();
            intakeMecanism.updateTelemetry();
            telemetry.update();
        }
    }
}
