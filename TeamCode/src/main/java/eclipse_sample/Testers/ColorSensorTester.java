package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;

@TeleOp(name = "Color Sensor Tester")
public class ColorSensorTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this,DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Initializded");
        telemetry.update();
        waitForStart();

//        while (opModeIsActive()) {
//            telemetry.addData("Sensor 3 Alpha: ", arm.sensor1.alpha());
//            telemetry.addData("Sensor 3 Blue/Red: ", (double)arm.sensor1.blue() / (double)arm.sensor1.red());
//            telemetry.addData("Slot one: ", arm.goldOne());
//            telemetry.addData("Slot one Blue/Red: ", (double)arm.sensor3.blue() / (double)arm.sensor3.red());
//            telemetry.addData("Slot two: ", arm.goldTwo());
//            telemetry.addData("Slot two Blue/Red: ", (double)arm.sensor2.blue() / (double)arm.sensor2.red());
//
//            telemetry.update();
//        }
    }
}