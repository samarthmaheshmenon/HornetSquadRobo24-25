package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class turretControl {
    private DcMotorEx GunOne, GunTwo; //103 ticks per rev
    private DcMotorEx turretRotor; // 1120
    private DcMotorEx panAdjust;
    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    public TensorFlowLite tensorFlowLite;


    private ControlMode controlMode;

    enum ControlMode {
        AUTO_MODE, MANUAL_MODE
    }

    public turretControl(LinearOpMode l) {
        linearOpMode = l;
        telemetry = linearOpMode.telemetry;
        HardwareMap hardwareMap = l.hardwareMap;
        GunOne = (DcMotorEx) hardwareMap.dcMotor.get("G1");
        GunTwo = (DcMotorEx) hardwareMap.dcMotor.get("G2");
        turretRotor = (DcMotorEx) hardwareMap.dcMotor.get("TR");
        panAdjust = (DcMotorEx) hardwareMap.dcMotor.get("PA");
        tensorFlowLite = new TensorFlowLite(linearOpMode);


        //Setting end motor behavior for turret motors
        GunOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        GunTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretRotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        panAdjust.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Setting Motor Directions
        GunOne.setDirection(DcMotorSimple.Direction.FORWARD);
        GunTwo.setDirection(DcMotorSimple.Direction.REVERSE);
        turretRotor.setDirection(DcMotorSimple.Direction.FORWARD);
        panAdjust.setDirection(DcMotorSimple.Direction.FORWARD);

        GunOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        GunTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        panAdjust.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tensorFlowLite.activateTfod();
    }


    public double computeRPSOne(double runtime, double encoderStart) {
        double tics = GunOne.getCurrentPosition() - encoderStart;
        double rotations = tics / 1120;
        double currentRPS = rotations / runtime;
        return currentRPS;
    }

    public double computeRPSTwo(double runtime, double encoderStart) {
        double tics = GunOne.getCurrentPosition() - encoderStart;
        double rotations = tics / 1120;
        double currentRPS = rotations / runtime;
        return currentRPS;
    }

    public void updateByGamepad() {
        double TurretControl = linearOpMode.gamepad1.left_stick_x;
        double PanControl = linearOpMode.gamepad1.right_stick_y;
        if (linearOpMode.gamepad1.a) {
            controlMode = ControlMode.AUTO_MODE;
        }
        if (linearOpMode.gamepad1.b){
            controlMode = ControlMode.MANUAL_MODE;
        }
        panAdjust.setPower(PanControl);

        switch (controlMode){
            case AUTO_MODE:
                break;
            case MANUAL_MODE:
                turretRotor.setPower(TurretControl);
                break;

        }
    }

    public double computeTurretAlignment(){
        double error = tensorFlowLite.getAzimuth();
        double ticsToLockon = (1120.0/360.0) * error;
        return 1; //Note to Self: Temporary
    }

}
