package eclipse_sample.Archives.RobotModules;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

import java.util.Arrays;

public class MecanumDriveTrain {
    private Telemetry telemetry;
    private LinearOpMode linearOpMode;
    private BNO055IMU imu;

    public DcMotor leftFront, leftBack, rightFront, rightBack;
    public Servo colorDistanceServo;

    private VoltageSensor voltageSensor;

    public DistanceSensor leftSensorDistance;
    public DistanceSensor rightSensorDistance;
    public ColorSensor leftSensorColor;
    public ColorSensor rightSensorColor;
    public org.firstinspires.ftc.teamcode.Archives.RobotModules.SmartRangeMR forwardsWallDistanceSensor, leftFacingDistanceSensor, rightFacingDistanceSensor;

    enum DriveMode {
        NORMAL_SPEED, SLOW_MODE, RELIC_SLOW
    }

    private DriveMode driveMode;
    private boolean lastLeftBumper = false;
    private boolean lastRightBumper = false;

    public MecanumDriveTrain(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        new MecanumDriveTrain(l, zeroPowerBehavior, true);
    }

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    public MecanumDriveTrain(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior, boolean useSensors) {
        driveMode = DriveMode.NORMAL_SPEED;
        telemetry = l.telemetry;
        linearOpMode = l;
        HardwareMap hardwareMap = l.hardwareMap;

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        status("globals");

        leftFront = hardwareMap.dcMotor.get(UniversalConstants.leftFrontDrive);
        leftBack = hardwareMap.dcMotor.get(UniversalConstants.leftBackDrive);
        rightFront = hardwareMap.dcMotor.get(UniversalConstants.rightFrontDrive);
        rightBack = hardwareMap.dcMotor.get(UniversalConstants.rightBackDrive);

        status("Motors");

        leftBack.setZeroPowerBehavior(zeroPowerBehavior);
        leftFront.setZeroPowerBehavior(zeroPowerBehavior);
        rightBack.setZeroPowerBehavior(zeroPowerBehavior);
        rightFront.setZeroPowerBehavior(zeroPowerBehavior);

        status("Zero Power Behavior");

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);

        status("Direction");


        if (useSensors) {
            leftSensorDistance = linearOpMode.hardwareMap.get(DistanceSensor.class, UniversalConstants.leftSensorDistanceServo);
            rightSensorDistance = linearOpMode.hardwareMap.get(DistanceSensor.class, UniversalConstants.rightSensorDistanceServo);
            leftSensorColor = linearOpMode.hardwareMap.get(ColorSensor.class, UniversalConstants.leftSensorDistanceServo);
            rightSensorColor = linearOpMode.hardwareMap.get(ColorSensor.class, UniversalConstants.rightSensorDistanceServo);
            leftSensorColor.enableLed(true);
            rightSensorColor.enableLed(true);

            colorDistanceServo = linearOpMode.hardwareMap.servo.get(UniversalConstants.colorDistanceAutonomousServo);

            colorDistanceServo.setPosition(UniversalConstants.colorDistanceServoStored);
            forwardsWallDistanceSensor = new SmartRangeMR(linearOpMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, UniversalConstants.forwardsWallSensor));
            leftFacingDistanceSensor = new SmartRangeMR(linearOpMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, UniversalConstants.leftWallSensor));
            rightFacingDistanceSensor = new SmartRangeMR(linearOpMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, UniversalConstants.rightWallSensor));

            status("Distance Arm");

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            status("IMU Parameters");

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
            status("Initialized IMU");
        }
        setAll(DcMotor.RunMode.RUN_USING_ENCODER);

        status("Motor Run-Modes");

    }


    public void updateAll() {
        updateByGamepad();
        updateTelemetry();
    }

    //used for Tele-Operated control
    public void updateByGamepad() {
        double forwardsPower = -linearOpMode.gamepad1.left_stick_y;
        double horizontalPower = linearOpMode.gamepad1.left_stick_x;
        double turnPower = linearOpMode.gamepad1.right_stick_x;

        if (linearOpMode.gamepad1.left_bumper && !lastLeftBumper) {
            if (driveMode == DriveMode.SLOW_MODE) {
                driveMode = DriveMode.NORMAL_SPEED;
            } else {
                driveMode = DriveMode.SLOW_MODE;
            }
        }

        if (linearOpMode.gamepad1.right_bumper && !lastRightBumper) {
            if (driveMode == DriveMode.RELIC_SLOW) {
                driveMode = DriveMode.NORMAL_SPEED;
            } else {
                driveMode = DriveMode.RELIC_SLOW;
            }
        }


        forwardsPower = driverScalePower(forwardsPower, linearOpMode.gamepad1.left_trigger);
        horizontalPower = driverScalePower(horizontalPower, linearOpMode.gamepad1.right_trigger);

        forwardsPower = passiveScalePower(forwardsPower, UniversalConstants.maxDriveSpeed);
        horizontalPower = passiveScalePower(horizontalPower, UniversalConstants.maxDriveSpeed);

        switch (driveMode) {
            case SLOW_MODE:
                forwardsPower *= .5;
                horizontalPower *= .5;
                turnPower *= .5;
                break;
            case RELIC_SLOW:
                forwardsPower *= .3;
                horizontalPower *= .3;
                turnPower *= .3;
        }

        translateBy(forwardsPower, horizontalPower, turnPower);
        lastLeftBumper = linearOpMode.gamepad1.left_bumper;
        lastRightBumper = linearOpMode.gamepad1.right_bumper;
    }

    public double passiveScalePower(double power, double maxDriveSpeed) {
        if (power == 0) {
            return 0;
        }
        double maxValueOfScaled = (Math.cbrt(1 - UniversalConstants.joystickDeadzone));

        if (power > 0) {
            return maxDriveSpeed * Math.cbrt(power - UniversalConstants.joystickDeadzone) /
                    maxValueOfScaled;
        } else {
            return maxDriveSpeed * Math.cbrt(power + UniversalConstants.joystickDeadzone) /
                    maxValueOfScaled;
        }

        // Information about other scaling functions can be found at:
        // https://www.desmos.com/calculator/dzfyleh6ot

        // This was inspired by Jaxon Brown's Calculator, which is below
        // https://www.desmos.com/calculator/zhkohhj2wv
    }

    //used for Tele-Operated control
    public double driverScalePower(double power, double scaler) {
        if (scaler > .6) {
            return power / 5;
        }
        if (scaler > .3) {
            return power / 3;
        }
        return power;
    }

    public double getVoltage() {
        if (voltageSensor == null) {
            return 13;
        }
        return voltageSensor.getVoltage();
    }

    public double getTurnPower() {
        double max = .15, min = .08, avgVolts = 14.35;
        return max * (1 - getVoltage() / avgVolts) + min;
        // https://www.desmos.com/calculator/phdfnhkvgf
        // return .5 - (getVoltage() / 22);
        // tune this equation
    }

    public double angleThreshold = 1.5;

    public void turnExact(double angle) {
        turnExact(angle, getTurnPower());
    }

    public void turnExact(double angle, double power) {
        turnExact(power, angle, angleThreshold);
    }

    public void turnExact(double angle, double power, double threshold) {
        turnExactCoast(angle, power, threshold);
        park();
    }

    public void turnRelative(double angle) {
        turnRelative(angle, getTurnPower());
    }

    public void turnRelative(double angle, double power) {
        turnRelative(power, angle, angleThreshold);
    }

    public void turnRelative(double angle, double power, double threshold) {
        turnExact(getHeading() + angle, power, threshold);
    }

    public void alignToWithinOf(double angle) {
        alignToWithinOf(angle, getTurnPower());
    }

    public void alignToWithinOf(double angle, double power) {
        alignToWithinOf(angle, power, angleThreshold);
    }

    public void alignToWithinOf(double angle, double power, double threshold) {
        turnExact(angle, power, threshold);
        idle();
        turnExact(angle, power, threshold);
        idle();
        turnExact(angle, power, threshold);
    }

    public void alignToWithinVuforia(VuforiaRelicRecoveryGetter vuforiaRelicRecoveryGetter, double angle) {
        alignToWithinVuforia(vuforiaRelicRecoveryGetter, angle, getTurnPower());
    }

    public void turnLeft(double power) {
        setAll(-power, power);
        updateTelemetry();
        telemetry.update();
    }

    public void turnRight(double power) {
        setAll(power, -power);
        updateTelemetry();
        telemetry.update();
    }

    public void alignToWithinVuforia(VuforiaRelicRecoveryGetter vuforiaRelicRecoveryGetter, double angle, double power) {
        int tries = 0;
        while (opModeIsActive() &&
                tries < 3 &&
                !vuforiaRelicRecoveryGetter.getOffset().foundValues) {
            tries++;
            while (opModeIsActive() &&
                    !vuforiaRelicRecoveryGetter.getOffset().foundValues &&
                    getHeading() > angle) {
                turnLeft(power);
                linearOpMode.telemetry.addLine("Test1");
                linearOpMode.telemetry.update();
            }
            while (opModeIsActive() &&
                    !vuforiaRelicRecoveryGetter.getOffset().foundValues &&
                    getHeading() < angle) {
                turnRight(power);
                linearOpMode.telemetry.addLine("Test2");
                linearOpMode.telemetry.update();
            }
            linearOpMode.telemetry.addLine("Test3");
            linearOpMode.telemetry.update();

        }
    }

    public void turnExactCoast(double angle) {
        turnExactCoast(angle, getTurnPower());
    }

    public void turnExactCoast(double angle, double power) {
        turnExactCoast(angle, power, angleThreshold);
    }

    public void turnExactForcePower(double angle, double power, double threshold) {
        double turnPower = power;
        double distL = ((angle - (getHeading())) + 360) % 360;
        double distR = (((getHeading() - angle)) + 360) % 360;
        telemetry.addData("distanceL", distL);
        telemetry.addData("distanceR", distR);
        telemetry.update();
        while (opModeIsActive() && distL > threshold && distR > threshold) {
            distL = ((angle - (getHeading())) + 360) % 360;
            distR = (((getHeading() - angle)) + 360) % 360;
            if (distL < distR) {
                setAll(-turnPower, turnPower);
            } else {
                setAll(turnPower, -turnPower);
            }
            telemetry.addData("distanceL", distL);
            telemetry.addData("distanceR", distR);
            updateTelemetry();
            telemetry.update();
        }
    }

    public void turnExactCoast(double angle, double power, double threshold) {
        double turnPower;
        double distL = ((angle - (getHeading())) + 360) % 360;
        double distR = (((getHeading() - angle)) + 360) % 360;
        telemetry.addData("distanceL", distL);
        telemetry.addData("distanceR", distR);
        telemetry.update();
        while (opModeIsActive() && distL > threshold && distR > threshold) {
            distL = ((angle - (getHeading())) + 360) % 360;
            distR = (((getHeading() - angle)) + 360) % 360;
            turnPower = getTurnPower();
            if (distL < distR) {
                setAll(-turnPower, turnPower);
            } else {
                setAll(turnPower, -turnPower);
            }
            telemetry.addData("distanceL", distL);
            telemetry.addData("distanceR", distR);
            updateTelemetry();
            telemetry.update();
        }
        // Explanation of this math can be found at: https://www.desmos.com/calculator/tzijlpu8qc
    }

    public void turnRelativeCoast(double angle, double power) {
        turnRelativeCoast(angle, power, 1.5);
    }

    public void turnRelativeCoast(double angle, double power, double threshold) {
        turnExactCoast(getHeading() + angle, power, threshold);
    }

    public void setLeft(double power) {
        leftFront.setPower(power);
        leftBack.setPower(power);
    }

    public void setLeft(DcMotor.RunMode mode) {
        leftFront.setMode(mode);
        leftBack.setMode(mode);
    }

    public void setRight(double power) {
        rightBack.setPower(power);
        rightFront.setPower(power);
    }

    public void setRight(DcMotor.RunMode mode) {
        rightBack.setMode(mode);
        rightFront.setMode(mode);
    }

    public void setAll(double power) {
        setLeft(power);
        setRight(power);
    }

    public void setAll(DcMotor.RunMode mode) {
        setLeft(mode);
        setRight(mode);
    }

    public void setAll(double powerL, double powerR) {
        setLeft(powerL);
        setRight(powerR);
    }

    public void resetDriveEncoders() {
        DcMotor.RunMode mode = leftFront.getMode();
        setLeft(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRight(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while ((leftBack.getCurrentPosition() != 0 || rightBack.getCurrentPosition() != 0 || leftFront.getCurrentPosition() != 0 || rightFront.getCurrentPosition() != 0) && opModeIsActive()) {
            idle();
            updateTelemetry();
            telemetry.update();
        }
        setLeft(mode);
        setRight(mode);
    }

    public void setRelativeTargetPos(DcMotor m, int targetPos) {
        m.setTargetPosition(m.getCurrentPosition() + targetPos);
    }

    public void setRelativeTargetPos(int targetPos) {
        setRelativeTargetPos(leftFront, targetPos);
        setRelativeTargetPos(rightFront, targetPos);
        setRelativeTargetPos(leftBack, targetPos);
        setRelativeTargetPos(rightBack, targetPos);
    }

    public boolean aMotorIsBusy() {
        return leftFront.isBusy() || rightFront.isBusy() || leftBack.isBusy() || rightFront.isBusy();
    }

    public void moveToMillimeters(int distance, double power) {
        moveToInches((int) (distance / UniversalConstants.millimetersPerInch), power);
    }

    public double getOverallPosition() {
        return (getPositionFrontWheels() + getPositionBackWheels()) / 2;
    }

    public double getPositionFrontWheels() {
        return (leftFront.getCurrentPosition() + rightFront.getCurrentPosition()) / 2;
    }

    public double getPositionBackWheels() {
        return (leftBack.getCurrentPosition() + rightBack.getCurrentPosition()) / 2;
    }

    public void moveToInchesCoast(double distance, double power) {
        double timeout = linearOpMode.getRuntime() + Math.abs(distance) / 4;
        power = Math.abs(power);
        double target = getOverallPosition() + distance * UniversalConstants.ticksPerInch;
        double accuracyToTheInch = 1; //1 inch
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && Math.abs(target - getOverallPosition()) > (UniversalConstants.ticksPerInch * accuracyToTheInch)) {
            if (target > getOverallPosition()) {
                setAll(power);
            } else {
                setAll(-power);
            }
            linearOpMode.idle();
            updateTelemetry();
            telemetry.update();
        }
    }

    public void moveToInches(double distance, double power) {
        double timeout = linearOpMode.getRuntime() + ((.25 / power) * Math.abs(distance) / 4);
        power = Math.abs(power);
        double target = getOverallPosition() + distance * UniversalConstants.ticksPerInch;
        double accuracyToTheInch = .75;
        double accuracyInTicks = UniversalConstants.ticksPerInch * accuracyToTheInch;
        double error = Math.abs(target - getOverallPosition());
        while (opModeIsActive() && (linearOpMode.getRuntime() < timeout) && (error > accuracyInTicks)) {
            error = Math.abs(target - getOverallPosition());
            if (target > getOverallPosition()) {
                setAll(power);
            } else {
                setAll(-power);
            }
            linearOpMode.idle();
            updateTelemetry();
            telemetry.update();
        }
        park();
    }

    public void moveToPositionInches(double distance, double power) {
        setAll(DcMotor.RunMode.RUN_TO_POSITION);
        double timeout = linearOpMode.getRuntime() + ((.25 / power) * Math.abs(distance) / 2);
        power = Math.abs(power);
        double ticksDistance = distance * UniversalConstants.ticksPerInch;
        leftFront.setTargetPosition((int) (leftFront.getCurrentPosition() + ticksDistance));
        leftBack.setTargetPosition((int) (leftBack.getCurrentPosition() + ticksDistance));
        rightFront.setTargetPosition((int) (rightFront.getCurrentPosition() + ticksDistance));
        rightBack.setTargetPosition((int) (rightBack.getCurrentPosition() + ticksDistance));
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && aMotorIsBusy()) {
            setAll(power);
            updateTelemetry();
            telemetry.update();
        }
        setAll(DcMotor.RunMode.RUN_USING_ENCODER);
        park();
    }

    public void strafeToPositionInches(double distance, double power) {
        setAll(DcMotor.RunMode.RUN_TO_POSITION);
        distance *= 8 / 10;
        double timeout = linearOpMode.getRuntime() + ((.25 / Math.abs(power)) * Math.abs(distance) / 4);
        double sign = 1;
        if (power > 0) {
            sign = -1;
        }
        power = Math.abs(power);
        double ticksDistance = distance * UniversalConstants.ticksPerInch;
        leftFront.setTargetPosition((int) (leftFront.getCurrentPosition() + sign * ticksDistance));
        leftBack.setTargetPosition((int) (leftBack.getCurrentPosition() - sign * ticksDistance));
        rightFront.setTargetPosition((int) (rightFront.getCurrentPosition() - sign * ticksDistance));
        rightBack.setTargetPosition((int) (rightBack.getCurrentPosition() + sign * ticksDistance));
        while (aMotorIsBusy() && linearOpMode.getRuntime() < timeout && opModeIsActive()) {
            translateBy(0, power, 0);
            updateTelemetry();
            telemetry.update();
        }
        setAll(DcMotor.RunMode.RUN_USING_ENCODER);
        park();
    }

    public void moveToInchesSpeedUpSlowDown(double distance, double power) {
        double timeout = linearOpMode.getRuntime() + (.5 / power) * Math.abs(distance) / 4;
        double maxPower = Math.abs(power);
        double target = getOverallPosition() + distance * UniversalConstants.ticksPerInch;
        double accuracyToTheInch = .25;
        double accuracyInTicks = UniversalConstants.ticksPerInch * accuracyToTheInch;
        double error = Math.abs(target - getOverallPosition());
        double startError = error;
        double instantPower;
        double percent;
        double percentToRamp = .25;
        double slowestPower = .05;
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && error > accuracyInTicks) {
            error = Math.abs(target - getOverallPosition());
            percent = (startError - error) / startError;
            if (percent < percentToRamp) {
                instantPower = slowestPower + ((maxPower - slowestPower) / percentToRamp) * (percent);
                //f1(x) on desmos
                /*for the first 1/4 of the duration of it's run, the robot will move according to the
                    f1 graph on the desmos file.  This is when the graph is gradually increasing.
                */
            } else if (percent > (1 - percentToRamp)) {
                instantPower = slowestPower + ((maxPower - slowestPower) / percentToRamp) * (1 - percent);
                //f3(x) on desmos
                //This is the last quarter of the graph, the robot will move at a gradually decreasing speed
            } else {
                instantPower = maxPower;
                //f2(x) on desmos
                //this is the middle half of the graph when the robot moves at a constant speed
                //This is the fastest that the robot travels in the entire run
            }

            //A model for the math for this scaling algorithm can be found at https://www.desmos.com/calculator/lsjhykndca

            if (target > getOverallPosition()) {
                setAll(Math.abs(instantPower));
            } else {
                setAll(-Math.abs(instantPower));
            }
            linearOpMode.idle();
            updateTelemetry();
            telemetry.update();
        }
        park();
    }

    public void translateBy(double y, double x, double c) {
        double leftFrontVal = y + x + c;
        double rightFrontVal = y - x - c;
        double leftBackVal = y - x + c;
        double rightBackVal = y + x - c;

        //Move range to between 0 and +1, if not already
        double[] wheelPowers = {Math.abs(rightFrontVal), Math.abs(leftFrontVal), Math.abs(leftBackVal), Math.abs(rightBackVal)};
        Arrays.sort(wheelPowers);
        if (wheelPowers[3] > 1) {
            leftFrontVal /= wheelPowers[3];
            rightFrontVal /= wheelPowers[3];
            leftBackVal /= wheelPowers[3];
            rightBackVal /= wheelPowers[3];
        }

        leftFront.setPower(Range.clip(leftFrontVal, -1, 1));
        rightFront.setPower(Range.clip(rightFrontVal, -1, 1));
        leftBack.setPower(Range.clip(leftBackVal, -1, 1));
        rightBack.setPower(Range.clip(rightBackVal, -1, 1));
        // The math behind this code can be found at
        // https://www.desmos.com/calculator/wtj2vanrce
        // If statements are handled using piecewise functions.
    }

    public void fieldOriented(double y, double x, double c) {
        fieldOriented(y, x, c, getHeading());
    }

    public void fieldOriented(double y, double x, double c, double gyroheading) {
        double cosA = Math.cos(Math.toRadians(gyroheading));
        double sinA = Math.sin(Math.toRadians(gyroheading));
        double xOut = x * cosA - y * sinA;
        double yOut = x * sinA + y * cosA;
        translateBy(yOut, xOut, c);
        // The math behind this code can be found at
        // https://www.desmos.com/calculator/3qazr1a5wm
        // If statements are handled using piecewise functions.
    }


    public void balanceOnStone() {
        while (opModeIsActive() && Math.abs(getPitch()) < 5 && Math.abs(getRoll()) < 5) {
            translateBy(getPitch() / 5, getRoll() / 5, 0);
            updateTelemetry();
            telemetry.update();
        }
        park();
    }

    public void moveByAngle(double power, double theta) {
        moveByAngle(power, theta, getHeading());
    }

    public void moveToAngle(double y, double x, double c, double gyroheading) {
        while (opModeIsActive() && Math.abs(gyroheading - getHeading()) < 2.5) {
            translateBy(y, x, c);
            updateTelemetry();
            telemetry.update();
        }
        park();
    }

    public void moveByAngle(double power, double theta, double angle) {
        double y = power * Math.sin(Math.toRadians(theta));
        double x = power * Math.cos(Math.toRadians(theta));
        translateBy(y, x, angle);
    }

    @Deprecated
    public double getStrafeValue() {
        return imu.getPosition().y;
    }

    @Deprecated
    public double getForwardsValue() {
        return imu.getPosition().z;
    }

    @Deprecated
    public void moveToOffset(double strafingGoal, double forwardsGoal) {
        //strafingGoal and forwardsGoal are in millimeters
        double strafingStart = getStrafeValue();
        double forwardsStart = getForwardsValue();
        double forwardsOffset;
        double strafingOffset;
        double strafingRemaining;
        double forwardsRemaining;
        double percentDone;
        double totalLength = Math.sqrt(forwardsGoal * forwardsGoal + strafingGoal * strafingGoal);
        do {
            strafingOffset = strafingStart + getStrafeValue();
            forwardsOffset = forwardsStart + getForwardsValue();
            strafingRemaining = strafingGoal - strafingOffset;
            forwardsRemaining = forwardsGoal - forwardsOffset;
            if (forwardsRemaining > strafingRemaining) {
                strafingRemaining = strafingRemaining / forwardsRemaining;
                forwardsRemaining = 1;
            } else {
                forwardsRemaining = forwardsRemaining / strafingRemaining;
                strafingRemaining = 1;
            }
            percentDone = Math.sqrt(forwardsOffset * forwardsOffset + strafingOffset * strafingOffset) / totalLength;
            translateBy(forwardsRemaining * ((1 - percentDone) + .15), strafingRemaining * ((1 - percentDone) + .15), 0);
            updateTelemetry();
            telemetry.update();
        }
        while (opModeIsActive() && (Math.abs(strafingOffset - strafingGoal) > 5 || Math.abs(forwardsOffset - forwardsGoal) > 5));
        park();
    }


    private boolean opModeIsActive() {
        return linearOpMode.opModeIsActive();
    }

    public double getHeading() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public double getPitch() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.secondAngle;
    }

    public double getRoll() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.thirdAngle;
    }

    private void idle() {
        linearOpMode.idle();
    }

    public void updateTelemetry() {
        telemetry.addData("Drive Mode", driveMode);
    }

    public void park() {
        setAll(0);
    }


    public void slowGyroTurn(double minSpeed, double maxSpeed, double angle) {
        // keep looping while we are still active, and not on heading.
        double kP = .007;
        double HEADING_THRESHOLD = 1;
        if (Math.abs(getError(angle)) < HEADING_THRESHOLD) {
            return;
        }
        double timeout = linearOpMode.getRuntime() + 2.5;
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && !slowOnHeading(minSpeed, maxSpeed, angle, kP, HEADING_THRESHOLD)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    boolean slowOnHeading(double minSpeed, double maxSpeed, double angle, double kP, double HEADING_THRESHOLD) {
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;
        double PosOrNeg;
        double error = getError(angle);
        // determine turn power based on +/- error
        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0;
            rightSpeed = 0;
            onTarget = true;
        } else {
            // This is the main part of the Proportional GyroTurn.  This takes a set power variable,
            // and adds the absolute value of error*kP.
            // This allows for the robot to turn faster when farther
            // away from the target, and turn slower when closer to the target.  This allows for quicker, as well
            // as more accurate turning when using the GyroSensor
            PosOrNeg = Range.clip((int) -error, -1, 1);
            steer = getSteer(error, kP);
            leftSpeed = Range.clip(minSpeed + Math.abs(error * kP), minSpeed, maxSpeed) * PosOrNeg;
            rightSpeed = -leftSpeed;
        }

        // Set motor speeds.
        setAll(leftSpeed, rightSpeed);
        // Display debug info in telemetry.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Left, Right", "%5.2f, %5.2f", leftSpeed, rightSpeed);
        return onTarget;
    }

    public void gyroTurn(double speed, double angle) {
        // keep looping while we are still active, and not on heading.
        double kP = .007;
        double HEADING_THRESHOLD = 1;
        if (Math.abs(getError(angle)) < HEADING_THRESHOLD) {
            return;
        }
        double timeout = linearOpMode.getRuntime() + 2.5;
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && !onHeading(speed, angle, kP, HEADING_THRESHOLD)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    boolean onHeading(double speed, double angle, double kP, double HEADING_THRESHOLD) {
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;
        double PosOrNeg;
        double error = getError(angle);
        // determine turn power based on +/- error
        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0;
            rightSpeed = 0;
            onTarget = true;
        } else {
            // This is the main part of the Proportional GyroTurn.  This takes a set power variable,
            // and adds the absolute value of error*kP.
            // This allows for the robot to turn faster when farther
            // away from the target, and turn slower when closer to the target.  This allows for quicker, as well
            // as more accurate turning when using the GyroSensor
            PosOrNeg = Range.clip((int) -error, -1, 1);
            steer = getSteer(error, kP);
            leftSpeed = Range.clip(speed + Math.abs(error * kP), speed, .5) * PosOrNeg;
            rightSpeed = -leftSpeed;
        }

        // Set motor speeds.
        setAll(leftSpeed, rightSpeed);
        // Display debug info in telemetry.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Left, Right", "%5.2f, %5.2f", leftSpeed, rightSpeed);
        return onTarget;
    }


    public double getError(double targetAngle) {

        double robotError;
        // calculate error in -179 to +180 range  (
        robotError = targetAngle - getRawHeading();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getSteer(double error, double kP) {
        return Range.clip(error * (2 * kP), -1, 1);
    }

    public double getRawHeading() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    //negative power is left
    public void assistedStrafe(double horizontal, double targetHeading) {
        if (targetHeading == 180 && getRawHeading() < 0) {
            targetHeading = -180;
        }
        // c is positive -> turn to right
        // positive degrees are to the left
        // so, to turn left we want a negative offAngle.
        double offAngle = getRawHeading() - targetHeading;
        double kP = .005;
        double turnValue = offAngle * kP;
        translateBy(0, horizontal, Range.clip(turnValue, -.1, .1));
    }

    public void strafeToDistanceLeft(double power, double dist, DistanceUnit unit) {
        strafeToDistanceLeft(power, dist, getHeading(), unit);
    }

    public void strafeToDistanceLeft(double power, double dist, double targetHeading, DistanceUnit unit) {
        swingColorDistanceDown();
        //while the robot's position is not the certain amount of distance from the white tape
        // use the color distance sensor to find the distance
        double error = dist - leftSensorDistance.getDistance(unit);
        if (Double.isNaN(error)) {
            error = -100;
        }
        while (opModeIsActive() && Math.abs(error) > .75) {
            error = dist - leftSensorDistance.getDistance(unit);
            if (Double.isNaN(error)) {
                error = -100;
            }

            assistedStrafe(Math.signum(error) * power, targetHeading);
        }
        park();
    }


    public void strafeToDistanceLeftCoast(double power, double dist, double targetHeading, DistanceUnit unit) {
        swingColorDistanceDown();
        //while the robot's position is not the certain amount of distance from the white tape
        // use the color distance sensor to find the distance
        double error = dist - leftSensorDistance.getDistance(unit);
        double lastError = error;
        if (Double.isNaN(error)) {
            error = -100;
        }
        while (opModeIsActive() && Math.abs(error) > .75) {
            error = dist - leftSensorDistance.getDistance(unit);
            if (error / lastError < 0) {
                return;
            }
            if (Double.isNaN(error)) {
                error = -100;
            }
            assistedStrafe(Math.signum(error) * power, targetHeading);
            lastError = error;
            telemetry.addLine("Moving");
            telemetry.update();
        }
    }

    public void strafeToDistanceRight(double power, double dist, DistanceUnit unit) {
        strafeToDistanceRight(power, dist, getHeading(), unit);
    }

    public void strafeToDistanceRight(double power, double dist, double targetHeading, DistanceUnit unit) {
        swingColorDistanceDown();
        //while the robot's position is not the certain amount of distance from the white tape
        // use the color distance sensor to find the distance
        double error = rightSensorDistance.getDistance(unit) - dist;
        if (Double.isNaN(error)) {
            error = 100;
        }
        while (opModeIsActive() && Math.abs(error) > .75) {
            error = rightSensorDistance.getDistance(unit) - dist;
            if (Double.isNaN(error)) {
                error = 100;
            }
            assistedStrafe(Math.signum(error) * power, targetHeading);
            telemetry.addLine("Moving");
            telemetry.update();
        }
        park();
    }


    public void strafeToDistanceRightCoast(double power, double dist, double targetHeading, DistanceUnit unit) {
        swingColorDistanceDown();
        //while the robot's position is not the certain amount of distance from the white tape
        // use the color distance sensor to find the distance
        double error = dist - rightSensorDistance.getDistance(unit);
        double lastError = error;
        if (Double.isNaN(error)) {
            error = -100;
        }
        while (opModeIsActive() && Math.abs(error) > .75) {
            error = dist - rightSensorDistance.getDistance(unit);
            if (error / lastError < 0 || error > 0) {
                return;
            }
            if (Double.isNaN(error)) {
                error = -100;
            }
            assistedStrafe(-Math.signum(error) * power, targetHeading);
            lastError = error;
            telemetry.addLine("Moving");
            telemetry.update();
        }
    }

    public void swingColorDistanceUp() {
        colorDistanceServo.setPosition(UniversalConstants.colorDistanceServoUp);
    }

    public void swingColorDistanceDown() {
        colorDistanceServo.setPosition(UniversalConstants.ColorDistanceServoDown);
    }

    public void storeColorDistance() {
        colorDistanceServo.setPosition(UniversalConstants.colorDistanceServoStored);
    }

    /*
    Distance is positive: Go left
     */
    public void encoderStrafeToInches(double distance, double power) {
        encoderStrafeToInches(distance, power, getRawHeading());
    }

    public void encoderStrafeToInches(double distance, double power, double targetHeading) {
        power = Math.abs(power);
        double timeout = linearOpMode.getRuntime() + ((.25 / power) * Math.abs(distance) / 4);
        double target = getOverallPosition() + distance * UniversalConstants.ticksPerInch;
        double accuracyToTheInch = .75;
        double accuracyInTicks = UniversalConstants.ticksPerInch * accuracyToTheInch;
        double error = Math.abs(target - getOverallPosition());
        double lastError = error;
        while (opModeIsActive() && (linearOpMode.getRuntime() < timeout) && (error > accuracyInTicks)) {
            error = Math.abs(target - getOverallPosition());
            if (lastError / error < 0) {
                return;
            }
            if (target > getOverallPosition()) {
                assistedStrafe(-power, targetHeading);
            } else {
                assistedStrafe(power, targetHeading);
            }
            lastError = error;
            linearOpMode.idle();
            updateTelemetry();
            telemetry.update();
        }
    }

    public void turnWithOneSideOnly(double angle, double angleThreshold, double left, double right) {
        double error = angle - getRawHeading();
        double lastError = error;
        while (opModeIsActive() && Math.abs(error) > angleThreshold) {
            error = angle - getRawHeading();
            if (error / lastError < 0) {
                park();
                return;
            }
            setAll(left, right);
            lastError = error;
        }
        park();
    }

    public void moveToSeesDistance(double power) {
        while (Double.isNaN(leftSensorDistance.getDistance(DistanceUnit.CM)) && Double.isNaN(rightSensorDistance.getDistance(DistanceUnit.CM))) {
            translateBy(power, 0, 0);
        }
        park();
    }

    public void moveToNotSeesDistance(double power) {
        while (!(Double.isNaN(leftSensorDistance.getDistance(DistanceUnit.CM)) && Double.isNaN(rightSensorDistance.getDistance(DistanceUnit.CM)))) {
            translateBy(power, 0, 0);
        }
        park();
    }

    public void autoWallDistanceSensor(double distance, double power, DistanceUnit unit) {
        autoWallDistanceSensor(distance, power, unit, .5);
    }

    public void autoWallDistanceSensor(double distance, double power, DistanceUnit unit, double allowedError) {
        power = Math.abs(power);
        double error = forwardsWallDistanceSensor.getDistance(unit) - distance;
        // double lastError = error;
        if (Double.isNaN(error)) {
            error = 100;
        }
        while (opModeIsActive() && Math.abs(error) > allowedError) {
            error = forwardsWallDistanceSensor.getDistance(unit) - distance;
            //if (error / lastError < 0) {
            //    return;
            //}
            if (Double.isNaN(error)) {
                error = 100;
            }
            //lastError = error;
            translateBy(Math.signum(error) * power, 0, 0);
        }
    }

    public void autoLeftDistanceSensor(double distance, double power, double targetHeading, DistanceUnit unit) {
        autoLeftDistanceSensor(distance, power, targetHeading, unit, .5);
    }

    public void autoLeftDistanceSensor(double distance, double power,
                                       double targetHeading, DistanceUnit unit, double allowedError) {
        power = Math.abs(power);
        double error = distance - forwardsWallDistanceSensor.getDistance(unit);
        // double lastError = error;
        if (Double.isNaN(error)) {
            error = 100;
        }
        while (opModeIsActive() && Math.abs(error) > allowedError) {
            error = distance - leftFacingDistanceSensor.getDistance(unit);
            if (Double.isNaN(error)) {
                error = 100;
            }
            // negative error -> go left
            assistedStrafe(Math.signum(error) * power, targetHeading);
        }
    }

    public void autoRightDistanceSensor(double distance, double power, double targetHeading, DistanceUnit unit) {
        autoRightDistanceSensor(distance, power, targetHeading, unit, .5);
    }

    public void autoRightDistanceSensor(double distance, double power, double targetHeading, DistanceUnit unit, double allowedError) {
        power = Math.abs(power);
        double error = rightFacingDistanceSensor.getDistance(unit) - distance;
        // double lastError = error;
        if (Double.isNaN(error)) {
            error = 100;
        }
        while (opModeIsActive() && Math.abs(error) > allowedError) {
            error = rightFacingDistanceSensor.getDistance(unit) - distance;
            if (Double.isNaN(error)) {
                error = 100;
            }
            // negative error -> go left
            assistedStrafe(Math.signum(error) * power, targetHeading);
        }
    }

}