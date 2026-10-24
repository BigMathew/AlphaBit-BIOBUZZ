package org.firstinspires.ftc.teamcode.drive.OpMode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.Structure.Chassis;
import org.firstinspires.ftc.teamcode.drive.Structure.Systems;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class BioBuzz_TeleOp extends LinearOpMode {

    public Follower follower;
    Chassis chassis;
    Systems systems;

    @Override
    public void runOpMode() throws InterruptedException {

        follower = Constants.create(hardwareMap);

        chassis = new Chassis(follower,gamepad1);
        systems = new Systems(hardwareMap,gamepad1,gamepad2,follower);

        while (opModeInInit()){

        }

        waitForStart();

        while (opModeIsActive()){
            chassis.run();

            telemetry.addData("X position", systems.x_position);
            telemetry.addData("Y position", systems.y_position);
            telemetry.addData("Heading","%.1f°",systems.headingAngle);
        }
    }

}
