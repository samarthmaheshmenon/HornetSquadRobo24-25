package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp(name = "Side Full Teleop")
public class armTeleopSide extends LinearOpMode {
    enum armState {
        STOWED, INTAKING, CROUCHING, SCORING,
        DEPLOYING, CROUCHING_TO_SCORING, SCORING_TO_CROUCHING, CROUCHING_TO_INTAKING, INTAKING_TO_CROUCHING,
        INTAKING_TO_STOWED, SCORING_TO_STOWED, CROUCHING_TO_STOWED
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double angle1 = -20, angle2 = 160, angle3 = -160, angle4 = 135;
        boolean wasTurning = false;
        double joint2Saved = 0;
        Servo servo = this.hardwareMap.servo.get(UniConstTwo.dispenser);


        double[][] DEPLOYING = new double[][]{
                //{40, 160, -130, -15},
                {20, 160, 0, -15},
        };

        /*double[][] DEPLOYING = new double[][]{
                {40, 140, -130, -15},
                {50, 140, 0, -15},
        };*/

        double[][] INTAKING_TO_CROUCHING = new double[][]{
                {35, 140, -140, 130},
        };

        double[][] CROUCHING_TO_SCORING = new double[][]{
                {45, 90, -90, 40},
                {65, 20, -20, -60},
                //{20, 90, -30, 40},
                //{40, 0, 20, -50},
                //{70, 0, 0, -73},
        };

        double[][] SCORING_TO_CROUCHING = new double[][]{
                //{65, 90, -70, 130},
                {55, 60, -40, 130},
                {55, 100, -70, 130},
                {35, 140, -140, 130},
        };

        double[][] CROUCHING_TO_INTAKING = new double[][]{
                {40, 140, -130, -15},
                {50, 140, 0, -15},
        };

        double[][] INTAKING_TO_STOWED = new double[][]{
                //{62, 150, 0, -20},
                {-20, 160, -160, 135},
        };

        double[][] CROUCHING_TO_STOWED = new double[][]{
                {-20, 160, -160, 135}
        };

        double[][] SCORING_TO_STOWED = new double[][]{
                {25, 120, -50, 50},
                {25, 140, -100, 90},
                {-20, 160, -160, 135},
        };

        double currentTime = 0;
        int posCount = 0;
        armState state = armState.STOWED;
        telemetry.addLine("Initialized");
        telemetry.update();
        waitForStart();
        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        servo.setPosition(.2);
        arm.joint1.setAngle(angle1);
        arm.joint2.setRelativeAngle(angle2);
        arm.joint3.setRelativeAngle(angle3);
        arm.joint3.setRelativeAngle(angle4);


        while (opModeIsActive()) {
            switch (state) {
                case STOWED:
                    arm.disable();
                    if (gamepad2.x && gamepad2.y) {
                        arm.enable();
                        elapsedTime.reset();
                        arm.joint1.setPower(1);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                        angle1 = DEPLOYING[posCount][0];
                        angle2 = DEPLOYING[posCount][1];
                        angle3 = DEPLOYING[posCount][2];
                        angle4 = DEPLOYING[posCount][3];
                        state = armState.DEPLOYING;
                        elapsedTime.reset();
                    }
                    break;
                case DEPLOYING:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= DEPLOYING.length) {
                            state = armState.INTAKING;
                            arm.joint2.motor.setMotorDisable();
                            posCount = 0;
                            arm.joint1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            arm.joint1.setPower(-gamepad2.right_stick_y / 2);
                            break;
                        }
                    }
                    angle1 = DEPLOYING[posCount][0];
                    angle2 = DEPLOYING[posCount][1];
                    angle3 = DEPLOYING[posCount][2];
                    angle4 = DEPLOYING[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
                case INTAKING:
                    if (arm.joint1.getAngle() < 20){
                        double error =  20 - arm.joint1.getAngle();
                        arm.joint1.setPower(error*.0167);
                        telemetry.addData("Correcting:", error*.167);
                    }
                    else {
                        arm.joint1.setPower(-gamepad2.right_stick_y / 2);
                    }
                    angle4 -= gamepad2.left_stick_y * 4;
                    arm.intake.update();
                    if(drivetrain.isTurning()){
                        wasTurning = true;
                        arm.joint2.motor.setMotorEnable();
                        angle3 = - 20;
                    }
                    else{
                        wasTurning = false;
                        angle3 = 0;
                        angle2 = arm.joint2.getAngle();
                        arm.joint2.motor.setMotorDisable();
                    }
                    telemetry.addData("Angle 2: ", angle2);

                    //EXIT CASES
                    if (gamepad2.right_bumper) {
                        state = armState.INTAKING_TO_CROUCHING;
                        elapsedTime.reset();
                        posCount = 0;
                        arm.joint2.motor.setMotorEnable();
                        arm.joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        arm.joint1.setPower(1);
                        angle1 = INTAKING_TO_CROUCHING[0][0];
                    }
                    if (gamepad2.x && gamepad2.y) {
                        state = armState.INTAKING_TO_STOWED;
                        elapsedTime.reset();
                        posCount = 0;
                        arm.joint2.motor.setMotorEnable();
                        arm.joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        arm.joint1.setPower(1);
                        angle1 = INTAKING_TO_STOWED[0][0];
                    }
                    break;
                case INTAKING_TO_CROUCHING:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= INTAKING_TO_CROUCHING.length) {
                            state = armState.CROUCHING;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = INTAKING_TO_CROUCHING[posCount][0];
                    angle2 = INTAKING_TO_CROUCHING[posCount][1];
                    angle3 = INTAKING_TO_CROUCHING[posCount][2];
                    angle4 = INTAKING_TO_CROUCHING[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.intaker();
                    break;
                case CROUCHING:
                    if (gamepad2.right_bumper) {
                        state = armState.CROUCHING_TO_SCORING;
                        elapsedTime.reset();
                        posCount = 0;
                        angle1 = CROUCHING_TO_SCORING[posCount][0];
                        angle2 = CROUCHING_TO_SCORING[posCount][1];
                        angle3 = CROUCHING_TO_SCORING[posCount][2];
                        angle4 = CROUCHING_TO_SCORING[posCount][3];
                    }
                    if (gamepad2.left_bumper) {
                        state = armState.CROUCHING_TO_INTAKING;
                        elapsedTime.reset();
                        posCount = 0;
                        angle1 = CROUCHING_TO_INTAKING[posCount][0];
                        angle2 = CROUCHING_TO_INTAKING[posCount][1];
                        angle3 = CROUCHING_TO_INTAKING[posCount][2];
                        angle4 = CROUCHING_TO_INTAKING[posCount][3];
                    }
                    if (gamepad2.x && gamepad2.y) {
                        state = armState.CROUCHING_TO_STOWED;
                        elapsedTime.reset();
                        posCount = 0;
                        angle1 = CROUCHING_TO_STOWED[posCount][0];
                        angle2 = CROUCHING_TO_STOWED[posCount][1];
                        angle3 = CROUCHING_TO_STOWED[posCount][2];
                        angle4 = CROUCHING_TO_STOWED[posCount][3];
                    }
                    arm.intake.update();
                    break;
                case CROUCHING_TO_SCORING:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= CROUCHING_TO_SCORING.length) {
                            state = armState.SCORING;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = CROUCHING_TO_SCORING[posCount][0];
                    angle2 = CROUCHING_TO_SCORING[posCount][1];
                    angle3 = CROUCHING_TO_SCORING[posCount][2];
                    angle4 = CROUCHING_TO_SCORING[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.intaker();
                    break;
                case SCORING:
                    angle4 += gamepad2.left_stick_y * 5;
                    if (gamepad2.left_bumper) {
                        state = armState.SCORING_TO_CROUCHING;
                        elapsedTime.reset();
                        posCount = 0;
                        angle1 = SCORING_TO_CROUCHING[posCount][0];
                        angle2 = SCORING_TO_CROUCHING[posCount][1];
                        angle3 = SCORING_TO_CROUCHING[posCount][2];
                        angle4 = SCORING_TO_CROUCHING[posCount][3];
                    }
                    if (gamepad2.x && gamepad2.y) {
                        state = armState.SCORING_TO_STOWED;
                        elapsedTime.reset();
                        posCount = 0;
                        angle1 = SCORING_TO_STOWED[posCount][0];
                        angle2 = SCORING_TO_STOWED[posCount][1];
                        angle3 = SCORING_TO_STOWED[posCount][2];
                        angle4 = SCORING_TO_STOWED[posCount][3];
                    }
                    arm.intake.update();
                    break;
                case SCORING_TO_CROUCHING:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= SCORING_TO_CROUCHING.length) {
                            state = armState.CROUCHING;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = SCORING_TO_CROUCHING[posCount][0];
                    angle2 = SCORING_TO_CROUCHING[posCount][1];
                    angle3 = SCORING_TO_CROUCHING[posCount][2];
                    angle4 = SCORING_TO_CROUCHING[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
                case CROUCHING_TO_INTAKING:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= CROUCHING_TO_INTAKING.length) {
                            state = armState.INTAKING;
                            posCount = 0;
                            arm.joint2.motor.setMotorDisable();
                            arm.joint1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            arm.joint4.coefficients.p = 10;
                            arm.joint4.motor.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, arm.joint4.coefficients);
                            break;
                        }
                    }
                    angle1 = CROUCHING_TO_INTAKING[posCount][0];
                    angle2 = CROUCHING_TO_INTAKING[posCount][1];
                    angle3 = CROUCHING_TO_INTAKING[posCount][2];
                    angle4 = CROUCHING_TO_INTAKING[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
                case INTAKING_TO_STOWED:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= INTAKING_TO_STOWED.length) {
                            state = armState.STOWED;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = INTAKING_TO_STOWED[posCount][0];
                    angle2 = INTAKING_TO_STOWED[posCount][1];
                    angle3 = INTAKING_TO_STOWED[posCount][2];
                    angle4 = INTAKING_TO_STOWED[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
                case CROUCHING_TO_STOWED:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= CROUCHING_TO_STOWED.length) {
                            state = armState.STOWED;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = CROUCHING_TO_STOWED[posCount][0];
                    angle2 = CROUCHING_TO_STOWED[posCount][1];
                    angle3 = CROUCHING_TO_STOWED[posCount][2];
                    angle4 = CROUCHING_TO_STOWED[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
                case SCORING_TO_STOWED:
                    if(arm.checkPos()){
                        posCount ++;
                        if (posCount >= SCORING_TO_STOWED.length) {
                            state = armState.STOWED;
                            posCount = 0;
                            break;
                        }
                    }
                    angle1 = SCORING_TO_STOWED[posCount][0];
                    angle2 = SCORING_TO_STOWED[posCount][1];
                    angle3 = SCORING_TO_STOWED[posCount][2];
                    angle4 = SCORING_TO_STOWED[posCount][3];
                    telemetry.addData("Array State: ", posCount);
                    arm.intake.update();
                    break;
            }
            if (!(state == armState.STOWED)) {
                if (arm.joint1.convertToTicks(angle1) != arm.joint1.one.getTargetPosition()) {
                    if (state == armState.INTAKING) {
                    } else {
                        arm.joint1.setAngle(angle1);
                    }
                }
                if (arm.joint2.convertToTicks(angle2) != arm.joint2.motor.getTargetPosition()) {
                    arm.joint2.setRelativeAngle(angle2);
                }
                if (arm.joint3.convertToTicks(angle3) != arm.joint3.motor.getTargetPosition()) {
                    arm.joint3.setRelativeAngle(angle3);
                }
                if (arm.joint4.convertToTicks(angle4) != arm.joint4.motor.getTargetPosition()) {
                    arm.joint4.setRelativeAngle(angle4);
                }
            }
            telemetry.addData("Arm State: ", state);
            arm.updateTelemetry();
            drivetrain.updateAll();
            lift.updateByGamepad();
            telemetry.update();
        }
    }
}

