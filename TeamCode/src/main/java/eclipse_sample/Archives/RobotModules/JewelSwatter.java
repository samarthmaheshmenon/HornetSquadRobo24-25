package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

public class JewelSwatter {
    public Servo elbowServo, wristServo;
    Controller lastGamepad1;
    ColorSensor colorSensor = null;
    public DistanceSensor sensorDistance = null;
    LinearOpMode linearOpMode;
    boolean colorSensorLed = false;
    double startRed;
    double startBlue;

    public JewelSwatter(LinearOpMode l, boolean shouldBeStored) {
        linearOpMode = l;
        lastGamepad1 = new Controller(l.gamepad1);
        elbowServo = linearOpMode.hardwareMap.servo.get(UniversalConstants.jewelGimbleElbow);
        wristServo = linearOpMode.hardwareMap.servo.get(UniversalConstants.jewelGimbleWrist);
        if (shouldBeStored) {
            wristServo.setPosition(UniversalConstants.jewelWristStored);
            elbowServo.setPosition(UniversalConstants.jewelElbowStored);
        }
        colorSensor = linearOpMode.hardwareMap.colorSensor.get(UniversalConstants.jewelColorSensor);
        sensorDistance = linearOpMode.hardwareMap.get(DistanceSensor.class, "jcs");
        if (colorSensor != null) {
            setLed(true);
        }
    }

    public void zeroSwatter() {
        elbowServo.setPosition(UniversalConstants.jewelElbowStored);
        wristServo.setPosition(UniversalConstants.jewelWristStored);
    }

    public void updateAll() {
        return;
    }

    public void updateTelemetry() {
        linearOpMode.telemetry.addData("Swatter Angle", wristServo.getPosition());
        linearOpMode.telemetry.addData("Gimble Angle", elbowServo.getPosition());
        if (colorSensorLed) {
            linearOpMode.telemetry.addData("Red Start", startRed);
            linearOpMode.telemetry.addData("Blue Start", startBlue);
            linearOpMode.telemetry.addData("Red", colorSensor.red());
            linearOpMode.telemetry.addData("Blue", colorSensor.blue());
            linearOpMode.telemetry.addData("Green", colorSensor.green());
        }
    }

    public void setLed(boolean b) {
        colorSensorLed = b;
        if (colorSensor != null) {
            colorSensor.enableLed(b);
        }
    }

    public AutonomousUtil.AllianceColor getJewelColor(double startRed, double startBlue) {
        return seesRed(startRed, startBlue) ? AutonomousUtil.AllianceColor.Red : AutonomousUtil.AllianceColor.Blue;
    }

    public boolean seesRed(double startRed, double startBlue) {
        if (colorSensor == null) {
            return true;
        }
        double redLeft = (colorSensor.red() - startRed);
        double blueLeft = (colorSensor.blue() - startBlue);
        return redLeft > blueLeft;
    }

    public void removeJewelOfColor(AutonomousUtil.AllianceColor color) throws InterruptedException {
        removeJewel(color);
        waitForServoToMove(UniversalConstants.jewelServoSleepTimeShort);
        elbowServo.setPosition(UniversalConstants.jewelElbowStored);
    }

    public void removeJewel(AutonomousUtil.AllianceColor color) throws InterruptedException {
        elbowServo.setPosition(UniversalConstants.jewelElbowCenter);
        double red = colorSensor.red();
        double blue = colorSensor.blue();
        waitForServoToMove(UniversalConstants.jewelServoSleepTimeMedium);
        wristServo.setPosition(UniversalConstants.jewelWristLowered);
        waitForServoToMove(UniversalConstants.jewelServoSleepTimeMedium);
        if (getJewelColor(red, blue) == color) {
            elbowServo.setPosition(UniversalConstants.jewelElbowScoreLeft);
        } else {
            elbowServo.setPosition(UniversalConstants.jewelElbowScoreRight);
        }
        waitForServoToMove(UniversalConstants.jewelServoSleepTimeShort);
        wristServo.setPosition(UniversalConstants.jewelWristStored);
    }

    @Deprecated
    public void moveJewelToForwards(){
        elbowServo.setPosition(UniversalConstants.jewelElbowForwards);
        wristServo.setPosition(UniversalConstants.jewelWristForwards);
    }

    @Deprecated
    public void moveJewelForwardsAway(){
        elbowServo.setPosition(UniversalConstants.jewelElbowForwardsAway);
        wristServo.setPosition(UniversalConstants.jewelWristForwardsAway);
    }

    public void waitForServoToMove(long time) throws InterruptedException {
        Thread.sleep(time);
    }

}
