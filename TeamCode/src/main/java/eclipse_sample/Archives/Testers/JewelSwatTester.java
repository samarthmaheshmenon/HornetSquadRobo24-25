package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.JewelSwatter;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

@Disabled
@Autonomous(name = "Jewel Swat Test")
public class JewelSwatTester extends LinearOpMode {
    JewelSwatter jewelSwatter;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, true, true, true, DcMotor.ZeroPowerBehavior.BRAKE);
        AutonomousUtil.Settings settings = AutonomousUtil.getStartingSettings(this, AutonomousUtil.AllianceColor.Red, robot);
        double t = getRuntime();
        robot.jewelSwatter.removeJewelOfColor(settings.allianceColor);
        t = getRuntime() - t;
        telemetry.addData("Time", t);
        robot.jewelSwatter.waitForServoToMove(UniversalConstants.jewelServoSleepTimeLong);
        while (opModeIsActive()) {
            telemetry.addData("Time", t);
            telemetry.update();
            idle();
        }
    }

    public void updateTelemetry() {
        jewelSwatter.updateTelemetry();
        telemetry.update();
    }
}
