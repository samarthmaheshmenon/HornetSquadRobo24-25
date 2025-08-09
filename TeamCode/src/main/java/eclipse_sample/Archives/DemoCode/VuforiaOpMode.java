package eclipse_sample.Archives.DemoCode;/*package org.firstinspires.ftc.teamcode.Archives.DemoCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Disabled
@Autonomous(name = "Vuforia General", group = "Autonomous")
public class VuforiaOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        org.firstinspires.ftc.teamcode.DemoCode.VuforiaVelocityVortexGetter v = new org.firstinspires.ftc.teamcode.DemoCode.VuforiaVelocityVortexGetter();
        VuforiaTrackables trackables = v.getVuforia();
        waitForStart();

        trackables.activate();
        while (opModeIsActive()) {
            boolean foundOne = false;
            for (VuforiaTrackable trackable : trackables) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getPose();
                if (pose != null) {
                    foundOne = true;
                    VectorF translation = pose.getTranslation();
                    double distanceForwardsBackwards = Math.abs(pose.getTranslation().get(2));
                    double distanceLeftRight = pose.getTranslation().get(0);
                    double distanceUpDown = pose.getTranslation().get(1);
                    // double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(2)));
                    telemetry.addData(trackable.getName() + " Forwards", distanceForwardsBackwards);
                    telemetry.addData(trackable.getName() + (distanceLeftRight > 0 ? " Left" : " Right"), distanceLeftRight);
                }
            }
            if (!foundOne) {
                telemetry.addData("Found No Objects", "Uh Oh!");
            }
            telemetry.update();
        }

    }
}
*/