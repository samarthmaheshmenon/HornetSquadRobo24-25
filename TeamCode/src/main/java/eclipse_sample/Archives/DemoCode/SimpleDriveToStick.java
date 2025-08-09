package eclipse_sample.Archives.DemoCode;/*package org.firstinspires.ftc.teamcode.DemoCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.UniversalConstants;

@Disabled
@Autonomous(name = "Find the Stick!", group = "Autonomous")
public class SimpleDriveToStick extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaVelocityVortexGetter vuforiaGetter = new VuforiaVelocityVortexGetter();
        VuforiaTrackables patterns = vuforiaGetter.getVuforia(1);
        DcMotor leftFront = hardwareMap.dcMotor.get(UniversalConstants.leftFrontDrive);
        DcMotor leftBack = hardwareMap.dcMotor.get(UniversalConstants.leftBackDrive);
        DcMotor rightFront = hardwareMap.dcMotor.get(UniversalConstants.rightFrontDrive);
        DcMotor rightBack = hardwareMap.dcMotor.get(UniversalConstants.rightBackDrive);

        SixWheelDrive drive = new SixWheelDrive(SimpleDriveToStick.this, leftFront, leftBack, rightFront, rightBack);

        waitForStart();

        patterns.activate();
        VuforiaVelocityVortexGetter.Pattern pattern = VuforiaVelocityVortexGetter.Pattern.Gears;
        double distanceLeftRight, distanceUpDown, distanceForwardsBackwards, powerL, powerR, MIN_POWER = .25;
        UniversalConstants.LeftOrRight sideOfPattern;
        while (opModeIsActive()) {
            VuforiaVelocityVortexGetter.DistanceOffsets distanceOffsets = vuforiaGetter.getOffset(patterns, pattern);
            if (distanceOffsets.foundValues) {
                distanceForwardsBackwards = distanceOffsets.distance;
                distanceLeftRight = distanceOffsets.horizontal;
                distanceUpDown = distanceOffsets.vertical;
                telemetry.addData(pattern + " Forwards", "%.2f in", distanceForwardsBackwards / UniversalConstants.millimetersPerInch);
                if (distanceLeftRight > 0) {
                    sideOfPattern = UniversalConstants.LeftOrRight.LEFT;
                } else {
                    distanceLeftRight = -distanceLeftRight;
                    sideOfPattern = UniversalConstants.LeftOrRight.RIGHT;
                }
                telemetry.addData(pattern + (sideOfPattern == UniversalConstants.LeftOrRight.LEFT ? " Left" : " Right"),
                        "%.2f mm", distanceLeftRight);

                //BACKWARDS
                if (distanceForwardsBackwards > 200) {
                    if (sideOfPattern == UniversalConstants.LeftOrRight.LEFT) {
                        powerL = (MIN_POWER - 5 * VuforiaConstants.addToHigherSide);
                        powerR = (MIN_POWER + 5 * VuforiaConstants.subtractFromLowerSide);
                    } else {
                        powerL = (MIN_POWER + 5 * VuforiaConstants.subtractFromLowerSide);
                        powerR = (MIN_POWER - 5 * VuforiaConstants.addToHigherSide);
                    }
                } else if (distanceForwardsBackwards < 150) {
                    if (sideOfPattern == UniversalConstants.LeftOrRight.LEFT) {
                        powerL = -(MIN_POWER + 5 * VuforiaConstants.addToHigherSide);
                        powerR = -(MIN_POWER - 5 * VuforiaConstants.subtractFromLowerSide);
                    } else {
                        powerL = -(MIN_POWER - 5 * VuforiaConstants.subtractFromLowerSide);
                        powerR = -(MIN_POWER + 5 * VuforiaConstants.addToHigherSide);
                    }
                } else {
                    powerL = powerR = 0;
                }
                drive.setLeft(powerL);
                drive.setRight(powerR);
                telemetry.addData("Left", "%.2f", powerL);
                telemetry.addData("Right", "%.2f", powerR);
            } else {
                drive.setPowers(0);
            }
        }
        patterns.deactivate();
    }
}
*/