package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class ColerSensorControl {
    com.qualcomm.robotcore.hardware.ColorSensor colorSensorLeft;
    com.qualcomm.robotcore.hardware.ColorSensor colorSensorRight;
    LinearOpMode linearOpMode;

    public ColerSensorControl(LinearOpMode l) {
        linearOpMode = l;
        colorSensorLeft = linearOpMode.hardwareMap.colorSensor.get(UniConstTwo.colorSensor1);
        colorSensorRight = linearOpMode.hardwareMap.colorSensor.get(UniConstTwo.colorSensor2);

        if (colorSensorLeft != null && colorSensorRight != null) {
            colorSensorLeft.enableLed(true);
            colorSensorRight.enableLed(true);
        }
    }

    public UniConstTwo.Direction whereIsTheGoldMineral() {
        if (seesYellow(colorSensorLeft)) {
            return UniConstTwo.Direction.Left;
        }else if (seesYellow(colorSensorRight)) {
            return UniConstTwo.Direction.Middle;
        }else {
            return UniConstTwo.Direction.Right;
        }
    }

    public String findGoldString() {
        display();
        if (seesYellow(colorSensorLeft)) {
            return "Left";
        }else if (seesYellow(colorSensorRight)) {
            return "Middle";
        }else {
            return "Right";
        }
    }

    private boolean seesYellow(com.qualcomm.robotcore.hardware.ColorSensor colorSensor) {
        if((double)colorSensor.blue() / (double)colorSensor.alpha() < .25){
            return true;
        }
        return false;
    }

    public boolean seesColor(){
        int threshold = 500;
        if(colorSensorLeft.alpha() > threshold || colorSensorRight.alpha() > threshold){
            return true;
        }
        return false;
    }

    private UniConstTwo.SensorColor getLeft() {
        return (colorSensorLeft.blue() >= UniConstTwo.BLUETHRESHOLD) ? UniConstTwo.SensorColor.White : UniConstTwo.SensorColor.Yellow;
    }

    private UniConstTwo.SensorColor getRight() {
        return (colorSensorRight.blue() >= UniConstTwo.BLUETHRESHOLD) ? UniConstTwo.SensorColor.White : UniConstTwo.SensorColor.Yellow;
    }

    public void display() {
        linearOpMode.telemetry.addLine("LEFT Alpha: " + colorSensorLeft.alpha());
        linearOpMode.telemetry.addLine("LEFT Blue: " + colorSensorLeft.blue());
        linearOpMode.telemetry.addLine("LEFT Blue/Alpha: " + (double)colorSensorLeft.blue()/(double)colorSensorLeft.alpha());
        linearOpMode.telemetry.addLine("RIGHT Alpha: " + colorSensorRight.alpha());
        linearOpMode.telemetry.addLine("RIGHT Blue: " + colorSensorRight.blue());
        linearOpMode.telemetry.addLine("RIGHT Blue/Alpha: " + (double)colorSensorRight.blue()/(double)colorSensorRight.alpha());
        linearOpMode.telemetry.update();
    }

    /*public double leftDistance(){
        return distanceSensorLeft.getDistance(DistanceUnit.INCH);
    }

    public double rightDistance(){
        return distanceSensorRight.getDistance(DistanceUnit.INCH);
    }
    */
}
