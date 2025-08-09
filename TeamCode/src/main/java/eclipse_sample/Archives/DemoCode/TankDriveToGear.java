package eclipse_sample.Archives.DemoCode;/*package org.firstinspires.ftc.teamcode.DemoCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.UniversalConstants;

@Disabled
@Autonomous(name = "Vuforia Drive to Gear", group = "Autonomous")
public class TankDriveToGear extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaVelocityVortexGetter vuforiaGetter = new VuforiaVelocityVortexGetter();
        VuforiaTrackables patterns = vuforiaGetter.getVuforia(1);
        DcMotor leftFront = hardwareMap.dcMotor.get(UniversalConstants.leftFrontDrive);
        DcMotor leftBack = hardwareMap.dcMotor.get(UniversalConstants.leftBackDrive);
        DcMotor rightFront = hardwareMap.dcMotor.get(UniversalConstants.rightFrontDrive);
        DcMotor rightBack = hardwareMap.dcMotor.get(UniversalConstants.rightBackDrive);

        SixWheelDrive drive = new SixWheelDrive(TankDriveToGear.this, leftFront, leftBack, rightFront, rightBack);

        waitForStart();

        patterns.activate();

        VuforiaVelocityVortexGetter.Pattern pattern = VuforiaVelocityVortexGetter.Pattern.Gears;
        while (opModeIsActive() && !vuforiaGetter.getOffset(patterns, pattern).foundValues) {
            drive.setPowers(.55);
            telemetry.addData("Left", "%.2f", drive.leftBack.getPower());
            telemetry.addData("Right", "%.2f", drive.rightBack.getPower());
        }
        drive.driveToTargetFromSeen(VuforiaVelocityVortexGetter.Pattern.Gears, patterns, vuforiaGetter);
        patterns.deactivate();
    }
}
*/