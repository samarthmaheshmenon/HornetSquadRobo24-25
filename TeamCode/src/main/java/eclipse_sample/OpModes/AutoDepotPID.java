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

@Autonomous(name = "Depot Auto")
public class AutoDepotPID extends LinearOpMode {
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
            


            switch (Position) {
                case "Right":
                    drivetrain.pidTurn(turnPower,-25);
                    drivetrain.moveInches(-38,movePower);
                    drivetrain.pidTurn(turnPower,25);
                    drivetrain.moveInches(-14.5,movePower);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.pidTurn(turnPower,90);
                    drivetrain.moveInches(-28.5,movePower);
                    drivetrain.pidTurn(turnPower,125);
                    dispenser.setPosition(.9);
                    drivetrain.moveInches(-66, movePower);


                    break;

                case "Center":
                    drivetrain.pidTurn(turnPower,5);
                    drivetrain.moveInches(-46, movePower);
                    drivetrain.turnFromPosition(turnPower,-10);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.turnFromPosition(turnPower,100);
                    drivetrain.moveInches(-15, movePower);
                    drivetrain.pidTurn(turnPower,115);
                    drivetrain.moveInches(-7, movePower);
                    drivetrain.pidTurn(turnPower,125);
                    lift.setServo(1);
                    drivetrain.moveInches(-60,movePower);
                    break;
                case "Left":
                default:
                    drivetrain.pidTurn(turnPower, 35);
                    drivetrain.moveInches(-43,movePower);
                    drivetrain.pidTurn(turnPower,-40);
                    drivetrain.moveInches(-8,movePower);
                    dispenser.setPosition(.9);
                    Thread.sleep(550);
                    dispenser.setPosition(.6);
                    drivetrain.pidTurn(turnPower,-50);
                    lift.setServo(1);
                    drivetrain.moveInches(60,1);
                    break;
            }
            dispenser.setPosition(.6);
        }
    }
}
