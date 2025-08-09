package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;
import org.firstinspires.ftc.teamcode.R;

public class VuforiaRelicRecoveryGetter {

    VuforiaTrackables vuforiaTrackables;
    LinearOpMode linearOpMode;

    public VuforiaRelicRecoveryGetter(LinearOpMode l) {
        vuforiaTrackables = getVuforia();
        linearOpMode = l;
    }

    public VuforiaTrackables getVuforiaTrackables() {
        return vuforiaTrackables;
    }

    public void activateTrackables() {
        vuforiaTrackables.activate();
    }

    public void deactivateTrackables() {
        vuforiaTrackables.deactivate();
    }

    public VuforiaTrackables getVuforia() {
        return getVuforia(true);
    }

    public VuforiaTrackables getVuforia(boolean showCameraView) {

        VuforiaLocalizer.Parameters parameters;
        if (showCameraView) {
            parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
            // Makes the Vuforia view appear on the phone screen
            // Can remove the R.id.cameraMonitorViewId to save battery or whatever.
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        parameters.cameraDirection = UniversalConstants.cameraDirection;
        parameters.vuforiaLicenseKey = UniversalConstants.vuforiaLicenceKey;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.TEAPOT;
        // Can also use axes if you want to be lame.
        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);
        // We only need one vision target for this year.

        VuforiaTrackables trackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        trackables.setName("Vision Targets");
        return trackables;
    }

    public RelicRecoveryVuMark getPattern() {
        return getPattern(vuforiaTrackables);
    }

    public RelicRecoveryVuMark getPattern(VuforiaTrackables vuforiaTrackables) {
        VuforiaTrackable target = vuforiaTrackables.get(0);
        return RelicRecoveryVuMark.from(target);
    }

    public DistanceOffsets getOffset() {
        return getOffset(vuforiaTrackables);
    }

    public DistanceOffsets getOffset(VuforiaTrackables vuforiaTrackables) {
        VuforiaTrackable target = vuforiaTrackables.get(0);
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) target.getListener()).getPose();
        RelicRecoveryVuMark type = RelicRecoveryVuMark.from(target);
        if (pose != null) {
            double distanceLeftRight = pose.getTranslation().get(0) - UniversalConstants.robotHorizontalOffset;
            double distanceUpDown = pose.getTranslation().get(1) - UniversalConstants.robotVerticalOffset;
            double distanceForwardsBackwards = Math.abs(pose.getTranslation().get(2)) - UniversalConstants.robotFrontOffset;
            return new DistanceOffsets(distanceForwardsBackwards, distanceLeftRight, distanceUpDown, type);
        }
        return new DistanceOffsets();
        //Found nothing
    }

    public class DistanceOffsets {
        public double distance, horizontal, vertical;
        public boolean foundValues;
        public RelicRecoveryVuMark vuMarkType;

        DistanceOffsets(double forwardsBack, double leftRight, double upDown, RelicRecoveryVuMark type) {
            foundValues = true;
            distance = forwardsBack;
            horizontal = leftRight;
            vertical = upDown;
            vuMarkType = type;
        }

        DistanceOffsets() {
            this.foundValues = false;
            distance = horizontal = vertical = 0;
            vuMarkType = RelicRecoveryVuMark.UNKNOWN;
        }
    }

    public void updateTelemetry() {
        DistanceOffsets offsets = getOffset();
        if (offsets != null && offsets.foundValues) {
            linearOpMode.telemetry.addData("VuMark", offsets.vuMarkType);
            linearOpMode.telemetry.addData("Distance", offsets.distance);
            linearOpMode.telemetry.addData("Horizontal", offsets.horizontal);
        } else {
            linearOpMode.telemetry.addData("Vuforia", "Nothing seen");
        }
    }
}
