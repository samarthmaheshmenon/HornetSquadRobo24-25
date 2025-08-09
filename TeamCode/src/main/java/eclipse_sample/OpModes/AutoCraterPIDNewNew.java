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
@Autonomous(name = "Crater Auto II")
public class AutoCraterPIDNewNew extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain drivetrain = new Drivetrain(this, DcMotor.ZeroPowerBehavior.BRAKE);
        LiftModule lift = new LiftModule(this, DcMotor.ZeroPowerBehavior.BRAKE);
        Servo dispenser = this.hardwareMap.servo.get(UniConstTwo.dispenser);
        TensorFlowLite tensorFlowLite = new TensorFlowLite(this);
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

            if (Position == "Right"){
                lift.moveToPosition(7950);
                drivetrain.pidTurn(turnPower,-23);
                lift.liftMotor.setTargetPosition(0);
            }
            else {
                lift.moveToPosition(7950);
                drivetrain.pidTurn(turnPower, -40);
                drivetrain.moveInches(-3, .1);
                drivetrain.pidTurn(turnPower, 0);
                drivetrain.moveInches(-4, movePower);
                dispenser.setPosition(.2);
                lift.liftMotor.setTargetPosition(0);
            }
            switch (Position) {
                case "Left":
                    drivetrain.pidTurn(turnPower,37);
                    drivetrain.moveInches(-26,movePower);
                    drivetrain.pidTurn(turnPower,90);
                    drivetrain.reverseArcTurn(28,turnPower,42);
                    drivetrain.moveInches(-36,movePower);
                    dispenser.setPosition(.6);
                    Thread.sleep(250);
                    dispenser.setPosition(.34);
                    drivetrain.turnFromPosition(turnPower,5);
                    drivetrain.moveInches(65,movePower);
                    lift.setServo(1);
                    break;

                case "Right":
                    drivetrain.pidTurn(turnPower,-25);
                    drivetrain.moveInches(-24,movePower);
                    drivetrain.moveInches(10,movePower);
                    drivetrain.pidTurn(turnPower,80);
                    drivetrain.moveInches(-15,movePower);
                    drivetrain.reverseArcTurn(40,1,42);
                    drivetrain.moveInches(-23,1);
                    dispenser.setPosition(.6);
                    Thread.sleep(250);
                    dispenser.setPosition(.34);
                    drivetrain.turnFromPosition(turnPower,5);
                    drivetrain.moveInches(65,movePower);
                    lift.setServo(1);
                    break;

                case "Center":
                default:
                    drivetrain.moveInches(-20, movePower);
                    drivetrain.moveInches(10, movePower);
                    drivetrain.pidTurn(turnPower, 89);
                    drivetrain.moveInches(-26, movePower);
                    drivetrain.reverseArcTurn(32, turnPower, 40);
                    drivetrain.moveInches(-12,movePower);
                    dispenser.setPosition(.6);
                    Thread.sleep(250);
                    dispenser.setPosition(.34);
                    drivetrain.turnFromPosition(turnPower,5);
                    drivetrain.moveInches(64,movePower);
                    lift.setServo(1);
                    break;
            }

        }
    }
}
