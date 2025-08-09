package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

import java.util.concurrent.TimeUnit;

public class SlamDunker {
    public DcMotor dunkMotor;
    public Servo stopperServo;
    private LinearOpMode linearOpMode;
    private Controller controller2;

    public SlamDunker(LinearOpMode l) {
        HardwareMap hardwareMap = l.hardwareMap;
        linearOpMode = l;
        controller2 = new Controller(l.gamepad2);
        dunkMotor = hardwareMap.dcMotor.get(UniversalConstants.slamDunker);
        dunkMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dunkMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        stopperServo = hardwareMap.servo.get(UniversalConstants.dunkerServo);
        engageServo();
        while (dunkMotor.getCurrentPosition() != 0 && l.opModeIsActive()) {
            l.idle();
        }
        dunkMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void updateAll() {
        updateByGamepad();
    }

    public void updateByGamepad() {
        int dunkOffsetForButton = 35;
        controller2.update();
        if (controller2.A()) {
            dunkNoWait();
        } else if (controller2.Y()) {
            retractDunkNoWait();
        } else if (controller2.leftBumper()) {
            dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed);
            dunkMotor.setTargetPosition(dunkMotor.getTargetPosition() + dunkOffsetForButton);
        } else if (controller2.rightBumper()) {
            dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed);
            dunkMotor.setTargetPosition(Range.clip(dunkMotor.getTargetPosition() - dunkOffsetForButton, 0, 10000));
        } else if (controller2.right_trigger > .25 && controller2.left_trigger > .25) {
            dunkMotor.setPower(UniversalConstants.dunkGlyphsSpeed);
            dunkMotor.setTargetPosition((dunkMotor.getTargetPosition() - dunkOffsetForButton));
        }

        //  Running the motor with voltage to keep it in place isn't required, as we already set the ZeroPowerBehavior to BRAKE.
        if (!dunkMotor.isBusy() && dunkMotor.getTargetPosition() == 0) {
            dunkMotor.setPower(0);
        }

    }

    public void updateTelemetry() {
        linearOpMode.telemetry.addData("Position", dunkMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("Target", dunkMotor.getTargetPosition());
    }

    public void engageServo() {
        stopperServo.setPosition(UniversalConstants.stopperServoEngaged);
    }

    public void disengageServo() {
        stopperServo.setPosition(UniversalConstants.stopperServoDown);
    }

    public void score() throws InterruptedException {
        dunk();
        Thread.sleep(50);
        retractDunkNoWait();
    }

    public boolean dunkSlowWithAutoStop() {
        engageServo();
        ElapsedTime time = new ElapsedTime();
        time.startTime();
        setMotorToApproach(UniversalConstants.dunkGlyphsPosition, UniversalConstants.dunkGlyphsSpeed * .5);
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
            if (time.time(TimeUnit.SECONDS) > 2.5) {
                retractDunkNoWait();
                return false;
            }
        }
        return true;
    }

    public void dunk() {
        dunkNoWait();
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
        }
    }

    public void dunkSlow() throws InterruptedException {
        engageServo();
        setMotorToApproach(UniversalConstants.dunkGlyphsPosition / 3, UniversalConstants.dunkGlyphsSpeed / 3);
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
        }
        Thread.sleep(100);
        setMotorToApproach(UniversalConstants.dunkGlyphsPosition * 2 / 3, UniversalConstants.dunkGlyphsSpeed);
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
        }
        Thread.sleep(100);
        setMotorToApproach(UniversalConstants.dunkGlyphsPosition, UniversalConstants.dunkGlyphsSpeed);
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
        }
        disengageServo();
    }

    public void dropTheDunk() {
        dropTheDunk(dunkMotor.getCurrentPosition());
    }

    public void dropTheDunk(int startingPosition) {
        dunkMotor.setTargetPosition(startingPosition - UniversalConstants.dunkGlyphsPosition / 2);
        dunkMotor.setPower(.5);
    }

    public void dunkNoWait() {
        engageServo();
        setMotorToApproach(UniversalConstants.dunkGlyphsPosition, UniversalConstants.dunkGlyphsSpeed / 2.5);
    }

    public void retract() {
        retractDunkNoWait();
        while (dunkMotor.isBusy()) {
            linearOpMode.idle();
        }
        dunkMotor.setPower(0);
    }

    public void retractDunkNoWait() {
        setMotorToApproach(0, UniversalConstants.dunkGlyphsPosition / 3);
        disengageServo();
    }

    private void setMotorToApproach(int target, double power) {
        dunkMotor.setTargetPosition(target);
        if (dunkMotor.getCurrentPosition() > target) {
            dunkMotor.setPower(-power);
        } else {
            dunkMotor.setPower(power);
        }
    }
}
