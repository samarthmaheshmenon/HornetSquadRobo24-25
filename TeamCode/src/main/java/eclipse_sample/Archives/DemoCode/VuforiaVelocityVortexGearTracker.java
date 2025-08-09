package eclipse_sample.Archives.DemoCode;/*package org.firstinspires.ftc.teamcode.Archives.DemoCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Vuforia - Gear", group = "Autonomous")
public class VuforiaVelocityVortexGearTracker extends LinearOpMode {
    public enum LeftOrRight {
        LEFT, RIGHT, CENTER
    }

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaVelocityVortexGetter v = new VuforiaVelocityVortexGetter();
        VuforiaTrackables beacons = v.getVuforia(1);

        //General Constants
        double MIN_POWER = .2;


        // Terms that change each loop
        double defaultPower;
        double powerL;
        double powerR;

        double distanceForwardsBackwards;
        double distanceLeftRight;
        double distanceUpDown;

        LeftOrRight sideOfPattern;
        boolean isGoingBackwards = false;

        waitForStart();

        beacons.activate();

        while (opModeIsActive()) {
            VuforiaVelocityVortexGetter.Pattern pattern = VuforiaVelocityVortexGetter.Pattern.Gears;
            VuforiaVelocityVortexGetter.DistanceOffsets distanceOffsets = v.getOffset(beacons, pattern);
            if (distanceOffsets.foundValues) {
                distanceForwardsBackwards = distanceOffsets.distance;
                distanceLeftRight = distanceOffsets.horizontal;
                distanceUpDown = distanceOffsets.vertical;
                telemetry.addData(pattern + " Forwards", "%.2f in", distanceForwardsBackwards / UniversalConstants.millimetersPerInch);
                if (distanceLeftRight > 0) {
                    sideOfPattern = LeftOrRight.LEFT;
                } else {
                    distanceLeftRight = -distanceLeftRight;
                    sideOfPattern = LeftOrRight.RIGHT;
                }
                telemetry.addData(pattern + (sideOfPattern == LeftOrRight.LEFT ? " Left" : " Right"),
                        "%.2f mm", distanceLeftRight);

                if (distanceForwardsBackwards < 50) {
                    defaultPower = -.5;
                    isGoingBackwards = true;
                } else {
                    defaultPower = distanceForwardsBackwards / (1200);
                    if (defaultPower < MIN_POWER) {
                        defaultPower = MIN_POWER;
                    }
                    isGoingBackwards = false;
                }

                if (sideOfPattern == LeftOrRight.LEFT) {
                    //Robot is on Left
                    powerL = defaultPower - (isGoingBackwards ? -VuforiaConstants.addToHigherSide : VuforiaConstants.addToHigherSide);
                    powerR = defaultPower + (isGoingBackwards ? -VuforiaConstants.subtractFromLowerSide : VuforiaConstants.subtractFromLowerSide);
                } else {
                    //Robot is on Right
                    powerL = defaultPower + (isGoingBackwards ? -VuforiaConstants.addToHigherSide : VuforiaConstants.addToHigherSide);
                    powerR = defaultPower - (isGoingBackwards ? -VuforiaConstants.subtractFromLowerSide : VuforiaConstants.subtractFromLowerSide);
                }

                telemetry.addData("Left", "%.2f", powerL);
                telemetry.addData("Right", "%.2f", powerR);

            } else {
                telemetry.addLine("Found No Objects!");
            }
            telemetry.update();
        }
        beacons.deactivate();
    }
}
*/