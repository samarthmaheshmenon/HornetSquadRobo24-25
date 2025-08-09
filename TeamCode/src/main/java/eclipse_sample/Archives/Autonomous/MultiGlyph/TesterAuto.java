package eclipse_sample.Archives.Autonomous.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@Autonomous(name = "Tester A")
public class TesterAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        robot.addAndUpdateTelemetry("Ready to go!");
        waitForStart();

        robot.driveTrain.moveToInches( 18, .25);
        robot.driveTrain.gyroTurn(.05,90);
        robot.driveTrain.moveToInches( 18, .25);
        robot.driveTrain.gyroTurn(.05,180);
        robot.driveTrain.moveToInches( 18, .25);
        robot.driveTrain.gyroTurn(.05,270);
        robot.driveTrain.moveToInches( 18, .25);
        robot.driveTrain.park();
    }
}
