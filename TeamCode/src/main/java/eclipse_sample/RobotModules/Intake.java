package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    private LinearOpMode linearOpMode;
    private Telemetry telemetry;
    public VEX393Servo leftServo;
    public VEX393Servo rightServo;
    //public CRServo intake;

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    public Intake(LinearOpMode l) {
        linearOpMode = l;
        telemetry = l.telemetry;
        HardwareMap hardwareMap = l.hardwareMap;
        //intake = hardwareMap.crservo.get(UniConstTwo.intake);
        //intake.setDirection(DcMotorSimple.Direction.REVERSE);
        leftServo = new VEX393Servo(hardwareMap.crservo.get(UniConstTwo.leftIntake), 1, 1);
        rightServo = new VEX393Servo(hardwareMap.crservo.get(UniConstTwo.rightIntake),1,1);
        status("Initialized Globals!");
        leftServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    /**
     *  This method sets the powers of the motors in the intake direction
     */
    public void intaker() {
        leftServo.setPower(.7);
        rightServo.setPower(.7);
    }

    /**
     * This method sets the powers of the motors in the outtake direction
     */
    public void outtaker() {
        leftServo.setPower(-.7);
        rightServo.setPower(-.7);;
    }

    public void stopper() {
        leftServo.setPower(0);
        rightServo.setPower(0);;
    }

    public void update() {
        if (linearOpMode.gamepad2.right_trigger != 0 || linearOpMode.gamepad2.left_trigger != 0) {
            if (linearOpMode.gamepad2.left_trigger > 0) {
                intaker();
            } else if (linearOpMode.gamepad2.right_trigger > 0) {
                outtaker();
            }

        } else {
            stopper();
        }
    }
}
