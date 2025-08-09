package eclipse_sample.Archives.Testers;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

import java.util.Locale;

@Disabled
@TeleOp(name = "Everything Tester", group = "RobotTeleOp")

public class EverythingTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        /*  4 motors to test for drivetrain
            1 motor for slam dunk
            1 motor for relic
            4 servos for intake (controlled by power) (continuous)
            2 servos for jewel swatter (controlled by position) (normal)
            2 servos for the relic (controlled by position) (normal)
            1 servo for slam dunk, it pinches stuff (normal)

        */
        /* controller stuff:
            gamepad 1 for motors
            --D pad (top left plus sign) is used to "scroll"
                every time you scroll, the motor that you "selected" is going to show up on the
                phone using telemetry
            -- Joystick (bottom left) control motor power
            gamepad 2 for servos
            --D pad (top left plus sign) is used to "scroll"
                every time you scroll, the servo that you "selected" is going to show up on the
                phone using telemetry

         */
        //motors initial stuff

        //this is making an array for controller 1 in which there are all the motors
        Controller controller1 = new Controller(gamepad1);
        DcMotor[] motors = {hardwareMap.dcMotor.get(UniversalConstants.leftFrontDrive),
                hardwareMap.dcMotor.get(UniversalConstants.rightFrontDrive),
                hardwareMap.dcMotor.get(UniversalConstants.leftBackDrive),
                hardwareMap.dcMotor.get(UniversalConstants.rightBackDrive),
                hardwareMap.dcMotor.get(UniversalConstants.slamDunker),
                hardwareMap.dcMotor.get(UniversalConstants.relicSpoolMotor),
                hardwareMap.dcMotor.get(UniversalConstants.intakeMotorLeft),
                hardwareMap.dcMotor.get(UniversalConstants.intakeMotorRight)
        };
        //This is what will be printed on the phone when you scroll through the array
        String[] motorName = {"Left Front", "Right Front", "Left Back", "Right Back",
                "Slam Dunk", "Relic Motor", "Intake_2018OLD Left", "Intake_2018OLD Right"};

        int motorArrayPosition = 0;

        //servos initial stuff
        //this is making an array for controller 2 in which there are all the servos
        Controller controller2 = new Controller(gamepad2);
        //normal servo array
        Servo[] normalServos = {
                hardwareMap.servo.get(UniversalConstants.jewelGimbleElbow),
                hardwareMap.servo.get(UniversalConstants.jewelGimbleWrist),
                hardwareMap.servo.get(UniversalConstants.relicElbow),
                hardwareMap.servo.get(UniversalConstants.relicPincher),
                hardwareMap.servo.get(UniversalConstants.dunkerServo),
                hardwareMap.servo.get(UniversalConstants.intakeFoldOutLeft),
                hardwareMap.servo.get(UniversalConstants.intakeFoldOutRight),
                hardwareMap.servo.get(UniversalConstants.colorDistanceAutonomousServo)
        };
        //normal servo name array
        //This is what will be printed on teh phone when you scroll through the array
        String[] normalServoName = {"Jewel Gimble Elbow", "Jewel Gimble Wrist",
                "Relic Elbow", "Relic Pincher", "Slam Dunker", "Intake_2018OLD Fold Out Left", "Intake_2018OLD Fold Out Right", "Distance Servo"};

        int normalServoArrayPosition = 0;

        ColorSensor sensorColor;
        DistanceSensor sensorDistance;
        ModernRoboticsI2cRangeSensor forwardsWallDistSensor;
        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, UniversalConstants.jewelColorSensor);
        forwardsWallDistSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, UniversalConstants.forwardsWallSensor);

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, UniversalConstants.jewelColorSensor);
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;
        waitForStart();

        while (opModeIsActive()) {
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.addData("Distance From Wall (cm)", forwardsWallDistSensor.getDistance(DistanceUnit.CM));

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
            controller1.update();
            controller2.update();
            //code for motors

            if (controller1.dpadLeftOnce()) {
                motorArrayPosition -= 1;
            } else if (controller1.dpadRightOnce()) {
                motorArrayPosition += 1;
            }
            motorArrayPosition = (motorArrayPosition + motors.length) % motors.length;
            //motor position will always be between 0 and 5

            telemetry.addData("Motor Selected", motorName[motorArrayPosition]);
            motors[motorArrayPosition].setPower(controller1.left_stick_y);
            //y is for up and down
            //x is for right and left of the bottom left joystick
            telemetry.addData("Motor Selected Power", motors[motorArrayPosition].getPower());
            telemetry.addData("Motor Encoder Position", motors[motorArrayPosition].getCurrentPosition());

            //code for Normal Servos; This will the Left and Right for the D pad; POSITION

            if (controller2.dpadLeftOnce()) {
                normalServoArrayPosition -= 1;
            } else if (controller2.dpadRightOnce()) {
                normalServoArrayPosition += 1;
            }
            normalServoArrayPosition = (normalServoArrayPosition + normalServos.length) % normalServos.length;
            telemetry.addData("Normal Servo Selected", normalServoName[normalServoArrayPosition]);
            //if y is pressed, add .01 to the position
            //if a is pressed, subtract .01 to the position
            if (controller2.YOnce()) {
                normalServos[normalServoArrayPosition].setPosition(normalServos[normalServoArrayPosition].getPosition() + .01);
            } else if (controller2.AOnce()) {
                normalServos[normalServoArrayPosition].setPosition(normalServos[normalServoArrayPosition].getPosition() - .01);
            } else if (controller2.leftBumperOnce()) {
                normalServos[normalServoArrayPosition].setPosition(normalServos[normalServoArrayPosition].getPosition() + .1);
            } else if (controller2.rightBumperOnce()) {
                normalServos[normalServoArrayPosition].setPosition(normalServos[normalServoArrayPosition].getPosition() - .1);
            }
            telemetry.addData("Normal Servo Selected Position", normalServos[normalServoArrayPosition].getPosition());

            /*
            //code for Continuous Servos; This will be Up and Down for for D pad; down is -1, up is +1; POWER

            if (controller2.dpadDownOnce()) {
                continuousServoArrayPosition -= 1;
            } else if (controller2.dpadUpOnce()) {
                continuousServoArrayPosition += 1;
            }
            continuousServoArrayPosition = ((continuousServoArrayPosition + continuousServos.length) % continuousServos.length);
            telemetry.addData("Continuous Servo Selected", continuousServoName[continuousServoArrayPosition]);
            continuousServos[continuousServoArrayPosition].setPower(controller2.left_stick_y / 2);
            telemetry.addData("Continuous Servo Selected Power", continuousServos[continuousServoArrayPosition].getPower());
            */

            telemetry.update();
        }

        // Set the panel back to the default color
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });

    }
}