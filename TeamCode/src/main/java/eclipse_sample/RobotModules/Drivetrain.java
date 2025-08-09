package eclipse_sample.RobotModules;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Drivetrain {
    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    private DcMotorEx leftOne, rightOne;
    private BNO055IMU imu;
    private SensorDistance frontSensor, sideSensor;
    private PIDCoefficients pidCoefficients;
    private Boolean firstTurn = true;
    private double firstAngle = 0;
    private int headingCounter = 0;
    private double integral = 0;//Integral counter for PID turns.


    private DriveSpeed driveSpeed;

    enum DriveSpeed {
        FULLSPEED, HALFSPEED, QUARTERSPEED
    }

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    // drivetrain object constructor
    public Drivetrain(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        linearOpMode = l;
        driveSpeed = DriveSpeed.FULLSPEED;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = l.hardwareMap;

        frontSensor = new SensorDistance(linearOpMode, UniConstTwo.distanceSensorFront);
        sideSensor = new SensorDistance(linearOpMode, UniConstTwo.distanceSensorSide);

        status("Initialized Globals");

        leftOne = (DcMotorEx) hardwareMap.dcMotor.get(UniConstTwo.leftDriveOne);
        rightOne = (DcMotorEx) hardwareMap.dcMotor.get(UniConstTwo.rightDriveOne);

        status("Initialized Motors");

        leftOne.setZeroPowerBehavior(zeroPowerBehavior);
        rightOne.setZeroPowerBehavior(zeroPowerBehavior);

        status("Zero Power Behavior Set");

        leftOne.setDirection(DcMotorSimple.Direction.REVERSE);
        rightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        pidCoefficients = new PIDCoefficients(3, .1140625 * 2, 1.875);
        leftOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
        rightOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);

        status("Directions Set");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        //We don't know how this line affects code
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        // Don't fully know usage of this line
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        status("IMU Parameters Set");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        status("IMU Initialized");

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // methods set motor run modes
    public void setRunMode(DcMotor.RunMode runMode) {
        setLeftMode(runMode);
        setRightMode(runMode);
    }

    public void setLeftMode(DcMotor.RunMode runMode) {
        leftOne.setMode(runMode);
    }

    public void setRightMode(DcMotor.RunMode runMode) {
        rightOne.setMode(runMode);
    }


    // this method will set the max speed based off of the input enum
    public double setSpeed(DriveSpeed param) {
        double drivespeed;
        switch (param) {
            case FULLSPEED:
                drivespeed = UniConstTwo.maxDriveSpeed;
                break;
            case HALFSPEED:
                drivespeed = UniConstTwo.maxDriveSpeed / 2;
                break;
            case QUARTERSPEED:
                drivespeed = UniConstTwo.maxDriveSpeed / 4;
                break;
            default:
                drivespeed = UniConstTwo.maxDriveSpeed;
                break;

        }
        return drivespeed;
    }

    // takes the desired power and the set speed and returns a scaled power value
    public double ScaledPower(double power, double speed) {
        if (power == 0) {
            return 0;
        }
        double maxValueOfScaled = (Math.cbrt(1 - UniConstTwo.joystickDeadzone));

        if (power > 0) {
            return speed * Math.cbrt(power - UniConstTwo.joystickDeadzone) /
                    maxValueOfScaled;
        } else {
            return speed * Math.cbrt(power + UniConstTwo.joystickDeadzone) /
                    maxValueOfScaled;
        }
    }

    //These methods set the scaled power for teleOp to the motors
    public void setLeft(double power, double speed) {
        leftOne.setPower(ScaledPower(power, speed));
    }

    public void setRight(double power, double speed) {
        rightOne.setPower(ScaledPower(power, speed));
    }

    //  motor power method for teleop
    public void setAll(double powerL, double powerR, double speed) {
        setLeft(powerL, speed);
        setRight(powerR, speed);
    }

    //These are the power set methods that will be used for auto since no scaling is needed
    public void setRight(double power) {
        rightOne.setPower(power);
    }

    public void setLeft(double power) {
        leftOne.setPower(power);
    }


    public void setAll(double powerR, double powerL) {
        setRight(powerR);
        setLeft(powerL);
    }

    // This method will be used in a teleOp program to get stick values to set motor power and drive speed.
    public void updateByGamepad() {
        //sets the initial power to be scaled
        double leftPow = linearOpMode.gamepad1.left_stick_y;
        double rightPow = linearOpMode.gamepad1.right_stick_y;
        goBackwards();

        // These if statements allow for speed settings to be set
        if (linearOpMode.gamepad1.left_bumper) {
            driveSpeed = DriveSpeed.QUARTERSPEED;
        } else if (linearOpMode.gamepad1.right_bumper) {
            driveSpeed = DriveSpeed.HALFSPEED;
        } else {
            driveSpeed = DriveSpeed.FULLSPEED;
        }
        if (linearOpMode.gamepad1.dpad_left) {
            goForwards();
            teleOpPidTurn(1, getHeading() + 90);
        }
        if (linearOpMode.gamepad1.dpad_right) {
            if (firstTurn) {
                firstTurn = false;
                firstAngle = getHeading();
            }
            goForwards();
            teleOpPidTurn(1, getHeading() - 90);
        }
        if (linearOpMode.gamepad1.dpad_up) {
            goForwards();
            teleOpPidTurn(1, firstAngle);
        }
        if (linearOpMode.gamepad1.dpad_down) {
            firstAngle = getHeading();
        }
        if (linearOpMode.gamepad1.x) {
            firstAngle += 5;
        }
        if (linearOpMode.gamepad1.y) {
            firstAngle -= 5;
        }
        // sets the power of all motors. see other methods
        else {
            setAll(leftPow, rightPow, setSpeed(driveSpeed));
        }
    }

    // This method will tell the driver what drive setting the robot is operating on.
    public void updateTelemetry() {
        telemetry.addData("Drive Speed: ", driveSpeed);
        telemetry.addData("Heading: ", getHeading());
        telemetry.addData("Saved Angle: ", firstAngle);
        //telemetry.update();
    }

    // This method will call both the updateByGamepad method and updateTelemetry to get driver inputs and show speed setting.
    public void updateAll() {
        updateByGamepad();
        updateTelemetry();
    }

    /*
    The following methods are primarily related to autonomous.
     */

    /**
     * The angle which the robot is facing.
     *
     * @return angle at which the robot is facing
     */
    public double getHeading() {
        Orientation angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angle.firstAngle;
        //turning right is negative direction
    }

    // Distance sensor methods which get distance in inches
    public double getFrontDistInches() {
        return frontSensor.getDistanceUltra() / 2.54;
    }

    public double getSideDistInches() {
        return sideSensor.getDistanceUltra() / 2.54;
    }

    public double calcError(double targetInch) {
        double left_value = leftOne.getCurrentPosition();
        double right_value = rightOne.getCurrentPosition();
        double average = (left_value + right_value) / 2;
        return convertToTicks(targetInch) - average;
    }

    public int direction(double inch) {
        double error = calcError(inch);
        if (error < 0) {
            return -1;
        } else if (error > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void targetPosition(double ticks) {
        leftOne.setTargetPosition((int) ticks);
        rightOne.setTargetPosition((int) ticks);
    }


    //Converts inches to Orbital 20 motor encoder ticks
    public double convertToTicks(double inches) {
        double rotations = inches / (3 * Math.PI);
        double ticks = rotations * 537.6;
        return ticks;
    }


    //First PID implementation for movement methods. positive inch to go forward, negative to go backwards
    public void moveInches(double inches, double power) throws InterruptedException {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setAll(power, power);
        boolean shrekInch = true;
        int tickPos = (int) convertToTicks(inches);
        while (opModeIsActive() && shrekInch) {
            targetPosition(convertToTicks(inches));
            telemetry.addData("Target: ", convertToTicks(inches));
            telemetry.addData("Set Target pos: ", leftOne.getTargetPosition());
            printEncoderTicks();
            telemetry.update();
            if (leftOne.getCurrentPosition() >= (tickPos - 20) && leftOne.getCurrentPosition() <= (tickPos + 20)) {
                shrekInch = false;
                setAll(0, 0);
            }

        }
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Thread.sleep(150);

    }

    public void moveToWithinDistance(double inches, double power) throws InterruptedException {
        double inchtoMove = getFrontDistInches() - inches;
        moveInches(inchtoMove, power);
    }

    public void otherMoveToWithinDistance(double inches, double power) throws InterruptedException {
        double inchtoMove = getFrontDistInches() - inches;
        otherMoveInches(inchtoMove, power);
    }

    //Second PID implementation of movement methods.
    public void otherMoveInches(double inches, double power) throws InterruptedException {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setAll(power, power);
        int tickPos = (int) convertToTicks(inches);
        targetPosition(convertToTicks(inches));
        while (opModeIsActive() && !(leftOne.getCurrentPosition() >= (tickPos - 35) && leftOne.getCurrentPosition() <= (tickPos + 35))) {
            telemetry.addData("Target: ", convertToTicks(inches));
            printEncoderTicks();
            telemetry.update();
        }
        setAll(0, 0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Thread.sleep(150);
    }


    //The following methods are old and not PID based. Do Not Use
    @Deprecated
    public void moveForward(double inches, double power) {
        double rotations = inches / (3 * Math.PI);
        double goal = rotations * 537.6;
        betterMoveTicks(goal, power);
        setAll(0, 0);
    }

    @Deprecated
    public void moveBackwards(double inches, double power) {
        goBackwards();
        double rotations = inches / (3 * Math.PI);
        double goal = (rotations * 537.6);
        //moveTicks(goal, power);
        betterMoveTicks(goal, power);
        goForwards();
        setAll(0, 0);
    }

    /*public void moveToWithinDistance(double inches, double power, int direction) {
        double DIST_THRESHOLD = 1;
        //double timeout = linearOpMode.getRuntime() + 2.5;
        while (opModeIsActive() && !checkDistance(inches, power, DIST_THRESHOLD, direction)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }

    }*/

    /*public boolean checkDistance(double target, double power, double Threshold, int direction) {
        boolean matchDist = false;
        double error = getFrontDistInches() - target;
        double speed;
        double mod = .007;
        if (Math.abs(error) <= Threshold || !opModeIsActive()) {
            speed = 0;
            matchDist = true;
            telemetry.addLine("Robot is Stopped");
        } else {
            speed = Range.clip(power + Math.abs(error * mod), power, .5) * direction;
        }
        if (opModeIsActive()) {
            setAll(speed, speed);
            telemetry.addLine("Moving Robot.");
            telemetry.addData("Distance: ", getFrontDistInches());
        }
        return matchDist;
    }*/

    //sets motor directions for the robot to move backwards with  with positive power
    public void goBackwards() {
        leftOne.setDirection(DcMotorSimple.Direction.FORWARD);
        rightOne.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //sets motor directions for the robot to move forward with positive power
    public void goForwards() {
        leftOne.setDirection(DcMotorSimple.Direction.REVERSE);
        rightOne.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    //This method will print all of the encoder positions of the drivetrain motors to telemetry.
    public void printEncoderTicks() {
        telemetry.addData("left back: ", leftOne.getCurrentPosition());
        telemetry.addData("right back: ", rightOne.getCurrentPosition());
        //telemetry.update();
    }

    // move ticks will allow motors to move to a certain encoder position
    /*public void moveTicks(double ticks, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        // sets the position for the motors
        leftOne.setTargetPosition((int) ticks);
        rightOne.setTargetPosition((int) ticks);
        // moves motors to a position
        //ElapsedTime startTime = new ElapsedTime();
        while (leftOne.isBusy() || rightOne.isBusy()) {
            setAll(power, power);
            printEncoderTicks();
            telemetry.addData("Target Position", leftOne.getTargetPosition());
            telemetry.update();
            if (startTime.time() > 2.5){
                break;
            }
        }
    }*/

    public void betterMoveTicks(double ticks, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double threshold = 10;
        while ((rightOne.getCurrentPosition() <= ticks - threshold || rightOne.getCurrentPosition() >= ticks + threshold) && opModeIsActive()) {
            if (rightOne.getCurrentPosition() <= ticks - threshold) {
                setAll(power, power);
            }
            if (rightOne.getCurrentPosition() >= ticks + threshold) {
                setAll(-power, -power);
            }
            printEncoderTicks();
            telemetry.addData("Goal: ", ticks);
            telemetry.update();
        }
        setAll(0, 0);
    }


    public double getError(double target) {
        double headingError;
        // calculate error in -179 to +180 range  (
        headingError = target - getHeading();
        while (headingError >= 180) headingError -= 360;
        while (headingError <= -180) headingError += 360;
        return headingError;
    }


    public double getError2(double target) {
        double headingError;
        // calculate error in -179 to +180 range  (
        headingError = target - getHeading();
        if (target > 180) headingError -= 360;
        while (headingError <= -180) headingError += 360;
        return headingError;
    }

    /**
     * This method is the TeleOp version of pidTurn(). Uses less accurate heading check criteria to go faster.
     * Do not use in Autonomous
     *
     * @param power  Max power to run motors at
     * @param target Target heading
     */
    public void teleOpPidTurn(double power, double target) {
        // keep looping while we are still active, and not on heading.
        double HEADING_THRESHOLD = 1;
        if (Math.abs(getError(target)) < HEADING_THRESHOLD) {
            return;
        }
        double timeout = linearOpMode.getRuntime() + 2;
        ElapsedTime elapsedTime = new ElapsedTime();
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && !checkTeleopHeading(power, target, HEADING_THRESHOLD, elapsedTime.time())) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
        setAll(0, 0);
        if (linearOpMode.getRuntime() >= timeout) {
            telemetry.addLine("timed out!");
        }
        integral = 0;
        telemetry.addData("Heading Final: ", getHeading());
        telemetry.update();
    }


    /**
     * pidTurn() is the autonomous turning method which turns the robot in place. Uses a PID loop
     * to turn the robot to a desired heading. Use only in Autonomous.
     *
     * @param power  Max power to run motors at
     * @param target Target heading
     */
    public void pidTurn(double power, double target) {
        // keep looping while we are still active, and not on heading.
        double HEADING_THRESHOLD = 1;
        if (Math.abs(getError(target)) < HEADING_THRESHOLD) {
            return;
        }
        double timeout = linearOpMode.getRuntime() + 3;
        ElapsedTime elapsedTime = new ElapsedTime();
        while (opModeIsActive() && linearOpMode.getRuntime() < timeout && !checkHeading(power, target, HEADING_THRESHOLD, elapsedTime.time())) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
        setAll(0, 0);
        if (linearOpMode.getRuntime() >= timeout) {
            telemetry.addLine("timed out!");
        }
        integral = 0;
        telemetry.addData("Heading Final: ", getHeading());
        telemetry.update();
    }

    /**
     * Version of checkHeading which does not validate correct heading three times. Less accurate but faster.
     * Runs main component of teleopPidTurn()
     *
     * @param power             max power at which to run method
     * @param target            target heading
     * @param HEADING_THRESHOLD Threshold of which to pass in for stop condition
     * @param time              input of run time from loop start used for derivative calculation
     * @return Boolean value to keep loop running and stop when criteria is met
     */
    boolean checkTeleopHeading(double power, double target, double HEADING_THRESHOLD, double time) {
        double steer;
        double Kp = .008; //This is the proportional gain
        double Ki = .00000052734375 / 32; //This is the integral gain
        double Kd = .00421875; //This is the derivative gain.
        boolean goal = false;
        double leftSpeed = 0;
        double rightSpeed = 0;
        double PosOrNeg;
        double error = getError(target); // This calculates the error between target angle and current position.
        integral += (error * time); //Calculates Integral over time
        double derivative = (error) / time; //Calculates derivative over time
        // determine turn power based on +/- error
        if (Math.abs(error) <= HEADING_THRESHOLD || !opModeIsActive()) {
            headingCounter++;
            if (headingCounter >= 1 || !opModeIsActive()) {
                steer = 0.0;
                leftSpeed = 0;
                rightSpeed = 0;
                headingCounter = 0;
                goal = true;
            }
        } else {
            // This is the main part of the Proportional GyroTurn.  This takes a set power variable,
            // and adds the absolute value of error*kP.
            // This allows for the robot to turn faster when farther
            // away from the target, and turn slower when closer to the target.  This allows for quicker, as well
            // as more accurate turning when using the GyroSensor
            PosOrNeg = Range.clip((int) -error, -1, 1);
            steer = getSteer(error, Kp);
            //leftSpeed is determined bycombining the p, i, and d values and setting them within a range of .1 to set power.
            leftSpeed = Range.clip(Math.abs((error * Kp) + (Ki * integral) + (Kd * derivative)), .1, power) * PosOrNeg;
            rightSpeed = -leftSpeed;
            headingCounter = 0;
        }

        // Set motor speeds.
        if (opModeIsActive()) {
            setAll(rightSpeed, leftSpeed);
        }
        // Display debug info in telemetry.
        telemetry.addData("Target", target);
        telemetry.addData("Error: ", error);
        telemetry.addData("Left Power: ", leftSpeed);
        telemetry.addData("Right Power: ", rightSpeed);
        return goal;
    }

    /**
     * Main component of pidTurn(). Runs the central component of the PID and breaks loop when target heading is reached.
     * Has a 3 step validation for target heading to ensure pinpoint accuracy. Slower than teleOpPidTurn()
     *
     * @param power             max power at which to run method
     * @param target            target heading
     * @param HEADING_THRESHOLD Threshold of which to pass in for stop condition
     * @param time              input of run time from loop start used for derivative calculation
     * @return Boolean value to keep loop running and stop when criteria is met
     */
    boolean checkHeading(double power, double target, double HEADING_THRESHOLD, double time) {
        double steer;
        double Kp = .008; //This is the proportional gain
        double Ki = .00000052734375 / 32; //This is the integral gain
        double Kd = .00421875; //This is the derivative gain.
        boolean goal = false;
        double leftSpeed = 0;
        double rightSpeed = 0;
        double PosOrNeg;
        double error = getError(target); // This calculates the error between target angle and current position.
        integral += (error * time); //Calculates Integral over time
        double derivative = (error) / time; //Calculates derivative over time
        // determine turn power based on +/- error
        if (Math.abs(error) <= HEADING_THRESHOLD || !opModeIsActive()) {
            headingCounter++;
            if (headingCounter >= 3 || !opModeIsActive()) {
                steer = 0.0;
                leftSpeed = 0;
                rightSpeed = 0;
                headingCounter = 0;
                goal = true;
            }
        } else {
            // This is the main part of the Proportional GyroTurn.  This takes a set power variable,
            // and adds the absolute value of error*kP.
            // This allows for the robot to turn faster when farther
            // away from the target, and turn slower when closer to the target.  This allows for quicker, as well
            // as more accurate turning when using the GyroSensor
            PosOrNeg = Range.clip((int) -error, -1, 1);
            steer = getSteer(error, Kp);
            //leftSpeed is determined bycombining the p, i, and d values and setting them within a range of .1 to set power.
            leftSpeed = Range.clip(Math.abs((error * Kp) + (Ki * integral) + (Kd * derivative)), .1, power) * PosOrNeg;
            rightSpeed = -leftSpeed;
            headingCounter = 0;
        }

        // Set motor speeds.
        if (opModeIsActive()) {
            setAll(rightSpeed, leftSpeed);
        }
        // Display debug info in telemetry.
        telemetry.addData("Target", target);
        telemetry.addData("Error: ", error);
        telemetry.addData("Left Power: ", leftSpeed);
        telemetry.addData("Right Power: ", rightSpeed);
        return goal;
    }

    public double getSteer(double error, double mod) {
        return Range.clip(error * (2 * mod), -1, 1);
    }

    /**
     * Spline turn method which allows the robot to turn in a specified arc.
     *
     * @param radius adjusts the arc length of the turn in inches
     * @param power  max power of the motors
     * @param angle  sets the angle to which the arc should go to
     * @throws InterruptedException
     * @see #arcInches(double, double, double, double) for main turn component
     */
    public void arcTurn(double radius, double power, double angle) throws InterruptedException {
        double rads = Math.abs(angle * (Math.PI / 180));
        double startAngle = getHeading();
        double leftDist = 0;
        double rightDist = 0;
        double leftpower = 0;
        double rightpower = 0;
        PIDCoefficients innerPID = new PIDCoefficients(0, 0, 0);
        if (angle > 0) {
            leftDist = rads * radius;
            rightDist = rads * (radius + 17.5);
            rightpower = power;
            leftpower = (leftDist / rightDist) * rightpower;
            innerPID.p = (leftDist / rightDist) * pidCoefficients.p;
            innerPID.i = (leftDist / rightDist) * pidCoefficients.i;
            innerPID.d = (leftDist / rightDist) * pidCoefficients.d;
            leftOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, innerPID);
        }
        if (angle < 0) {
            leftDist = rads * (radius + 17.5);
            rightDist = rads * radius;
            leftpower = power;
            rightpower = (rightDist / leftDist) * leftpower;
            innerPID.p = (rightDist / leftDist) * pidCoefficients.p;
            innerPID.i = (rightDist / leftDist) * pidCoefficients.i;
            innerPID.d = (rightDist / leftDist) * pidCoefficients.d;
            rightOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, innerPID);
        }
        arcInches(leftDist, rightDist, leftpower, rightpower);
        if (Math.abs(getError(startAngle + angle)) >= 1) {
            pidTurn(power, startAngle + angle);
        }
        rightOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
        leftOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
    }

    /**
     * Inverse Spline turn
     *
     * @param radius adjusts the arc length of the turn in inches
     * @param power  max power of the motors
     * @param angle  sets the angle to which the arc should go to
     * @throws InterruptedException
     * @see #arcInches(double, double, double, double) for main turn component
     */
    public void reverseArcTurn(double radius, double power, double angle) throws InterruptedException {
        double rads = Math.abs(angle * (Math.PI / 180));
        double startAngle = getHeading();
        double leftDist = 0;
        double rightDist = 0;
        double leftpower = 0;
        double rightpower = 0;
        PIDCoefficients innerPID = new PIDCoefficients(0, 0, 0);
        if (angle < 0) {
            leftDist = rads * radius;
            rightDist = rads * (radius + 17.5);
            rightpower = power;
            leftpower = (leftDist / rightDist) * rightpower;
            innerPID.p = (leftDist / rightDist) * pidCoefficients.p;
            innerPID.i = (leftDist / rightDist) * pidCoefficients.i;
            innerPID.d = (leftDist / rightDist) * pidCoefficients.d;
            leftOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, innerPID);
        }
        if (angle > 0) {
            leftDist = rads * (radius + 17.5);
            rightDist = rads * radius;
            leftpower = power;
            rightpower = (rightDist / leftDist) * leftpower;
            innerPID.p = (rightDist / leftDist) * pidCoefficients.p;
            innerPID.i = (rightDist / leftDist) * pidCoefficients.i;
            innerPID.d = (rightDist / leftDist) * pidCoefficients.d;
            rightOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, innerPID);
        }
        arcInches(-leftDist, -rightDist, leftpower, rightpower);
        if (Math.abs(getError(startAngle + angle)) >= 1) {
            pidTurn(power, startAngle + angle);
        }
        rightOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
        leftOne.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
    }

    /**
     * Main component of the arcTurn method. Works by running two simultaneous PID loops in the REV hub to run motors at different speeds.
     *
     * @param leftdist  distance in inches that left wheels must move.
     * @param rightdist distance in inches that right wheels must move.
     * @param leftPow   maximum power to be sent to left wheels.
     * @param rightPow  maximum power to be sent to right wheels.
     * @throws InterruptedException
     */
    public void arcInches(double leftdist, double rightdist, double leftPow, double rightPow) throws InterruptedException {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setAll(rightPow, leftPow);
        boolean shrekInch = true;
        int tickPosLeft = (int) convertToTicks(leftdist);
        int tickPosRight = (int) convertToTicks(rightdist);
        while (opModeIsActive() && shrekInch) {
            leftOne.setTargetPosition(tickPosLeft);
            rightOne.setTargetPosition(tickPosRight);
            telemetry.addData("Target Left: ", convertToTicks(leftdist));
            telemetry.addData("Target Right: ", convertToTicks(rightdist));
            telemetry.addData("Set Target pos Left: ", leftOne.getTargetPosition());
            telemetry.addData("Set Target pos Right: ", rightOne.getTargetPosition());
            printEncoderTicks();
            telemetry.update();
            if ((leftOne.getCurrentPosition() >= (tickPosLeft - 20) && leftOne.getCurrentPosition() <= (tickPosLeft + 20)) &&
                    (rightOne.getCurrentPosition() >= (tickPosRight - 20) && rightOne.getCurrentPosition() <= (tickPosRight + 20))) {
                shrekInch = false;
                setAll(0, 0);
            }

        }
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Thread.sleep(150);

    }

    /**
     * Uses the pidTurn() method and the current robot heading to turn a certain angle relative from current robot heading.
     *
     * @param power max power at which to run
     * @param angle angle to turn from current heading
     */
    public void turnFromPosition(double power, double angle) {
        pidTurn(power, getHeading() + angle);
        //setAll(0, 0);
    }

    public boolean opModeIsActive() {
        return linearOpMode.opModeIsActive();
    }


    public boolean isTurning() {
        if (leftOne.getPower() / rightOne.getPower() < 0) {
            return true;
        }
        return false;
    }
}