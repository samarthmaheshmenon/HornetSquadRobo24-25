package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp(name = "New Arm Tester")
public class armTeleopFar extends LinearOpMode {
    enum armState {
        STOWED, INTAKING, CROUCHING, SCORING,
        DEPLOYING, CROUCHING_TO_SCORING, SCORING_TO_CROUCHING, CROUCHING_TO_INTAKING, INTAKING_TO_CROUCHING,
        TO_STOWED, SCORING_TO_STOWED, CROUCHING_TO_STOWED
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double angle1 = -28.98, angle2 = 170.83, angle3 = -163.83, angle4 = 125; //Stowed Angles
        boolean wasTurning = false;
        double joint2Saved = 0;
        double seg1 = 14.75;
        double seg2 = 17.0056 + 16;
        double YPrime = 0;
        double y;
        double x = 30;
        double z;
        double height = 9.95;
        double joint4Error;
        boolean liftPositioned = false;
        Servo servo = this.hardwareMap.servo.get(UniConstTwo.dispenser);


        double[][] DEPLOYING = new double[][]{
                {60, -100, 0, 40}
        };

        double[][] TO_CROUCHING = new double[][]{
                //{0, 130, -130, 60},
                {40, 105, -140, 46},
        };

        double[][] CROUCHING_TO_SCORING = new double[][]{
                {70, 70, 25, -105},
        };

        double[][] CROUCHING_TO_INTAKING = new double[][]{
                {60, -110, 0, 40}
        };

        double[][] TO_STOWED = new double[][]{
                {-26, 168, -161, 123},
        };

        double[][] CROUCHING_TO_STOWED = new double[][]{
                {-20, 160, -160, 135}
        };

        double[][] SCORING_TO_STOWED = new double[][]{
                {-20, 160, -160, 135},
        };

        double currentTime = 0;
        armState state = armState.STOWED;
        telemetry.addLine("Initialized");
        telemetry.update();
        waitForStart();
        servo.setPosition(.2);
        arm.joint1.setAngle(angle1);
        arm.joint2.setRelativeAngle(angle2);
        arm.joint3.setRelativeAngle(angle3);
        arm.joint3.setRelativeAngle(angle4);
        arm.disable();
        lift.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.liftMotor.setTargetPosition(3200);
        lift.liftMotor.setPower(.6);

        while (opModeIsActive()) {
            switch (state) {
                case STOWED:
                    arm.disable();
                    if (gamepad2.x && gamepad2.y) {
                        state = armState.DEPLOYING;
                        arm.enable();
                        angle1 = DEPLOYING[0][0];
                        angle2 = DEPLOYING[0][1];
                        angle3 = DEPLOYING[0][2];
                        angle4 = DEPLOYING[0][3];
                        arm.joint1.setPower(.2);
                        arm.joint2.setPower(.55);
                        arm.joint3.setPower(.8);
                        arm.joint4.setPower(1);
                    }
                    break;
                case DEPLOYING:
                    if (arm.checkPos()) {
                            state = armState.INTAKING;
                            arm.joint1.setPower(0);
                            arm.joint2.setPower(1);
                            arm.joint3.setPower(1);
                            arm.joint4.setPower(1);
                            arm.joint2.motor.setMotorDisable();
                            arm.joint1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            break;
                    }
                    angle1 = DEPLOYING[0][0];
                    angle2 = DEPLOYING[0][1];
                    angle3 = DEPLOYING[0][2];
                    angle4 = DEPLOYING[0][3];
                    break;
                case INTAKING:
                    /*if (arm.joint1.getAngle() < 0) {
                        double error = 0 - arm.joint1.getAngle();
                        arm.joint1.setPower(error * .1);
                        telemetry.addData("Correcting:", error * .1);
                    } else {*/
                        arm.joint1.setPower(gamepad2.right_stick_y / 3);
                    //}
                    angle4 = -20;
                    arm.joint2.motor.setMotorDisable();

                    //EXIT CASES
                    if (gamepad2.right_bumper) {
                        state = armState.INTAKING_TO_CROUCHING;
                        arm.joint2.motor.setMotorEnable();
                        arm.joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        angle1 = TO_CROUCHING[0][0];
                        angle2 = TO_CROUCHING[0][1];
                        angle3 = TO_CROUCHING[0][2];
                        angle4 = TO_CROUCHING[0][3];
                        arm.joint1.setPower(.3);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    } else if (gamepad2.x && gamepad2.y) {
                        state = armState.TO_STOWED;
                        arm.joint2.motor.setMotorEnable();
                        arm.joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        angle1 = TO_STOWED[0][0];
                        angle2 = TO_STOWED[0][1];
                        angle3 = TO_STOWED[0][2];
                        angle4 = TO_STOWED[0][3];
                        arm.joint1.setPower(.3);
                        arm.joint2.setPower(.7);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    }
                    break;
                case INTAKING_TO_CROUCHING:
                    if (arm.checkPos()) {
                        state = armState.CROUCHING;
                        break;
                    }
                    joint4Error = angle4 - arm.joint4.getAngle();
                    arm.setAllPower(1.0 - joint4Error * .02);
                    telemetry.addData("Power: ", 1.0 - joint4Error * .02);

//                    if(arm.joint4.getAbsoluteAngle() > 30 && !hasStopped){
//                        arm.intake.intaker();
//                    }
//                    else if(!hasStopped){
//                        arm.intake.stopper();
//                        hasStopped = true;
//                    }
                    break;
                case CROUCHING:
                    if (gamepad2.right_bumper) {
                        state = armState.CROUCHING_TO_SCORING;
                        angle1 = CROUCHING_TO_SCORING[0][0];
                        angle2 = CROUCHING_TO_SCORING[0][1];
                        angle3 = CROUCHING_TO_SCORING[0][2];
                        angle4 = CROUCHING_TO_SCORING[0][3];
                        arm.joint1.setPower(.4);
                        arm.joint2.setPower(.3);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    } else if (gamepad2.left_bumper) {
                        state = armState.CROUCHING_TO_INTAKING;
                        angle1 = CROUCHING_TO_INTAKING[0][0];
                        angle2 = CROUCHING_TO_INTAKING[0][1];
                        angle3 = CROUCHING_TO_INTAKING[0][2];
                        angle4 = CROUCHING_TO_INTAKING[0][3];
//                        arm.joint1.setPower(1);
//                        arm.joint2.setPower(1);
//                        arm.joint3.setPower(1);
//                        arm.joint4.setPower(1);
                        arm.joint1.setPower(.7);
                        arm.joint2.setPower(.3);
                        arm.joint3.setPower(.7);
                        arm.joint4.setPower(1);
                    } else if (gamepad2.x && gamepad2.y) {
                        state = armState.CROUCHING_TO_STOWED;
                        angle1 = CROUCHING_TO_STOWED[0][0];
                        angle2 = CROUCHING_TO_STOWED[0][1];
                        angle3 = CROUCHING_TO_STOWED[0][2];
                        angle4 = CROUCHING_TO_STOWED[0][3];
                        arm.joint1.setPower(1);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    }
                    break;
                case CROUCHING_TO_SCORING:
                    if (arm.checkPos()) {
                        state = armState.SCORING;
                        arm.joint4.setPower(0);
                        arm.joint4.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        break;
                    }
                    break;
                case SCORING:

//                    if(isScoring && arm.check4()){
//                        isScoring = false;
//                    }
//                    if(gamepad2.a){
//                        angle4 = 10;
//                        isScoring = true;
//                    }
//                    else if (!isScoring){
//                        angle4 = CROUCHING_TO_SCORING[posCount][3];
//                    }
                    arm.joint4.setPower(-gamepad2.left_stick_y);
//                    if (arm.joint4.getAngle() < 0) {
//                        arm.joint4.setPower(-gamepad2.left_stick_y);
//                    } else {
//                        if (gamepad2.left_stick_y < 0) {
//                            arm.joint4.setPower(gamepad2.left_stick_y);
//                        } else {
//                            arm.joint4.setPower(0);
//                        }
//                    }
                    if (gamepad2.left_bumper) {
                        state = armState.SCORING_TO_CROUCHING;
                        arm.joint4.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        angle1 = TO_CROUCHING[0][0];
                        angle2 = TO_CROUCHING[0][1];
                        angle3 = TO_CROUCHING[0][2];
                        angle4 = TO_CROUCHING[0][3];
                        arm.joint1.setPower(.25);
                        arm.joint2.setPower(.15);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    } else if (gamepad2.x && gamepad2.y) {
                        state = armState.TO_STOWED;
                        angle1 = TO_STOWED[0][0];
                        angle2 = TO_STOWED[0][1];
                        angle3 = TO_STOWED[0][2];
                        angle4 = TO_STOWED[0][3];
                        arm.joint1.setPower(1);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                    }
                    break;
                case SCORING_TO_CROUCHING:
                    if (arm.checkPos()) {
                        state = armState.CROUCHING;
                        break;

                    }
                    angle1 = TO_CROUCHING[0][0];
                    angle2 = TO_CROUCHING[0][1];
                    angle3 = TO_CROUCHING[0][2];
                    angle4 = TO_CROUCHING[0][3];
                    break;
                case CROUCHING_TO_INTAKING:
                    if (arm.checkPos()) {
                        state = armState.INTAKING;
                        arm.joint1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        arm.joint2.motor.setMotorDisable();
                        angle3 = 0;
                        angle4 = -18;
                        arm.joint1.setPower(0);
                        arm.joint2.setPower(0);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                        break;
                    }
                    break;
                case TO_STOWED:
                    if (arm.checkPos()) {
                        state = armState.STOWED;
                        arm.disable();
                        break;
                    }
                    break;
            }
            if (!(state == armState.STOWED)) {
                if (arm.joint1.convertToTicks(angle1) != arm.joint1.one.getTargetPosition()) {
                    arm.joint1.setAngle(angle1);
                }
                if (arm.joint2.convertToTicks(angle2) != arm.joint2.motor.getTargetPosition()) {
                    arm.joint2.setRelativeAngle(angle2);
                }
                if (arm.joint3.convertToTicks(angle3) != arm.joint3.motor.getTargetPosition()) {
                    arm.joint3.setRelativeAngle(angle3);
                }
                if (arm.joint4.convertToTicks(angle4) != arm.joint4.motor.getTargetPosition()) {
                    if (state == armState.INTAKING) {
                        arm.joint4.setAngle(angle4);
                    } else {
                        arm.joint4.setRelativeAngle(angle4);
                    }
                }
            }
            telemetry.addData("Arm State: ", state);
            arm.updateTelemetry();
            drivetrain.updateAll();
            if (liftPositioned) {
                lift.updateByGamepad();
            } else if (Math.abs(lift.liftMotor.getCurrentPosition() - lift.liftMotor.getTargetPosition()) < 5) {
                liftPositioned = true;
                lift.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            arm.intake.update();
            telemetry.update();
        }
    }
}


