package org.firstinspires.ftc.teamcode.drive.Structure;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Chasis {
    Gamepad gamepad;
    Follower follower;


    public Chasis(Follower follower, Gamepad gamepad){
        this.gamepad = gamepad;
        this.follower = follower;
    }

    public void run(){
        double forward = -gamepad.left_stick_y;
        double lateral = gamepad.left_stick_x;
        double turn = gamepad.right_stick_x;

        ///robot centric
        ManualDrive.driveOrHold(
                follower,
                -gamepad.left_stick_y,
                gamepad.left_stick_x,
                gamepad.right_stick_x
        );
        follower.manual(forward,lateral,turn);

        ///field centric
//        DrivePowers powers = ManualDrive.fieldCentric(
//                -gamepad.left_stick_y,
//                gamepad.left_stick_x,
//                gamepad.right_stick_x,
//                follower.pose().heading()
//        );
//        ManualDrive.driveOrHold(follower,powers);

        follower.update();

    }
}
