package eclipse_sample.Archives.DemoCode;/*package org.firstinspires.ftc.teamcode.DemoCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.UniversalConstants;

@Disabled
@Autonomous(name = "ImuSensorDemo")
public class ImuSensorDemo extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor leftFront = hardwareMap.dcMotor.get(UniversalConstants.leftFrontDrive);
        DcMotor leftBack = hardwareMap.dcMotor.get(UniversalConstants.leftBackDrive);
        DcMotor rightFront = hardwareMap.dcMotor.get(UniversalConstants.rightFrontDrive);
        DcMotor rightBack = hardwareMap.dcMotor.get(UniversalConstants.rightBackDrive);

        SixWheelDrive drive = new SixWheelDrive(ImuSensorDemo.this, leftFront, leftBack, rightFront, rightBack);
        waitForStart();
        drive.turnExact(.25, 90);
        Thread.sleep(1000);
        drive.turnExact(.25, -90);
        Thread.sleep(1000);
        drive.turnRelative(.25, 90);
    }

}
*/