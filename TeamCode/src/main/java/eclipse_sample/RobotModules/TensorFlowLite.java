package eclipse_sample.RobotModules;


import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Tensor Flow object should be used for autonomous and must be used with phones mounted
 * in a Horizontal position with rear camera.
 */
public class TensorFlowLite {
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private Telemetry telemetry;
    private LinearOpMode linearOpMode;
    private HardwareMap hardwareMap;
    private String position;

    public void status(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }

    // This constructor creates all the properties needed for Tensor flow to work
    public TensorFlowLite(LinearOpMode l) {
        linearOpMode = l;
        telemetry = linearOpMode.telemetry;
        hardwareMap = l.hardwareMap;
        position = "Unknown";

        initVuforiaWebCam();

        status("Vuforia has been initialized.");

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
            status("Tensor Flow has been initialized.");
        } else {
            status("This device does not support Tensor Flow.");
        }

    }

    public void activateTfod() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void shutDownTfod() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }


    public void updateTensorFlow() {
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // the following code was taken form FTC sample code and partially modified for our robot's purposes
                // It is alright if not understood.
                if (updatedRecognitions.size() == 3) {
                    //x and y position values for minerals
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();

                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            position = "Left";
                            telemetry.addData("Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            position = "Right";
                            telemetry.addData("Position", "Right");
                        } else {
                            position = "Center";
                            telemetry.addData("Position", "Center");
                        }
                    }
                }
                telemetry.addData("Position: ", position);
                telemetry.update();
            }
        }

    }

    public double getAzimuth(){
        double angle = 0;
        if (tfod != null){
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if(updatedRecognitions != null){
                    if (updatedRecognitions.get(0).getLabel().equals(LABEL_GOLD_MINERAL)) {
                        angle = updatedRecognitions.get(0).estimateAngleToObject(AngleUnit.DEGREES);
                    }

            }
        }
        return angle;
    }

    /**
     * This method is used for mineral detection and sampling identification without interference
     * background minerals or objects. Sets position variable to identified position and
     * prints to telemetry.
     */
    public void updateTensorFlowExp() {
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // the following code was taken form FTC sample code and partially modified for our robot's purposes
                // It is alright if not understood.
                if (updatedRecognitions.size() >= 3) {
                    //x and y position values for minerals
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    int goldMineralY = -1;
                    int silverMineral1Y = -1;
                    int silverMineral2Y = -1;
                    /* Creates an array list of x and y values for all minerals*/
                    ArrayList<Integer> goldXvals = new ArrayList<Integer>();
                    ArrayList<Integer> goldYvals = new ArrayList<Integer>();
                    ArrayList<Integer> silverXvals = new ArrayList<Integer>();
                    ArrayList<Integer> silverYvals = new ArrayList<Integer>();

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldXvals.add((int) recognition.getLeft());
                            goldYvals.add((int) recognition.getBottom());

                        } else {
                            silverXvals.add((int) recognition.getLeft());
                            silverYvals.add((int) recognition.getBottom());
                        }
                    }
                    int indexToRemove = 0;
                    for (int i = 0; i < goldYvals.size(); i++) {
                        if (goldMineralY < goldYvals.get(i)) {
                            goldMineralY = goldYvals.get(i);
                            goldMineralX = goldXvals.get(i);
                        }
                    }

                    for (int i = 0; i < silverYvals.size(); i++) {
                        if (silverMineral1Y < silverYvals.get(i)) {
                            silverMineral1Y = silverYvals.get(i);
                            silverMineral1X = silverXvals.get(i);
                            indexToRemove = i;
                        }
                    }
                    if (silverYvals.size() > 0) {
                        silverXvals.remove(indexToRemove);
                        silverYvals.remove(indexToRemove);
                    }
                    for (int i = 0; i < silverYvals.size(); i++) {
                        if (silverMineral2Y < silverYvals.get(i)) {
                            silverMineral2Y = silverYvals.get(i);
                            silverMineral2X = silverXvals.get(i);
                        }
                    }
                    // sets a string variable of position based off of coordinate values relative to each other.
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            position = "Left";
                            telemetry.addData("Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            position = "Right";
                            telemetry.addData("Position", "Right");
                        } else {
                            position = "Center";
                            telemetry.addData("Position", "Center");
                        }
                    }
                }
                telemetry.addData("Position: ", position);
                telemetry.update();
            }
        }

    }

    //Sam's Project

    int capturecyclesstatic;
    int capturecycles;
    double imageSize;
    double objSize;
    double Azimuth;
    double percentage;

    int totalcaptures = 100;
    int capturecounter = 0;
    double totalSize = 0;
    double totalAzimuth = 0;
    double totalPercentage = 0;

    public boolean getPickup() {
        capturecycles=100;
        capturecyclesstatic=capturecycles;
        boolean var = false;
        recursiveAverage();
        return var;
    }


    public void recursiveAverage(){
      if (tfod != null && capturecycles>0) {
        capturecycles=capturecycles-1;
          List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
          if (updatedRecognitions != null) {
              telemetry.addData("# Object Detected", updatedRecognitions.size());
              // the following code was taken form FTC sample code and partially modified for our robot's purposes
              // It is alright if not understood.

              //x and y position values for minerals

              for (Recognition recognition : updatedRecognitions) {
                  imageSize +=(recognition.getImageWidth() * recognition.getImageHeight());
                  objSize =(recognition.getWidth() * recognition.getHeight());
                  percentage = (objSize / imageSize) * 100;
                  Azimuth = (double) (recognition.estimateAngleToObject(AngleUnit.DEGREES));
                  telemetry.update();

              }

          }

      }
      if(capturecycles>0){
        double avImageSize=imageSize/capturecyclesstatic;
        double avObjSize=objSize/capturecyclesstatic;
        double avAzimuth=Azimuth/capturecyclesstatic;
        telemetry.addData("AverageObjSize = ",avObjSize);
        telemetry.addData("AverageAzimuth = ",avAzimuth);
      }else{
        recursiveAverage();
      }
    }
  //END SAMS CDDE

    public void recursive() {
        if (capturecounter >= totalcaptures) {

            capturecounter++;

            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    for (Recognition recognition : updatedRecognitions) {
                        double imageSize = (double) (recognition.getImageWidth() * recognition.getImageHeight());
                        double objSize = (double) (recognition.getWidth() * recognition.getHeight());
                        double percentage = (objSize / imageSize) * 100;
                        double Azimuth = (double) (recognition.estimateAngleToObject(AngleUnit.DEGREES));

                        totalSize = totalSize + imageSize;
                        totalAzimuth = totalAzimuth + Azimuth;
                        totalPercentage = totalPercentage + percentage;

                        telemetry.addData("size=", objSize);

                    }
                }
            }
            recursive();
        }
    }


    public void getAngleOfObject() {
        boolean var = false;
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // the following code was taken form FTC sample code and partially modified for our robot's purposes
                // It is alright if not understood.

                //x and y position values for minerals

                for (Recognition recognition : updatedRecognitions) {
                    double angle = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                    telemetry.addData("Angle: ", angle);
                }

                telemetry.update();
            }

        }
    }


    public String getPosition() {
        return position;
    }

    public void update() {
        updateTensorFlow();
        getPosition();
    }

    public void updateTensorFlowSingle() {
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // the following code was taken form FTC sample code and partially modified for our robot's purposes
                // It is alright if not understood.
                //x and y position values for minerals
                int goldMineralX = -1;
                int goldMineralY = -1;
                double Azimuth = 0;
                /* Creates an array list of x and y values for all minerals*/
                ArrayList<Integer> goldXvals = new ArrayList<Integer>();
                ArrayList<Integer> goldYvals = new ArrayList<Integer>();
                ArrayList<Double> goldAzimuths = new ArrayList<Double>();
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldXvals.add((int) recognition.getLeft());
                        goldYvals.add((int) recognition.getBottom());
                        goldAzimuths.add(recognition.estimateAngleToObject(AngleUnit.DEGREES));

                    }
                }
                int indexToRemove = 0;
                for (int i = 0; i < goldYvals.size(); i++) {
                    if (goldMineralY < goldYvals.get(i)) {
                        goldMineralY = goldYvals.get(i);
                        goldMineralX = goldXvals.get(i);
                        Azimuth = goldAzimuths.get(i);
                    }
                }
                telemetry.addData("Gold X value: ", goldMineralX);
                telemetry.addData("Azimuth: ", Azimuth);
                if (goldMineralX != -1) {
                    if (goldMineralX <= 426 && Azimuth <= -15) {
                        position = "Left";
                        telemetry.addData("Position: ", "Left");
                    } else if (goldMineralX >= 852 && Azimuth >= 15) {
                        position = "Right";
                        telemetry.addData("Position: ", "Right");
                    } else {
                        position = "Center";
                        telemetry.addData("Position: ", "Center");
                    }
                }

                telemetry.addData("Position: ", position);
                telemetry.update();
            }
        }
    }

    public void experimentalRecognition(){
        if(tfod != null){
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if(updatedRecognitions != null){
                telemetry.addData("# Object Detected", updatedRecognitions.size());
            }
        }
    }


    private void initVuforia() {
        //This method will create all neccessary vuforia parameters and set them
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = UniConstTwo.vuforiaLicenceKey;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initVuforiaWebCam() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = UniConstTwo.vuforiaLicenceKey;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

}
