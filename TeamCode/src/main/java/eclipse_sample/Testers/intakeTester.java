package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.Intake;

@TeleOp (name = "Intake Tester")
public class intakeTester extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException{
        Intake intake = new Intake(this);

        waitForStart();

        while(opModeIsActive()){
            intake.update();
        }
    }
}
