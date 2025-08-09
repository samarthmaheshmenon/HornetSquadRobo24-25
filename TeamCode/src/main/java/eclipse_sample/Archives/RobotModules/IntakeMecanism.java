package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

public class IntakeMecanism {

    public DcMotor infeedLeft;
    public DcMotor infeedRight;
    public Servo foldoutHolderLeft;
    public Servo foldoutHolderRight;

    private LinearOpMode linearOpMode;

    public IntakeMecanism(LinearOpMode l) {
        linearOpMode = l;
        HardwareMap hardwareMap = linearOpMode.hardwareMap;
        infeedRight = hardwareMap.dcMotor.get(UniversalConstants.intakeMotorRight);
        infeedLeft = hardwareMap.dcMotor.get(UniversalConstants.intakeMotorLeft);

        infeedRight.setDirection(DcMotorSimple.Direction.FORWARD);
        infeedLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        infeedRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        infeedLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        foldoutHolderLeft = hardwareMap.servo.get(UniversalConstants.intakeFoldOutLeft);
        foldoutHolderRight = hardwareMap.servo.get(UniversalConstants.intakeFoldOutRight);
        holdFoldoutIntake();
    }

    public void holdFoldoutIntake() {
        foldoutHolderLeft.setPosition(UniversalConstants.foldoutHolderLeftStored);
        foldoutHolderRight.setPosition(UniversalConstants.foldoutHolderRightStored);
    }

    public void deployFoldoutIntake() {
        foldoutHolderLeft.setPosition(UniversalConstants.foldoutHolderLeftOpen);
        foldoutHolderRight.setPosition(UniversalConstants.foldoutHolderRightOpen);
    }

    public void updateAll() {
        updateByGamepad();
    }

    public void updateByGamepad() {
        setIntakePowers(linearOpMode.gamepad2.left_stick_y, linearOpMode.gamepad2.right_stick_y);
    }

    public void updateTelemetry() {
        linearOpMode.telemetry.addData("Intake_2018OLD Left", infeedLeft.getPower() > 0 ? "Infeeding" : infeedLeft.getPower() < 0 ? "Outfeeding" : "Not Moving");
        linearOpMode.telemetry.addData("Intake_2018OLD Right", infeedRight.getPower() > 0 ? "Infeeding" : infeedRight.getPower() < 0 ? "Outfeeding" : "Not Moving");
    }

    public double[] getVelocities() {
        return new double[]{((DcMotorEx) infeedLeft).getVelocity(AngleUnit.DEGREES), ((DcMotorEx) infeedRight).getVelocity(AngleUnit.DEGREES)};
    }

    double getAvgPosition() {
        return ((infeedLeft.getCurrentPosition() + infeedRight.getCurrentPosition()) / 2);
    }

    public void setPowerVelocity(double l, double r) {
        ((DcMotorEx) infeedLeft).setVelocity(l * 380, AngleUnit.DEGREES);
        ((DcMotorEx) infeedRight).setVelocity(r * 380, AngleUnit.DEGREES);
    }

    public void outtakeFully() {
        double targetAvg = getAvgPosition() - UniversalConstants.ticksPerMotorRotation * 1.5;
        setIntakePowers(-1);
        while (getAvgPosition() > targetAvg) {
            linearOpMode.idle();
        }
        stopIntake();
    }

    public void setIntakePowers(double p) {
        setIntakePowers(p, p);
    }

    public void setIntakePowersOverride(double p) {
        infeedLeft.setPower(p);
        infeedRight.setPower(p);
    }

    public void setIntakePowers(double left, double right) {
        double maxSpeed = .5;
        left = Range.clip(left, -maxSpeed, maxSpeed);
        right = Range.clip(right, -maxSpeed, maxSpeed);
        infeedLeft.setPower(left);
        infeedRight.setPower(right);
    }

    public void intake() {
        setIntakePowers(1);
    }

    public void outtake() {
        setIntakePowers(-1);
    }

    public void stopIntake() {
        setIntakePowers(0);
    }

}
