package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;


@TeleOp(name = "Lift Reset")
public class liftReseter extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        LiftModule liftModule = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive()){
            liftModule.resetLift();
            liftModule.updateTelemetry();
        }
    }
}
