package eclipse_sample.RobotModules;

public class UniConstTwo {
    public static final int ticPerRev = 1120;
    public static double rotsPerRevBase = 3.33333;
    public static final double AngleperPos = 1.0 / 180.0;

    public static final double stowedOne = 0.71;
    public static final double stowedTwo = 0.41;
    public static final double stowedThree = 0.38;
    public static final double stowedFour = 0.39;

    public static final double maxOne = 0;
    public static final double maxTwo = 1;
    public static final double maxThree = .96;
    public static final double maxFour = 1;
    public static final double maxArmSpeed = 0.2; //inches per sec

    public static final double ticksPerDeg = .71 / 180.0;

    public static final double segmentOne = 8; // Arm segment 1+2 length
    public static final double segmentTwo = 8; // Arm segment 3+4 length
    public static final double armLength = 16; // Total Arm Length

    /*public static final String joint1 = "J1";
    public static final String joint2 = "J2";
    public static final String joint3 = "J3";
    public static final String joint4 = "J4";*/
    public static final String rightDriveOne = "RD1";
    public static final String rightDriveTwo = "RD2";
    public static final String leftDriveOne = "LD1";
    public static final String leftDriveTwo = "LD2";
    public static final String liftMotor = "liftMotor";
    public static final String baseJoint1 = "Base1";
    public static final String baseJoint2 = "Base2";
    public static final String armJoint2 = "Joint2";
    public static final String armJoint3 = "Joint3";
    public static final String armJoint4 = "Joint4";
    public static final String joint4Pot = "POT4";
    public static final String liftServo = "liftServo";

    public static final String colorSensor1 = "color1";
    public static final String colorSensor2 = "color2";
    public static final String colorSensor3 = "color3";

    //public static final String intake = "intake";

    public static final float joystickDeadzone = .1f;
    public static double maxDriveSpeed = .75;

    public static double angleToTicks(double angle) {
        return angle / (1 / ticksPerDeg);
    }

    public static double jointOneAngle(double x, double y) {
        return Math.toDegrees(Math.atan(y / x) + Math.acos(((-1 * Math.pow(UniConstTwo.segmentTwo, 2)) + Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(UniConstTwo.segmentOne, 2)) / (2 * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * UniConstTwo.segmentOne)));
    }

    public static double jointThreeAngle(double x, double y) {
        return Math.toDegrees(Math.acos((Math.pow(UniConstTwo.segmentTwo, 2) + Math.pow(UniConstTwo.segmentOne, 2) - (Math.pow(x, 2) + Math.pow(y, 2))) / (2 * UniConstTwo.segmentOne * UniConstTwo.segmentTwo)));
    }


    public enum SensorColor {
        Yellow, White
    }

    public enum Direction {
        Left, Middle, Right
    }


    public static final int BLUETHRESHOLD = 130;

    public static final String IMU = "imu";
    public static final double VoltsToAngle = 81.9423368741;
    public static final String dispenser = "DIS";



    public static final String vuforiaLicenceKey = "AUOJsRT/////AAABmYi3VkMsIEfwqQWzYHy+azUX1Gn6Y7BJYd/mmPwXlF2Npp3vKk34hM9KN+FbPVSrgsFgx3aXg6kHf0bPV6VtOlo5DN+sgaFKofKlskY5KtWfkw16qtNo54ZUMMMhJWMD6xJ9zmkmRz1WE0SWFn5W6VnqRBY0HDAmd7W6ySjG7b5EyUT3utNNd7bK/0tTb7QIlVl/o8ItGBnN7vKIUhVMklz1n1T4PNpq7wzTlVgnyQ1DlpAgc5ZBKgEng2ib+kTQ/vGKdO466yWgHyzJji6zOPwhxoTyuzQy4xpezLlb9ML9A1xFgKO+ZaLwn1Ul280CoJTB23GP7lemvNFpGWvMU024QZDauBLi/Z56JnDHWitu";
    public static final String distanceSensorFront = "sensor_range1";
    public  static  final String distanceSensorSide = "sensor_range2";

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";


    public static final String leftIntake = "LI";
    public static final String rightIntake = "RI";
    public static final int liftExtended = 1120;
    public static final double seg1 = 29.54;
    public static final double seg2 = 25.36;

    public static int nanoPerMili = 1000000;
}