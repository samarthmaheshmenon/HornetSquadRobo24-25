package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

public class RelicMechanism {
    public DcMotor spoolMotor;
    public Servo pincherServo, elbowServo;
    private LinearOpMode linearOpMode;
    private Controller controller1;
    private Controller controller2;
    int lastTargetPosition;
    double lastUpdateTime;

    public RelicMechanism(LinearOpMode l) {
        HardwareMap hardwareMap = l.hardwareMap;
        linearOpMode = l;
        controller1 = new Controller(l.gamepad1);
        controller2 = new Controller(l.gamepad2);
        spoolMotor = hardwareMap.dcMotor.get(UniversalConstants.relicSpoolMotor);
        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pincherServo = hardwareMap.servo.get(UniversalConstants.relicPincher);
        elbowServo = hardwareMap.servo.get(UniversalConstants.relicElbow);
        spoolMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        while (spoolMotor.getCurrentPosition() != 0 && l.opModeIsActive()) {
            l.idle();
        }
        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (l.opModeIsActive() && spoolMotor.getCurrentPosition() != 0) {
            l.idle();
        }
        spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spoolMotor.setTargetPosition(0);
        //storeServos();
    }

    public void storeServos() {
        elbowServo.setPosition(UniversalConstants.relicElbowStored);
        pincherServo.setPosition(UniversalConstants.relicPincherPinched);
    }

    public void closePinchServo() {
        pincherServo.setPosition(UniversalConstants.relicPincherPinched);
    }

    public void openPinchServo() {
        pincherServo.setPosition(UniversalConstants.relicPincherFullyOpen);
    }

    public void updateAll() {
        updateByGamepad();
    }

    public void swingElbowUp() {
        elbowServo.setPosition(UniversalConstants.relicElbowUp);
        pincherServo.setPosition(UniversalConstants.relicPincherFullyOpen);
    }

    public void swingElbowStayPinched() {
        elbowServo.setPosition(UniversalConstants.relicElbowUp);
        pincherServo.setPosition(UniversalConstants.relicPincherPinched);
    }

    public void swingAwayFromWall() {
        elbowServo.setPosition(UniversalConstants.relicElbowAwayFromWall);
    }

    public void updateByGamepad() {
        int spoolOffsetPosition = UniversalConstants.relicMotorLimit / 12;
        controller1.update();
        controller2.update();

        if (controller2.left_trigger > .5) {
            spoolMotor.setTargetPosition(
                    Range.clip(spoolMotor.getCurrentPosition() - spoolOffsetPosition,
                            0, UniversalConstants.relicMotorLimit));
            lastUpdateTime = linearOpMode.getRuntime();
        } else if (controller2.right_trigger > .5) {
            spoolMotor.setTargetPosition(
                    Range.clip(spoolMotor.getCurrentPosition() + spoolOffsetPosition,
                            0, UniversalConstants.relicMotorLimit));
            lastUpdateTime = linearOpMode.getRuntime();
        }

        if (!spoolMotor.isBusy() || (linearOpMode.getRuntime() - lastUpdateTime) > 2) {
            //Stall detection: timeout for relic motor at 2 seconds
            spoolMotor.setPower(0);
        } else {
            spoolMotor.setPower(1);
        }

        double servoMoveSpeed;
        double pincherMoveSpeed = .04;

        if(controller1.Y()){
            elbowServo.setPosition(.84);
        }
        if(elbowServo.getPosition() > .70){
            servoMoveSpeed = .01;
        } else {
            servoMoveSpeed = .02;
        }
        if (linearOpMode.gamepad2.dpad_down) {
            elbowServo.setPosition(
                    Range.clip(elbowServo.getPosition() + servoMoveSpeed,
                            UniversalConstants.relicElbowUp, UniversalConstants.relicElbowDown));
        } else if (linearOpMode.gamepad2.dpad_up) {
            elbowServo.setPosition(
                    Range.clip(elbowServo.getPosition() - servoMoveSpeed,
                            UniversalConstants.relicElbowUp, UniversalConstants.relicElbowDown));
        }

        if (linearOpMode.gamepad2.dpad_left) {
            pincherServo.setPosition(
                    Range.clip(pincherServo.getPosition() + pincherMoveSpeed,
                            UniversalConstants.relicPincherPinched, UniversalConstants.relicPincherFullyOpen));
        } else if (linearOpMode.gamepad2.dpad_right) {
            pincherServo.setPosition(Range.clip(pincherServo.getPosition() - pincherMoveSpeed,
                    UniversalConstants.relicPincherPinched, UniversalConstants.relicPincherFullyOpen));
        }

        if (linearOpMode.gamepad2.x) {
            storeServos();
        }

    }

    public void updateTelemetry() {
        linearOpMode.telemetry.addData("Position", spoolMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("Target", spoolMotor.getTargetPosition());
    }
}
