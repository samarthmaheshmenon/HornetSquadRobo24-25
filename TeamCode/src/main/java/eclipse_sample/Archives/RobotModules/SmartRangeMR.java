package eclipse_sample.Archives.RobotModules;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SmartRangeMR {
    ModernRoboticsI2cRangeSensor modernRoboticsI2cRangeSensor;

    public SmartRangeMR(ModernRoboticsI2cRangeSensor m) {
        modernRoboticsI2cRangeSensor = m;
    }

    public double getDistance(DistanceUnit distanceUnit) {
        double distance = modernRoboticsI2cRangeSensor.getDistance(distanceUnit);
        while (Double.isNaN(distance)) {
            distance = modernRoboticsI2cRangeSensor.getDistance(distanceUnit);
        }
        return distance;
    }

}

