package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@TeleOp(name = "Time Tester")
public class TimeTester extends LinearOpMode{
    public void runOpMode() throws InterruptedException {
        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime elapsedTime1 = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        int test = 0;
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Time Elapsed (ms): ", elapsedTime);
            telemetry.addData("Time Elapsed (ms): ", elapsedTime.time());
            telemetry.addData("Time Elapsed (s): ", elapsedTime1.time());
            telemetry.addData("Loop Number: ",test);
            telemetry.update();
            test++;
        }
    }
}