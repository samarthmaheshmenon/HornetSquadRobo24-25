package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@Autonomous(name = "Color Sensor Tester")
public class ColorSensorTester extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, "crd");
        DistanceSensor distanceSensor = hardwareMap.get(DistanceSensor.class, "crd");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Red value: ", colorSensor.red());
            telemetry.addData("Blue value: ", colorSensor.blue());
            telemetry.addData("Green value: ", colorSensor.green());
            telemetry.addData("Distance: ", distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
