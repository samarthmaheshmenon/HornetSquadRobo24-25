package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.TensorFlowLite;


@TeleOp (name = "Tensor Flow Tester")
public class TensorFlowTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TensorFlowLite tensorFlowLite = new TensorFlowLite(this);

        waitForStart();

        tensorFlowLite.activateTfod();
        while (opModeIsActive()){

            tensorFlowLite.getAngleOfObject();



        }
        tensorFlowLite.shutDownTfod();
    }
}
