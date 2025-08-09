package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@Autonomous(name = "Auto Tester New")
public class AutonomousTesterNew extends LinearOpMode {

    public class ActionType {
        public String name;
        public Runnable action;

        public ActionType(String actionName, Runnable actionAction) {
            name = actionName;
            action = actionAction;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        final Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.FLOAT);
        telemetry.addLine("Ready to go!");
        telemetry.update();

        Controller game1 = new Controller(gamepad1);
        int position = 0;
        ActionType[] actions = new ActionType[]{
                new ActionType("Forwards 10 Inches", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.moveToInches(10, 1);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Backwards 10 Inches", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.moveToInches(-10, 1);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Color Distance Up", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.swingColorDistanceUp();
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Color Distance Down", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.swingColorDistanceDown();
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Strafe to 70cm from Right", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.autoRightDistanceSensor(70, .15, robot.driveTrain.getHeading(), DistanceUnit.CM);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Strafe to 10cm Left", new Runnable() {
                    @Override
                    public void run() {
                        double distance = 10;
                        robot.driveTrain.strafeToDistanceLeft(.2, distance, DistanceUnit.CM);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Strafe to 6cm Left", new Runnable() {
                    @Override
                    public void run() {
                        double distance = 6;
                        robot.driveTrain.strafeToDistanceLeft(.2, distance, DistanceUnit.CM);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Strafe to 6cm Right", new Runnable() {
                    @Override
                    public void run() {
                        double distance = 6;
                        robot.driveTrain.strafeToDistanceRight(.2, distance, DistanceUnit.CM);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Encoder Strafe 6cm Right", new Runnable() {
                    @Override
                    public void run() {
                        double distance = -6;
                        robot.driveTrain.encoderStrafeToInches(distance, .25);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Encoder Strafe 10cm Left", new Runnable() {
                    @Override
                    public void run() {
                        double distance = 10;
                        robot.driveTrain.encoderStrafeToInches(distance, .25);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Encoder Strafe 6cm Left", new Runnable() {
                    @Override
                    public void run() {
                        double distance = 6;
                        robot.driveTrain.encoderStrafeToInches(distance, .25);
                        robot.driveTrain.park();
                    }
                }),
                new ActionType("Skip a column while going Right", new Runnable() {
                    double targetAngle = robot.driveTrain.getHeading();
                    double power = .1;
                    double distance = 7;
                    DistanceUnit unit = DistanceUnit.CM;

                    @Override
                    public void run() {
                        robot.driveTrain.swingColorDistanceDown();
                        robot.driveTrain.strafeToDistanceRightCoast(power, distance, targetAngle, unit);
                        robot.driveTrain.translateBy(0, power, 0);
                        robot.driveTrain.swingColorDistanceUp();
                        robot.driveTrain.encoderStrafeToInches(-1.5, power);
                        robot.driveTrain.swingColorDistanceDown();
                        robot.driveTrain.park();
                    }
                }),

                new ActionType("Swat Red Jewel", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            robot.jewelSwatter.removeJewel(AutonomousUtil.AllianceColor.Red);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }),
                new ActionType("Turn to 90 Degrees", new Runnable() {
                    @Override
                    public void run() {
                        robot.driveTrain.gyroTurn(.1, 90);
                    }
                })
        };


        waitForStart();
        robot.vuforiaRelicRecoveryGetter.activateTrackables();
        while (opModeIsActive()) {
            while (!gamepad1.a && opModeIsActive()) {
                game1.update();
                if (game1.dpadLeftOnce()) {
                    position -= 1;
                } else if (game1.dpadRightOnce()) {
                    position += 1;
                }
                position = (position + actions.length) % actions.length;
                telemetry.addData("Color Distance Reading Left (cm)", robot.driveTrain.leftSensorDistance.getDistance(DistanceUnit.CM));
                telemetry.addData("Color Distance Reading Right (cm)", robot.driveTrain.rightSensorDistance.getDistance(DistanceUnit.CM));
                telemetry.addData("Heading", robot.driveTrain.getHeading());
                telemetry.addData("Forwards Distance (cm)", robot.driveTrain.forwardsWallDistanceSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Left Distance (cm)", robot.driveTrain.leftFacingDistanceSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Right Distance (cm)", robot.driveTrain.rightFacingDistanceSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Chosen Task", actions[position].name);
                telemetry.update();
            }

            actions[position].action.run();

        }
    }
}
