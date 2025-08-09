package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.SimpleScoringArm;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@TeleOp(name = "Arm Demo")
public class Wave extends LinearOpMode {
    enum armState {
        STOWED, TO_VERTICAL, VERTICAL, TO_STOWED
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SimpleScoringArm arm = new SimpleScoringArm(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        double angle1 = -21, angle2 = 162, angle3 = -166, angle4 = 137;
        double loopTime = 0;
        boolean hold2 = true;
        Servo servo = this.hardwareMap.servo.get(UniConstTwo.dispenser);
        double[][] stowToVertical = new double[][]{
                {30,60,-30,0,5000},
                {-20,160,-164,136,5000}
        };
        double[][] stowingArray = new double[][]{};
        double currentTime = 0;
        int timeCount = 0;
        telemetry.addLine("Initialized");
        telemetry.update();
        armState state = armState.STOWED;
        waitForStart();
        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        while (opModeIsActive()) {
            servo.setPosition(.25);
            loopTime = elapsedTime.time();
            switch (state) {
                case STOWED:
                    if (gamepad2.x && gamepad2.y) {
                        elapsedTime.reset();
                        arm.joint1.setPower(1);
                        arm.joint2.setPower(1);
                        arm.joint3.setPower(1);
                        arm.joint4.setPower(1);
                        angle1 = stowToVertical[timeCount][0];
                        angle2 = stowToVertical[timeCount][1];
                        angle3 = stowToVertical[timeCount][2];
                        angle4 = stowToVertical[timeCount][3];
                        state = armState.TO_VERTICAL;
                        elapsedTime.reset();
                    }
                    break;
                case TO_VERTICAL:
                    angle1 = stowToVertical[timeCount][0];
                    angle2 = stowToVertical[timeCount][1];
                    angle3 = stowToVertical[timeCount][2];
                    angle4 = stowToVertical[timeCount][3];
                    //telemetry.addData("Difference: ", elapsedTime.time() - deployArray[timeCount][4]);
                    telemetry.addData("Array State: ", timeCount);
                    if (elapsedTime.time() - stowToVertical[timeCount][4] >= 0) {
                        timeCount++;
                        elapsedTime.reset();
                    }
                    if (timeCount >= stowToVertical.length) {
                        state = armState.STOWED;
                        arm.disable();
                        timeCount = 0;
                    }
                    arm.intake.update();
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
            telemetry.addData("Arm State: ", state);
            telemetry.addData("Main Loop Time: ", elapsedTime.time() - loopTime);
            telemetry.update();
        }
    }
}