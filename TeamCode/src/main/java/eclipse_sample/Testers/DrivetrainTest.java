package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;

@Disabled
@TeleOp(name = "No Arm TeleOp Trainer")
public class DrivetrainTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain driveTrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule liftModule = new LiftModule(this,DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive()){
            driveTrain.updateAll();
            liftModule.updateAll();
        }

    }
}
