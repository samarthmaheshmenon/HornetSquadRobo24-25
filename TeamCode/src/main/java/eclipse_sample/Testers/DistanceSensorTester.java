package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.SensorDistance;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp (name = "Distance Sensor Tester")
public class DistanceSensorTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SensorDistance sensorDistance = new SensorDistance(this, UniConstTwo.distanceSensorFront);
        waitForStart();

        while (opModeIsActive()){
            double distance = sensorDistance.getDistanceUltra();
            telemetry.addData("Distance: ", distance);
            telemetry.update();
        }
    }
}
