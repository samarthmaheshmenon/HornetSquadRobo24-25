package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;

@Disabled
@Autonomous(name = "Drivetrain Auto")
public class AutoTesterDrivetrain extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Drivetrain driveTrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        driveTrain.moveInches(25,1);
    }
}
