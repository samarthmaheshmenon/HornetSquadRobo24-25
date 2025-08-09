package eclipse_sample.Testers;

import static org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo.liftExtended;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;

@Disabled
@Autonomous(name = "Lift Auto")
public class liftAuto extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException{
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        lift.moveToPosition(liftExtended);
    }

}

