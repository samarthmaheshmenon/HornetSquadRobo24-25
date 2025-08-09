package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;
@Disabled
@TeleOp(name = "Arm Tester")
public class armTester extends LinearOpMode{
    enum armState{
        STOWED,DEPLOYING, INTAKING, TO_SCORING, SCORING, TO_INTAKING, TO_VERTICAL, TO_STOWED, TO_FAR_INTAKE, TO_CLOSE_INTAKE
    }
    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double angle1 = -21, angle2 = 142, angle3 = -22, angle4 = 76;
        double loopTime = 0;
        boolean farIntake = false;
        boolean closeIntake = false;
        double[][] deployArray = new double[][]{
                {-21, 130, 0, 45, 200},
                {-21, 90, 90, 90, 200},
                {-10, 90, 130, 100, 200},
                {0, 90, 170, 110, 200},
                {5, 100, 160, 115, 200},
                {10, 115, 150, 120, 200},
                {20, 120, 145, 130, 200},
                {35, 120, 180, 145, 200},
                {50, 120, 185, 145, 200},
                {55, 130, 185, 145, 200},
                {60, 140, 190, 145, 200},
                {70, 140, 190, 145, 200},
                {80, 145, 205, 150, 200},
                {95, 150, 220, 160, 200},
                {95, 170, 240, 160, 200},
                {100, 180, 260, 160, 200},
                {90, 190, 270, 160, 200}
        };
        double[][] scoreArray = new double[][]{
                {90,180,270,160,100},
                {90,170,270,160,100},
                {90,160,250,150,200},
                {90,150,230,140,200},
                {90,140,210,130,100},
                {90,130,190,120,100},
                {90,120,170,110,100},
                {90,110,150,100,100},
                {90,100,130,90,100},
                {90,95,110,90,100},
                {90,90,90,90,200},
                {93,80,80,75,200},
                {93,75,75,65,200},
                {95,70,70,55,200},
                {95,70,65,45,200},
                {95,70,60,35,300},
        };
        double[][] intakeArray = new double[][]{
                {95,70,40,-10,100},
                {95,70,40,-5,100},
                {95,70,40,0,100},
                {95,70,50,15,100},
                {95,70,60,35,100},
                {95,70,70,55,200},
                {93,80,80,75,200},
                {90,90,90,90,200},
                {90,93,100,90,200},
                {90,95,110,90,200},
                {90,100,130,90,200},
                {90,110,150,100,300},
                {90,120,170,110,300},
                {90,130,190,120,300},
                {90,140,210,130,300},
                {90,150,230,140,300},
                {90,160,250,150,300},
                {90,170,270,160,100},
                {90,180,270,160,100},
                {90,190,270,160,200}
        };
        double[][] farIntakeArray = new double[][]{
                {90, 190, 270, 160, 200},
                {95, 190, 265, 160, 200},
                {100, 190, 260, 160, 200},
                {105, 190, 255, 160, 200},
                {110, 190, 250, 160, 200}
        };
        double[][] stowingArray = new double[][]{};
        double currentTime = 0;
        int timeCount = 0;
        telemetry.addLine("Initialized");
        telemetry.update();
        armState state = armState.STOWED;
        waitForStart();
        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        while(opModeIsActive()) {
            loopTime = elapsedTime.time();
            switch(state){
                case STOWED:
                    if(gamepad2.x && gamepad2.y){
                        arm.joint1.setPower(.7);
                        arm.joint2.setPower(.7);
                        arm.joint3.setPower(.7);
                        arm.joint4.setPower(.7);
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
                    if(elapsedTime.time() - deployArray[timeCount][4] >= 0){
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if(timeCount >= deployArray.length){
                        state = armState.INTAKING;
                        timeCount = 0;
                    }
                    arm.intake.update();
                    break;
                case INTAKING:
                    angle1 -= gamepad2.left_stick_y * 4;
                    angle3 = 360 - angle1 - 10;
                    angle4 += gamepad2.right_stick_y * 3.5;
                    arm.intake.update();
                    if(gamepad2.right_bumper){
                        state = armState.TO_SCORING;
                    }
                    if(farIntake){
                        state = armState.TO_FAR_INTAKE;
                    }
                    if(closeIntake){
                        state = armState.TO_CLOSE_INTAKE;
                    }
                    break;
                case TO_SCORING:
                    angle1 = scoreArray[timeCount][0];
                    angle2 = scoreArray[timeCount][1];
                    angle3 = scoreArray[timeCount][2];
                    angle4 = scoreArray[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if(elapsedTime.time() - scoreArray[timeCount][4] >= 0){
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if(timeCount >= scoreArray.length){
                        state = armState.SCORING;
                        timeCount = 0;
                    }
                    arm.intake.update();
                    break;
                case SCORING:
                    angle4 -= gamepad2.right_stick_y * 3.5;
                    arm.intake.update();
                    if(gamepad2.left_bumper){
                        state = armState.TO_INTAKING;
                    }
                    break;
                case TO_INTAKING:
                    angle1 = intakeArray[timeCount][0];
                    angle2 = intakeArray[timeCount][1];
                    angle3 = intakeArray[timeCount][2];
                    angle4 = intakeArray[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if(elapsedTime.time() - intakeArray[timeCount][4] >= 0){
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if(timeCount >= intakeArray.length){
                        state = armState.INTAKING;
                        timeCount = 0;
                    }
                    arm.intake.update();
                    break;
                case TO_VERTICAL:
                    angle1 = 90;
                    angle2 = 90;
                    angle3 = 90;
                    angle4 = 90;
                    if(elapsedTime.time() % 100 == 0){
                        timeCount++;
                    }
                    if(timeCount > 30){
                        timeCount = 0;
                        state = armState.TO_STOWED;
                    }
                    break;
                case TO_STOWED:
                    angle1 = stowingArray[timeCount][0];
                    angle2 = stowingArray[timeCount][1];
                    angle3 = stowingArray[timeCount][2];
                    angle4 = stowingArray[timeCount][3];
                    if(elapsedTime.time() % 100 == 0){
                        timeCount++;
                    }
                    if(timeCount > 30){
                        timeCount = 0;
                        state = armState.STOWED;
                    }
                    break;
                case TO_FAR_INTAKE:
                    angle1 = farIntakeArray[timeCount][0];
                    angle2 = farIntakeArray[timeCount][1];
                    angle3 = farIntakeArray[timeCount][2];
                    //angle4 = farIntakeArray[timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if(elapsedTime.time() - farIntakeArray[timeCount][4] >= 0){
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if(timeCount >= farIntakeArray.length){
                        state = armState.INTAKING;
                        timeCount = 0;
                        farIntake = false;
                    }
                    arm.intake.update();
                    break;
                case TO_CLOSE_INTAKE:
                    angle1 = farIntakeArray[farIntakeArray.length - 1 - timeCount][0];
                    angle2 = farIntakeArray[farIntakeArray.length - 1 - timeCount][1];
                    angle3 = farIntakeArray[farIntakeArray.length - 1 - timeCount][2];
                    //angle4 = farIntakeArray[farIntakeArray.length - 1 - timeCount][3];
                    telemetry.addData("Array State: ", timeCount);
                    if(elapsedTime.time() - farIntakeArray[farIntakeArray.length - 1 - timeCount][4] >= 0){
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if(timeCount >= farIntakeArray.length){
                        state = armState.INTAKING;
                        timeCount = 0;
                        closeIntake = false;
                    }
                    arm.intake.update();
                    break;
            }

            if(!(state == armState.STOWED)){
                if(arm.joint1.convertToTicks(angle1) != arm.joint1.one.getTargetPosition()) {
                    arm.joint1.setAngle(angle1);
                }
                if(arm.joint2.convertToTicks(angle2) != arm.joint2.motor.getTargetPosition()) {
                    arm.joint2.setAngle(angle2);
                }
                if(arm.joint3.convertToTicks(angle3) != arm.joint3.motor.getTargetPosition()) {
                    arm.joint3.setAngle(angle3);
                }
                if(arm.joint4.convertToTicks(angle3) != arm.joint4.motor.getTargetPosition()) {
                    arm.joint4.setAngle(angle4);
                }
            }
            if(gamepad2.dpad_up){
                farIntake = true;
            }
            else if(gamepad2.dpad_down){
                closeIntake = true;
            }
            arm.updateTelemetry();
            drivetrain.updateByGamepad();
            telemetry.addData("Arm State: ", state);
            telemetry.addData("Main Loop Time: ", elapsedTime.time() - loopTime);
            telemetry.update();
        }
    }
}