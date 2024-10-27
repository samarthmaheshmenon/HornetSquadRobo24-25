/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Auto Place Specimen Pull Sample to Net", group="")
public class AutoCodeToNetZone extends LinearOpMode {

    static final double     DRIVE_SPEED             = 0.3;
    static final double     DRIVE_INCREASED_SPEED             = 0.8;
    static final double     TURN_SPEED              = 0.2;

    private HornetRobo hornetRobo;

    //manager classes
    private AutoDriveManager driveManager;
    private AutoArmManager armManager;
    private AutoGrabberManager grabberManager;
    private AutoViperSlideManager viperSlideManager;

    public void initialize()
    {
        hornetRobo = new HornetRobo();
        AutoHardwareMapper.MapToHardware(this, hornetRobo);
        driveManager = new AutoDriveManager(this, hornetRobo);
        grabberManager = new AutoGrabberManager(this, hornetRobo);
        armManager = new AutoArmManager(this, hornetRobo);
        viperSlideManager = new AutoViperSlideManager(this, hornetRobo);
    }

    public void runOpMode() {
        initialize();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Starting to move", "");
            telemetry.update();
            while (opModeIsActive() && !isStopRequested()) {

                telemetry.addData("Move Forward to reach subermersible  ", "");
                //TODO: adjust the distance during testing
                double distanceToSub = 12;
                telemetry.addData("Distance To Sub: ", distanceToSub);
                telemetry.update();
                //reset encoders
                driveManager.SetAllMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

                telemetry.addData("Setting target position", "");
                telemetry.update();

                //set target position to encoders and move to position
                driveManager.SetTargetPosition(AutoDriveManager.DriveDirection.FORWARD, distanceToSub);
                driveManager.SetDrivePower(DRIVE_SPEED, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

                telemetry.addData("Reached Submersible", "");
                telemetry.update();

                //Set Arm in a position to hang specimen
                double armPosition = 0.5; //TODO: Correct during testing
                armManager.MoveArmToPosition(armPosition);
                telemetry.addData("Set Arm Pos: ", armPosition);
                telemetry.update();

                //Open grabber to hang
                grabberManager.OpenOrCloseGrabber(true);
                telemetry.addData("Set grabber to open", "");
                telemetry.update();

                //Move arm back
                armPosition = 0.6; //TODO: Correct during testing
                armManager.MoveArmToPosition(armPosition);
                telemetry.addData("Set Arm Pos Back: ", armPosition);
                telemetry.update();

                // Go reverse to be away from submersible to move to pick sample
                int reverseDistance = 5; //TODO: Adjust during testing
                driveManager.SetTargetPosition(AutoDriveManager.DriveDirection.BACKWARD, reverseDistance);
                driveManager.SetDrivePower(DRIVE_SPEED, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("Go reverse to be away from submersible to move to pick sample", reverseDistance);
                telemetry.update();

                //strafe to go pass submersible edges to avoid hitting when moving forward to pick samples
                int strafeDistance = 23; //TODO: Adjust during testing
                driveManager.SetTargetPositionToStrafe(AutoDriveManager.DriveDirection.LEFT, strafeDistance);
                driveManager.SetPowerToStrafe(AutoDriveManager.DriveDirection.LEFT, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("strafe to go pass submersible edges to avoid hitting when moving forward to pick samples", strafeDistance);
                telemetry.update();

                //MOVE FORWARD past sample
                double distance = 30; //TODO: Adjust during testing
                driveManager.SetTargetPosition(AutoDriveManager.DriveDirection.FORWARD, distance);
                driveManager.SetDrivePower(DRIVE_SPEED, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("MOVE FORWARD past sample", distance);
                telemetry.update();

                //strafe to a position to plough right most sample to net zone when moving backward
                strafeDistance = 10; //TODO: Adjust during testing
                driveManager.SetTargetPositionToStrafe(AutoDriveManager.DriveDirection.LEFT, strafeDistance);
                driveManager.SetPowerToStrafe(AutoDriveManager.DriveDirection.LEFT, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("strafe to a position to plough right most sample to net zone when moving backward", "");
                telemetry.update();

                //Move backward until it reaches net zone
                distance = 35; //TODO: Adjust during testing
                driveManager.SetTargetPosition(AutoDriveManager.DriveDirection.FORWARD, distance);
                driveManager.SetDrivePower(DRIVE_SPEED, DRIVE_SPEED);
                driveManager.SetAllMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("Move backward until it reaches net zone", "");
                telemetry.update();

                //done
                driveManager.StopRobo();
                telemetry.addData("Stopped Robo", "");
                telemetry.update();

                break;

            }
        }
    }
}