package eclipse_sample.Archives.RobotModules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Archives.AutonomousUtil;
import org.firstinspires.ftc.teamcode.Archives.UniversalConstants;

public class Robot {
    public LinearOpMode linearOpMode;
    public Telemetry telemetry;

    public boolean mecanumDriveTrainExists = true;
    public boolean intakeMechanismExists = true;
    public boolean slamDunkerExists = true;
    public boolean jewelSwatterExists = true;
    public boolean relicMechanismExists = true;
    public boolean vuforiaRelicRecoveryGetterExists = true;

    public JewelSwatter jewelSwatter;
    public MecanumDriveTrain driveTrain;
    public org.firstinspires.ftc.teamcode.Archives.RobotModules.SlamDunker slamDunker;
    public IntakeMecanism intakeMecanism;
    public VuforiaRelicRecoveryGetter vuforiaRelicRecoveryGetter;
    public RelicMechanism relicMecanism;

    public Robot(LinearOpMode l, boolean useVuforia, boolean useSensors, boolean storeSwatter, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        // OpModeManagerImpl opModeManager = (OpModeManagerImpl) l.internalOpModeServices;
        // this line of code will later help us automate the Autonomous to RobotTeleOp Transition!
        // https://github.com/NoahAndrews/FtcAutoTransitioner/blob/master/library/src/main/java/net/kno3/ftcautotransitioner/AutoTransitioner.java

        linearOpMode = l;
        telemetry = l.telemetry;
        l.gamepad1.setJoystickDeadzone(UniversalConstants.joystickDeadzone);
        l.gamepad2.setJoystickDeadzone(UniversalConstants.joystickDeadzone);
        vuforiaRelicRecoveryGetterExists = useVuforia;

        linearOpMode.telemetry.addLine("Before Mecanum");
        linearOpMode.telemetry.update();
        if (mecanumDriveTrainExists) {
            driveTrain = new MecanumDriveTrain(l, zeroPowerBehavior, useSensors);
        }
        linearOpMode.telemetry.addLine("After Mecanum");
        linearOpMode.telemetry.update();

        if (slamDunkerExists) {
            slamDunker = new SlamDunker(l);
        }
        if (intakeMechanismExists) {
            intakeMecanism = new IntakeMecanism(l);
        }

        if (jewelSwatterExists) {
            jewelSwatter = new JewelSwatter(l, storeSwatter);
        }

        if (relicMechanismExists) {
            relicMecanism = new RelicMechanism(l);
        }
        linearOpMode.telemetry.update();
        if (vuforiaRelicRecoveryGetterExists) {
            vuforiaRelicRecoveryGetter = new VuforiaRelicRecoveryGetter(l);
        }
    }

    public void updateTelemetry() {
        if (mecanumDriveTrainExists) {
            driveTrain.updateTelemetry();
        }
        if (slamDunkerExists) {
            slamDunker.updateTelemetry();
        }
        if (intakeMechanismExists) {
            intakeMecanism.updateTelemetry();
        }
        if (jewelSwatterExists) {
            jewelSwatter.updateTelemetry();
        }
        if (vuforiaRelicRecoveryGetterExists) {
            vuforiaRelicRecoveryGetter.updateTelemetry();
        }
    }

    public void updateAll() {
        telemetry.clearAll();

        if (mecanumDriveTrainExists) {
            driveTrain.updateAll();
        }
        if (slamDunkerExists) {
            slamDunker.updateAll();
        }
        if (intakeMechanismExists) {
            intakeMecanism.updateAll();
        }
        if (jewelSwatterExists) {
            jewelSwatter.updateAll();
        }
        if (relicMechanismExists) {
            relicMecanism.updateAll();
        }
        telemetry.update();
    }

    public void scoreBlock() {
        scoreBlock(.5);
    }

    public void scoreBlock(double pow) {
        intakeMecanism.outtake();
        driveTrain.moveToInches(6, pow);
        driveTrain.moveToInches(-4, pow);
        driveTrain.moveToInches(6, pow);
        driveTrain.moveToInches(-10, pow);
    }

    public void closeOpModeIfOver() {
        if (!linearOpMode.opModeIsActive()) {
            linearOpMode.stop();
        }
    }

    public void moveToLocateVuMark(AutonomousUtil.AllianceColor color, int timeout) {
        int sign;
        if (color == AutonomousUtil.AllianceColor.Red) {
            sign = 1;
        } else {
            sign = -1;
        }
        RelicRecoveryVuMark vuMark;
        driveTrain.moveToInches(25, .25);
        alignToWithinVuforia(65 * sign);
        vuMark = vuforiaRelicRecoveryGetter.getPattern();

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            alignToWithinVuforia(45 * sign);
            vuMark = vuforiaRelicRecoveryGetter.getPattern();

            if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                alignToWithinVuforia(25 * sign);
            }
        }
        vuMark = vuforiaRelicRecoveryGetter.getPattern();
        double stTime = linearOpMode.getRuntime();
        while (opModeIsActive() && linearOpMode.getRuntime() - stTime < Range.clip(timeout, 0, 10) && vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuMark = vuforiaRelicRecoveryGetter.getPattern();
        }
    }

    public void alignToWithinVuforia(double angle) {
        driveTrain.alignToWithinVuforia(vuforiaRelicRecoveryGetter, angle);
    }

    public void turnToAngleWithVuforiaExit(double angle) {
        double turnPower;
        double distL = ((angle - (driveTrain.getHeading())) + 360) % 360;
        double distR = (((driveTrain.getHeading() - angle)) + 360) % 360;
        telemetry.addData("distanceL", distL);
        telemetry.addData("distanceR", distR);
        telemetry.update();
        if (distL < distR) {
            while (opModeIsActive() && distL > driveTrain.angleThreshold && vuforiaRelicRecoveryGetter.getPattern() == RelicRecoveryVuMark.UNKNOWN) {
                distL = ((angle - (driveTrain.getHeading())) + 360) % 360;
                turnPower = driveTrain.getTurnPower();
                driveTrain.setAll(-turnPower, turnPower);
            }
        } else {
            while (opModeIsActive() && distR > driveTrain.angleThreshold && vuforiaRelicRecoveryGetter.getPattern() == RelicRecoveryVuMark.UNKNOWN) {
                distR = (((driveTrain.getHeading()) - angle) + 360) % 360;
                turnPower = driveTrain.getTurnPower();
                driveTrain.setAll(turnPower, -turnPower);
            }
        }
        // Explanation of this math can be found at: https://www.desmos.com/calculator/tzijlpu8qc
    }

    public boolean opModeIsActive() {
        return linearOpMode.opModeIsActive();
    }

    public void addToTelemetry(String k, String s) {
        linearOpMode.telemetry.addData(k, s);
    }

    public void addToTelemetry(String s) {
        linearOpMode.telemetry.addLine(s);
    }

    public void addAndUpdateTelemetry(String s) {
        addToTelemetry(s);
        linearOpMode.telemetry.update();
    }

    public void addAndUpdateTelemetry(String k, String s) {
        addToTelemetry(k, s);
        linearOpMode.telemetry.update();
    }


    @Deprecated
    public void strafeToJewelSensedDistance(double power, double distance, double targetHeading, boolean park) {
        strafeToJewelSensedDistance(power, distance, targetHeading, DistanceUnit.CM, park);
    }

    @Deprecated
    public void strafeToJewelSensedDistance(double power, double distance, DistanceUnit distanceUnit, boolean park) {
        strafeToJewelSensedDistance(power, distance, driveTrain.getHeading(), distanceUnit, park);
    }

    @Deprecated
    public void strafeToJewelSensedDistance(double power, double distance, boolean park) {
        strafeToJewelSensedDistance(power, distance, driveTrain.getHeading(), DistanceUnit.CM, park);
    }

    @Deprecated
    public void strafeToJewelSensedDistance(double power, double distance) {
        strafeToJewelSensedDistance(power, distance, driveTrain.getHeading(), DistanceUnit.CM, true);
    }

    @Deprecated
    public void strafeToJewelSensedDistance(double power, double targetDistance, double targetHeading, DistanceUnit distanceUnit, boolean park) {
        jewelSwatter.moveJewelToForwards();
        double dist = jewelSwatter.sensorDistance.getDistance(distanceUnit);
        double error = 100;
        while (Double.isNaN(dist) || error > 1) {
            dist = jewelSwatter.sensorDistance.getDistance(distanceUnit);
            if (Double.isNaN(dist)) {
                error = -100;
            } else {
                error = targetDistance - dist;
            }
            if (targetDistance - dist < 1.5 && !park) {
                return;
            }
            driveTrain.assistedStrafe(Math.abs(power) * Math.signum(error), targetHeading);
            telemetry.addData("Distance", dist);
            telemetry.update();
        }
        if (park) {
            driveTrain.park();
        }
    }

}
