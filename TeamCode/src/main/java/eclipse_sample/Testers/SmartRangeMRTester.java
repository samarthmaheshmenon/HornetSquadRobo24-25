package eclipse_sample.Testers;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.SmartRangeMR;

@Disabled
@TeleOp (name = "SmartRange Sensor")
public class SmartRangeMRTester extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        SmartRangeMR smartRangeMR = new SmartRangeMR(hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range"));

        waitForStart();

        while (opModeIsActive()){
            double distance = smartRangeMR.getDistance(DistanceUnit.CM);
            telemetry.addData("Distance: ", distance);
            telemetry.update();
        }
    }
}
