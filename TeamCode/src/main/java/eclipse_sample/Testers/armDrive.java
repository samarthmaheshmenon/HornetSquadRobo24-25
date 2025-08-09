package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp(name = "Arm Drive")
public class armDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        //ArmOperator arm = new ArmOperator(this);

        telemetry.addLine("Let's roll joint");
        telemetry.update();

        float leftX; // velocity in the x-direction
        float leftY; // velocity in the y-direction
        double x = 6.143; // starting x
        double y = 5.2; // starting y

        waitForStart();

        while (opModeIsActive()) {
            leftX = this.gamepad1.left_stick_x;
//            leftY = -1*this.gamepad1.left_stick_y;

            // TODO: Check max/min bounds for each servo

            double j1 = UniConstTwo.jointOneAngle(x, y);
            double j3 = UniConstTwo.jointThreeAngle(x, y);

            // TODO: Black-box setAll() function
            //arm.setAll(UniConstTwo.stowedOne-UniConstTwo.angleToTicks(j1), UniConstTwo.maxTwo, UniConstTwo.stowedThree+UniConstTwo.angleToTicks(j3),UniConstTwo.maxFour);
            telemetry.addLine("("+x+","+y+"): " + Math.floor(j1) + "; " + Math.floor(j3));
            telemetry.update();

            x += leftX*UniConstTwo.maxArmSpeed;
//            y += leftY*UniConstTwo.maxArmSpeed;

            Thread.sleep(50);
        }
    }

}
