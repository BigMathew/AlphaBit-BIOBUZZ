package org.firstinspires.ftc.teamcode.drive.OpMode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.Structure.Chasis;
import org.firstinspires.ftc.teamcode.drive.Structure.Systems;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class BioBuzz_TeleOp extends LinearOpMode {

    public Follower follower;
    Chasis chasis;
    Systems systems;

    @Override
    public void runOpMode() throws InterruptedException {


        chasis = new Chasis(follower,gamepad1);
        systems = new Systems(hardwareMap,gamepad1,gamepad2,follower);

        while (opModeInInit()){
            follower = Constants.create(hardwareMap);
        }

        waitForStart();

        while (opModeIsActive()){
            chasis.run();

            telemetry.addData("X position", systems.x_position);
            telemetry.addData("Y position", systems.y_position);
            telemetry.addData("Heading","%.1f°",systems.headingAngle);
        }
    }

}
