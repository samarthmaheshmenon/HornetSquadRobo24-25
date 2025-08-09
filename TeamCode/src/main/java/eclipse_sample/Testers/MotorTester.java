package eclipse_sample.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp (name = " Test da Motor")
public class MotorTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor one = hardwareMap.dcMotor.get("one");
        DcMotor two = hardwareMap.dcMotor.get("two");
        DcMotor three = hardwareMap.dcMotor.get("three");
        DcMotor four = hardwareMap.dcMotor.get("four");
        DcMotor five = hardwareMap.dcMotor.get("five");
        DcMotor six = hardwareMap.dcMotor.get("six");
        DcMotor seven = hardwareMap.dcMotor.get("seven");
        DcMotor eight = hardwareMap.dcMotor.get("eight");

        one.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        two.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        three.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        four.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        five.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        six.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        seven.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        eight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);



    }
}
