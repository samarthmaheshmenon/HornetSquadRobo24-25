package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.TensorFlowLite;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;

@Disabled
@Autonomous(name = "Just Sift")
public class AutoJustSift extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        TensorFlowLite tensorFlowLite = new TensorFlowLite(this);
        //LiftModule liftModule = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Servo dispenser = this.hardwareMap.servo.get(UniConstTwo.dispenser);
        waitForStart();
        //liftModule.deploy();
        //drivetrain.pidTurn(.1, 90);
        double turnPower = .15;
        double movePower = .4;

        if (opModeIsActive()) {
            String Position = "Unknown";
            tensorFlowLite.activateTfod();
            ElapsedTime elapsedTime = new ElapsedTime();
            while (opModeIsActive() && Position == "Unknown") {
                tensorFlowLite.updateTensorFlowExp();
                Position = tensorFlowLite.getPosition();
                if (Position != "Unknown" || elapsedTime.time() > 6.0) {
                    break;
                }
            }
            tensorFlowLite.shutDownTfod();

            lift.moveToPosition(7950);
            drivetrain.pidTurn(turnPower, -40);
            drivetrain.moveBackwards(3, .1);
            drivetrain.pidTurn(turnPower, 0);
            drivetrain.moveBackwards(4, movePower);
            //liftModule.deploy();
            //drivetrain.pidTurn(.1, 90);


            switch (Position) {
                case "Left":
                    drivetrain.pidTurn(turnPower, 35);
                    drivetrain.moveBackwards(21.5, movePower);
                    drivetrain.moveForward(21.5, movePower);
                    drivetrain.pidTurn(turnPower, 0);
                    break;

                case "Right":
                    drivetrain.pidTurn(turnPower, -25);
                    drivetrain.moveBackwards(19, movePower);
                    drivetrain.moveForward(19, movePower);
                    drivetrain.pidTurn(turnPower, 0);
                    break;

                case "Center":
                default:
                    drivetrain.moveBackwards(18, movePower);
                    drivetrain.moveForward(18, movePower);
                    break;
            }
        }
    }
}
