package eclipse_sample.Archives;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class UniversalConstants {
    public static final double robotHorizontalOffset = 0;
    public static final double robotVerticalOffset = 0;
    public static final double robotFrontOffset = 0;
    // These are in millimeters.

    public static final double distanceBlueODS = 13;
    public static final double distanceRedODS = 8.25;

    public static final double millimetersPerInch = 25.4;

    public static final float joystickDeadzone = .1f;
    public static double maxDriveSpeed = 1;

    public static final String leftFrontDrive = "lf";
    public static final String leftBackDrive = "lb";
    public static final String rightFrontDrive = "rf";
    public static final String rightBackDrive = "rb";

    //how many ticks does the motor take to spin one rotation?
    public static int ticksPerMotorRotation = 1120;
    // This number can be found at https://www.andymark.com/NeveRest-p/am-neverest.htm

    //how many times does the wheel spin per rotation of the motor?
    public static final double motorGear = 32;
    public static final double wheelGear = 15;

    //how many inches does one spin of the wheel take us?
    public static double wheelCircumference = 4 * Math.PI;
    //product page for our Mecanum Wheel: https://www.superdroidrobots.com/shop/item.aspx/4-inch-nexus-mecanum-wheels-ball-bearing-set-of-4/1352/

    static double ticksPerRotation = (ticksPerMotorRotation * wheelGear) / (motorGear);
    static double rotationsPerInch = 1 / wheelCircumference;
    public static double ticksPerInch = ticksPerRotation * rotationsPerInch;
    // (1120 * 15) / (32 * 4*pi)

    public static final String jewelGimbleElbow = "jge"; //earlier bottom
    public static final String jewelGimbleWrist = "jgw"; // earlier top
    public static final String jewelColorSensor = "jcs";
    public static final String forwardsWallSensor = "fws";
    public static final String leftWallSensor = "lws";
    public static final String rightWallSensor = "rws";

    public static final String leftSensorDistanceServo = "sdsl";
    public static final String rightSensorDistanceServo = "sdsr";

    public static final double jewelWristStored = .05;
    public static final double jewelWristLowered = .53; // .62 for robot 1;
    public static final double jewelWristForwards = .8;
    public static final double jewelWristForwardsAway = 1;


    public static final double jewelElbowStored = 1; // .3 for robot 1
    public static final double jewelElbowCenter = .35 + .28; // .25 for robot 1;
    public static final double jewelElbowScoreLeft = jewelElbowCenter - .12; // .17 for robot 1;
    public static final double jewelElbowScoreRight = jewelElbowCenter + .12; // .32 for robot 1;
    public static final double jewelElbowForwards = .2;
    public static final double jewelElbowForwardsAway = .5;

    public static final int jewelServoSleepTimeLong = 1000;
    public static final int jewelServoSleepTimeMedium = 600;
    public static final int jewelServoSleepTimeShort = 350;
    //milliseconds

    public static final String slamDunker = "sd";
    public static String dunkerServo = "ds";
    static double dunkerGearRatio = 20 / 10;
    // 3/2 represents the gear ratio
    static double fractionMoved = 1 / 3;
    // 1/3 represents the 1/3 rotation the mechanism must travel.
    static double dunkerRatio = 2 / 3;
    public static final int dunkGlyphsPosition = (int) (ticksPerMotorRotation * .6);
    // Dunking Position - .7 is a little more than 2/3, as 2/3 = .667.
    public static final double dunkGlyphsSpeed = .85;
    public static final double lowerGlyphsSpeeed = .45;

    public static final double stopperServoDown = 0;
    public static final double stopperServoEngaged = .45;

    public static final String intakeMotorLeft = "inL";
    public static final String intakeMotorRight = "inR";
    public static final String intakeFoldOutLeft = "inLH";
    public static final String intakeFoldOutRight = "inRH";

    public static final double foldoutHolderLeftStored = .2;
    public static final double foldoutHolderLeftOpen = 0;
    public static final double foldoutHolderRightStored = .12;
    public static final double foldoutHolderRightOpen = .25;

    public static final String relicSpoolMotor = "rm";
    public static final String relicElbow = "re";
    public static final String relicPincher = "rp";

    public static final double relicElbowUp = .50;
    public static final double relicElbowAwayFromWall = .37;
    public static final double relicElbowDown = 1;
    public static final double relicElbowStored = .11;
    public static final double relicPincherPinched = .38;
    public static final double relicPincherFullyOpen = .9;

    private static double relicSpoolDiameter = 2.5; //inches
    private static double relicSpoolCircumference = relicSpoolDiameter * Math.PI; //inches
    private static double lengthOfRelicString = 17.5; //inches
    private static double requiredSpoolRotations = lengthOfRelicString / relicSpoolCircumference;
    private static double relicChainRatio = 1;
    private static double relicMotorRotations = requiredSpoolRotations / relicChainRatio;
    private static double excess = 1.0;
    public static final int relicMotorLimit = (int) (excess * (ticksPerMotorRotation * relicMotorRotations));

    public static final String distanceSensorLeft = "dsl";
    public static final String distanceSensorRight = "dsr";

    public static final String colorDistanceAutonomousServo = "cds";
    public static final double colorDistanceServoStored = 0;
    public static final double colorDistanceServoUp = .4;
    public static final double ColorDistanceServoDown = .9;


    public static final String vuforiaLicenceKey = "AfVNPjT/////AAAAGWxt1A0qnE/0ubDxBQVByN5Rb1GNo+3vvrqiIVpsnNHDWKyEcVhuKt6W/wPMw/0/wJh0iMnrWM+HddaZeSV8uGaUacthmkOT/xVt/+A+hlgt+3rIkDKkAYfIOw/DCK/RNY5U1LWCFSPGdjt5w3BQEg3iOEWzuhyovpBn+UlS56UhH6q5wP9qz9PpabM1Q7IW9MoYUGKTsiiLCQbB7ICHckbAQQBU1WJQdD3fAGnOOM0Dh1yjtOBlU3+kFZQOxKXNiVr5xxAZm903atvnT179VaATgl8U2yN9IW6h9gZ8tghlubFyeLRSiMaOrv/0gYchtBHH51WBJEsy2Tv2rzt403b4QMuZE5zQTRiZPJuJn83L";
    private static VuforiaLocalizer.CameraDirection selfieCam = VuforiaLocalizer.CameraDirection.FRONT;
    //public static VuforiaLocalizer.CameraDirection frontCam = VuforiaLocalizer.CameraDirection.BACK;

    public static final VuforiaLocalizer.CameraDirection cameraDirection = selfieCam;
    //We like the front-facing "selfie" camera because it allows us to mount the phone such that we can also access the phone screen.

    public enum LeftOrRight {
        LEFT, RIGHT
    }

}
