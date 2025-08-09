package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@TeleOp(name = "Close Teleop")
public class arm_TeleOp extends LinearOpMode {
    enum armState {
        STOWED, DEPLOYING, INTAKING, TO_SCORING, SCORING, TO_INTAKING, INTAKING_TO_STOWED, SCORING_TO_STOWED
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double angle1 = -21, angle2 = 142, angle3 = -22, angle4 = 76;
        double loopTime = 0;
        boolean hold2 = true;
        Servo servo = this.hardwareMap.servo.get(UniConstTwo.dispenser);

        double[][] deployArray = new double[][]{
                {180, 120, -30, -900, 100},
                {180, 120, -30, -900, 200},
                {-21, 120, -30, -45, 100},
                {60, 130, -30, -45, 400},
                {62, 140, -30, -20, 300},
                {62, 150, -10, -20, 300},
                {62, 150, 0, -20, 300},
                {62, 140, -10, -30, 100},
        };

        double[][] scoreArray = new double[][]{
                //Crouching tiger hidden robot
                {20, 140, -350, 45, 500},
                {40, 140, -350, 90, 100},
                {25, 140, -350, 90, 100},
                {25, 140, -130, 90, 100},
                //Attack!
                {60, 110, -100, 90, 100},
                {60, 45, -38, 0, 100},
        };

        double[][] intakeArray = new double[][]{
                {25, 370, -35, 0, 700},
                {50, 100, -15, 0, 100},
                {62, 120, -15, 0, 400},
                {62, 130, -15, -20, 400},
                {62, 150, 10, -20, 400},
                {62, 110, -10, -30, 100},
        };

        double[][] stowFromIntake = new double[][]{
                {62, 150, 0, -20, 200},
                {30, 155, -100, 0, 500},
                {0, 155, -100, 50, 200},
                {-15, 155, -150, 130, 500},
        };

        double[][] stowFromScoring = new double[][]{
                {62, 110, -10, -10, 300},
                {35, 130, -50, 50, 5000},
                {25, 140, -100, 90, 5000},
                {-15, 155, -150, 130, 5000},
        };
        double[][] stowingArray = new double[][]{};
        double currentTime = 0;
        int timeCount = 0;
        telemetry.addLine("Initialized");
        telemetry.update();
        armState state = armState.STOWED;
        waitForStart();
        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        arm.joint4.setRelativeAngle(90);
        arm.joint4.setPower(1);
        while (opModeIsActive()) {
            servo.setPosition(.25);
            loopTime = elapsedTime.time();
            switch (state) {
                case STOWED:
                    if (gamepad2.x && gamepad2.y) {
                        arm.enable();
                        elapsedTime.reset();
                        arm.joint1.setPower(1);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        angle1 = deployArray[timeCount][0];
                        angle2 = deployArray[timeCount][1];
                        angle3 = deployArray[timeCount][2];
                        angle4 = deployArray[timeCount][3];
                        state = armState.DEPLOYING;
                        elapsedTime.reset();
                    }
                    break;
                case DEPLOYING:
                    angle1 = deployArray[timeCount][0];
                    angle2 = deployArray[timeCount][1];
                    angle3 = deployArray[timeCount][2];
                    angle4 = deployArray[timeCount][3];
                    //telemetry.addData("Difference: ", elapsedTime.time() - deployArray[timeCount][4]);
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - deployArray[timeCount][4] >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= deployArray.length) {
                        state = armState.INTAKING;
                        arm.joint2.motor.setMotorDisable();
                        timeCount = 0;
                    }
                    arm.intake.update();
                    break;
                case INTAKING:
                    if(gamepad2.right_trigger != 0){
                        angle1 = Range.clip(angle1 - 2, 55, 180);
                    }
                    else if(gamepad2.left_trigger != 0){
                        angle1 = Range.clip(angle1 + 2, 55, 180);
                    }
                    angle2 = 126;
                    angle3 = 10;
                    angle4 = -30;
                    arm.intake.update();
                    if (gamepad2.right_bumper) {
                        state = armState.TO_SCORING;
                        elapsedTime.reset();
                        timeCount = 0;
                        arm.joint2.motor.setMotorEnable();
                    }
                    if (gamepad2.x && gamepad2.y) {
                        state = armState.INTAKING_TO_STOWED;
                        elapsedTime.reset();
                        timeCount = 0;
                        arm.joint2.motor.setMotorEnable();
                    }
                    break;
                case TO_SCORING:
                    angle1 = scoreArray[timeCount][0];
                    angle2 = scoreArray[timeCount][1];
                    angle3 = scoreArray[timeCount][2];
                    angle4 = scoreArray[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - (scoreArray[timeCount][4]) >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= scoreArray.length) {
                        state = armState.SCORING;
                        timeCount = 0;
                    }
                    arm.intake.update();
                    break;
                case SCORING:
                    arm.intake.update();
                    angle4 = angle4 - gamepad2.left_trigger * 4;
                    angle4 = angle4 + gamepad2.right_trigger * 4;
                    if (gamepad2.left_bumper) {
                        state = armState.TO_INTAKING;
                        elapsedTime.reset();
                        timeCount = 0;
                    }
                    break;
                case TO_INTAKING:
                    angle1 = intakeArray[timeCount][0];
                    angle2 = intakeArray[timeCount][1];
                    angle3 = intakeArray[timeCount][2];
                    angle4 = intakeArray[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - intakeArray[timeCount][4] >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= intakeArray.length) {
                        state = armState.INTAKING;
                        timeCount = 0;
                        arm.joint2.motor.setMotorDisable();
                    }
                    arm.intake.update();
                    break;
                case INTAKING_TO_STOWED:
                    angle1 = stowFromIntake[timeCount][0];
                    angle2 = stowFromIntake[timeCount][1];
                    angle3 = stowFromIntake[timeCount][2];
                    angle4 = stowFromIntake[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - stowFromIntake[timeCount][4] >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= stowFromIntake.length) {
                        state = armState.STOWED;
                        timeCount = 0;
                        arm.disable();
                    }
                    break;
                case SCORING_TO_STOWED:
                    angle1 = stowFromScoring[timeCount][0];
                    angle2 = stowFromScoring[timeCount][1];
                    angle3 = stowFromScoring[timeCount][2];
                    angle4 = stowFromScoring[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - stowFromScoring[timeCount][4] >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= stowFromScoring.length) {
                        state = armState.STOWED;
                        timeCount = 0;
                        arm.disable();
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
                    arm.joint4.setRelativeAngle(angle4);
                }
            }
            arm.updateTelemetry();
            drivetrain.updateByGamepad();
            lift.updateByGamepad();
            telemetry.addData("Arm State: ", state);
            telemetry.addData("Main Loop Time: ", elapsedTime.time() - loopTime);
            telemetry.update();
        }
    }
}