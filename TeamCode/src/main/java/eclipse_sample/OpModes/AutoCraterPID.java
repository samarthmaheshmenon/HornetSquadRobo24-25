package eclipse_sample.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotModules.Drivetrain;
import org.firstinspires.ftc.teamcode.RobotModules.LiftModule;
import org.firstinspires.ftc.teamcode.RobotModules.TensorFlowLite;
import org.firstinspires.ftc.teamcode.RobotModules.UniConstTwo;


@Autonomous(name = "Crater Auto")
public class AutoCraterPID extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Servo dispenser = this.hardwareMap.servo.get(UniConstTwo.dispenser);
        TensorFlowLite tensorFlowLite = new TensorFlowLite(this);
        dispenser.setDirection(Servo.Direction.REVERSE);
        waitForStart();

        double turnPower = 1;
        double movePower = .8;

        if (opModeIsActive()) {
            String Position = "Unknown";
            tensorFlowLite.activateTfod();
            ElapsedTime elapsedTime = new ElapsedTime();
            while (opModeIsActive() && Position == "Unknown") {
                tensorFlowLite.updateTensorFlowExp();
                Position = tensorFlowLite.getPosition();
                if (Position != "Unknown" || elapsedTime.time() > 3.0) {
                    break;
                }
            }
            tensorFlowLite.shutDownTfod();


            lift.moveToPosition(7950);
            drivetrain.pidTurn(turnPower, -40);
            drivetrain.moveInches(-3, .1);
            drivetrain.pidTurn(turnPower, 0);
            drivetrain.moveInches(-4, movePower);
            lift.liftMotor.setTargetPosition(0);
            dispenser.setPosition(.6);

            switch (Position) {
                case "Left":
                    drivetrain.pidTurn(turnPower, 37);
                    drivetrain.moveInches(-26, movePower);
                    drivetrain.pidTurn(turnPower, 90);
                    drivetrain.reverseArcTurn(32, turnPower, 42);
                    drivetrain.moveInches(-36, movePower);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.turnFromPosition(turnPower, 5);
                    drivetrain.moveInches(65, movePower);
                    lift.setServo(1);
                    break;

                case "Right":
                    drivetrain.pidTurn(turnPower, -25);
                    drivetrain.moveInches(-22, movePower);
                    drivetrain.moveInches(8, movePower);
                    drivetrain.pidTurn(turnPower, 90);
                    drivetrain.moveInches(-25, movePower);
                    drivetrain.reverseArcTurn(41, 1, 42);
                    drivetrain.moveInches(-23, 1);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.turnFromPosition(turnPower, 5);
                    drivetrain.moveInches(65, movePower);
                    lift.setServo(1);
                    break;

                case "Center":
                default:
                    drivetrain.moveInches(-20, movePower);
                    drivetrain.moveInches(8, movePower);
                    drivetrain.pidTurn(turnPower, 89);
                    drivetrain.moveInches(-26, movePower);
                    drivetrain.reverseArcTurn(31, turnPower, 40);
                    drivetrain.moveInches(-14, movePower);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.turnFromPosition(turnPower, 5);
                    drivetrain.moveInches(62, movePower);
                    lift.setServo(1);
                    break;
            }

        }
    }
}
