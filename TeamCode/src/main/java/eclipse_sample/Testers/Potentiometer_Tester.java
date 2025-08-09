package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

@Disabled
@Autonomous(name = "Potentiometer Test")
public class Potentiometer_Tester extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        AnalogInput P = hardwareMap.analogInput.get("P");
        waitForStart();
        while(opModeIsActive()){
            double voltage = P.getVoltage();
            int angle = (int) Math.rint(voltage*81.9423368741);
            telemetry.addData("Voltage", voltage);
            telemetry.addData("Angle", angle);
            telemetry.update();
        }
    }
}
