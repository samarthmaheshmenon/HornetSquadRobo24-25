package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;

@Disabled
@TeleOp (name = "Lift Tester")
public class liftTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        LiftModule liftModule = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive()){
            liftModule.updateAll();
            //liftModule.moveToPosition(8880);
        }
    }
}
