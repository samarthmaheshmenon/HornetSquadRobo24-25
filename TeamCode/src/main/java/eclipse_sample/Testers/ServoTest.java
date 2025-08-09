package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;


@TeleOp(name = "Servo Test")
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo dispenser = this.hardwareMap.servo.get(UniConstTwo.dispenser);

        dispenser.setDirection(Servo.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive()) {
            dispenser.setPosition(.9);
            double pos = dispenser.getPosition();
            telemetry.addData("Position: ", pos);
            telemetry.update();
        }
    }
}
