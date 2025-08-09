package eclipse_sample.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PRobot {
    public Drivetrain drivetrain;
    public  LiftModule liftModule;
    public  SimpleScoringArm scoringArm;
    private  LinearOpMode linearOpMode;
    private Telemetry telemetry;
    private boolean drivetrainExists = true;
    private boolean liftExists = true;
    private boolean armExists = true;


    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    // creates a robot object with all necessary modules of the robot
    public PRobot(LinearOpMode l){
        linearOpMode = l;
        telemetry = linearOpMode.telemetry;
        if(drivetrainExists){
            drivetrain = new Drivetrain(linearOpMode, DcMotor.ZeroPowerBehavior.FLOAT);
        }
        if(liftExists){
            liftModule = new LiftModule(linearOpMode, DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if(armExists){
            scoringArm = new SimpleScoringArm(linearOpMode, DcMotor.ZeroPowerBehavior.BRAKE);
        }
        status("Robot Globals Set!");
    }


    // teleOp method for all robot controls
    public  void  updateAll(){
        if(drivetrainExists){
            drivetrain.updateAll();
        }
        if(liftExists){
            liftModule.updateAll();
        }
        if(armExists){
            scoringArm.updateAll();
        }
    }

    public void initialize(){
        scoringArm.resetAngles();
    }
}
