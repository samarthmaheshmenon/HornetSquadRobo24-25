package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.IntakeMecanism;

@Disabled
@TeleOp(name = "Test Intake_2018OLD Velocity")
public class TestVelocityReadings extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        IntakeMecanism intakeMecanism = new IntakeMecanism(this);
        waitForStart();
        while (opModeIsActive()) {
            intakeMecanism.updateAll();
            telemetry.addData("Velocity 1", (int)intakeMecanism.getVelocities()[0]);
            telemetry.addData("Velocity 2", (int)intakeMecanism.getVelocities()[1]);
            telemetry.addData("Average Velocity", (int)((intakeMecanism.getVelocities()[0] + intakeMecanism.getVelocities()[1]) / 2));
            telemetry.update();
        }

    }
}
