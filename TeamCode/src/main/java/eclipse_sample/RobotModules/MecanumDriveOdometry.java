package eclipse_sample.RobotModules;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDriveOdometry {
    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    private DcMotorEx leftFront, leftBack, rightFront, frontBack;
    private BNO055IMU imu;

    public MecanumDriveOdometry(LinearOpMode l, DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        linearOpMode = l;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = l.hardwareMap;

    }

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

}
