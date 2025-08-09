package eclipse_sample.Archives.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Archives.Controller;
import org.firstinspires.ftc.teamcode.Archives.RobotModules.Robot;

@Disabled
@TeleOp(name = "Module Tester")
public class ModuleTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        String[] modules = {"Drive Train", "Intake_2018OLD", "Slam Dunk", "Jewel Swatter", "Full Robot"};
        int currentModule = 0;
        Robot robot = new Robot(this, false, false, true, DcMotor.ZeroPowerBehavior.FLOAT);
        Controller lastGamepad1 = new Controller(gamepad1);
        Controller lastGamepad2 = new Controller(gamepad2);

        while (!isStopRequested() && !isStarted()) {
            if (lastGamepad1.AOnce() || lastGamepad2.AOnce()) {
                currentModule = (currentModule + 1 + modules.length) % modules.length;
            } else if (lastGamepad1.YOnce() || lastGamepad2.YOnce()) {
                currentModule = (currentModule - 1 + modules.length) % modules.length;
            }
            telemetry.addData("Module", modules[currentModule]);
            telemetry.update();
            lastGamepad1.update();
            lastGamepad2.update();
        }

        while (opModeIsActive()) {
            switch (currentModule) {
                case 0:
                    robot.driveTrain.updateAll();
                    break;
                case 1:
                    robot.intakeMecanism.updateAll();
                    break;
                case 2:
                    robot.slamDunker.updateAll();
                    break;
                case 3:
                    robot.jewelSwatter.updateAll();
                    break;
                case 4:
                default:
                    robot.updateAll();
                    break;
            }
            while (!isStopRequested() && !isStarted()) {
                if (lastGamepad1.AOnce()) {
                    currentModule = (currentModule + 1) % modules.length;
                } else if (lastGamepad1.YOnce()) {
                    currentModule = (currentModule - 1) % modules.length;
                }
                telemetry.addData("Module", modules[currentModule]);
                telemetry.update();
                lastGamepad1.update();
            }
        }
    }
}
