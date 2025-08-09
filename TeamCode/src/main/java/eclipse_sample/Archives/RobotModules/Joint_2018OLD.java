package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Joint_2018OLD {
    public LinearOpMode linearOpMode;
    public Telemetry telemetry;
    public Servo joint;

    public Joint_2018OLD(LinearOpMode l, double initAngle, Servo jointServo) {
        linearOpMode = l;
        telemetry = l.telemetry;
        joint = jointServo;
        joint.setPosition(initAngle);
    }

    public void moveToPosition(double val) {
        joint.setPosition(val);
    }

    public double getPosition () {
        return joint.getPosition();
    }

    public void setDirection(int direction) {
        if (direction < 0)
            joint.setDirection(Servo.Direction.REVERSE);
        else if (direction > 0)
            joint.setDirection(Servo.Direction.FORWARD);
    }
    public void resetPosition() {
        while(linearOpMode.opModeIsActive() && getPosition()!=0) {
            moveToPosition(0);
        }
    }
}
