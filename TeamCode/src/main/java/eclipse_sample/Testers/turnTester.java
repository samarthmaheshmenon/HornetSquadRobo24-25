package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;

@Disabled
@Autonomous(name = "turn Tests")
public class turnTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain driveTrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();
        driveTrain.reverseArcTurn(20,1,-90);
    }
}
