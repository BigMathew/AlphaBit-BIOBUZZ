package org.firstinspires.ftc.teamcode.drive.Structure;

import static org.firstinspires.ftc.teamcode.drive.Structure.ConstantValues.VELOCITY_TRESHOLD;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Systems {
    public LimeLightConfig limelight = new LimeLightConfig();
    Gamepad gamepad1,gamepad2;
    Follower follower;
    LLResult llResult;

    DcMotorEx Intake_LeftMotor;
    DcMotorEx Intake_RightMotor;
    DcMotorEx Shooter_Motor1;
    DcMotorEx Shooter_Motor2;

    public Systems(HardwareMap hwmap, Gamepad gmpd1, Gamepad gmpd2, Follower TeleopFollwer){
        gamepad1 = gmpd1;
        gamepad2 = gmpd2;
        this.follower = TeleopFollwer;

        limelight.init(hwmap);

        Intake_RightMotor = hwmap.get(DcMotorEx.class, "Intake_LeftMotor");
        Intake_RightMotor = hwmap.get(DcMotorEx.class,"Intake_RightMotor");

        Shooter_Motor1 = hwmap.get(DcMotorEx.class,"Shooter_Motor1");
        Shooter_Motor2 = hwmap.get(DcMotorEx.class,"Shooter_Motor2");

        Intake_RightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        Intake_LeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        Shooter_Motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        Shooter_Motor2.setDirection(DcMotorSimple.Direction.REVERSE);

        Shooter_Motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Shooter_Motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Shooter_Motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Shooter_Motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Intake_LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Intake_RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Shooter_Motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Shooter_Motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public double x_position;
    public double y_position;
    public double headingAngle;
//    public double LL_x_position;
//    public double LL_y_position;
//    public double LL_headingAngle;
    public double robotVelocity;
    public  boolean robotStationary;

    public double calculatedRPM = 0;
    public double targetFlyWheelRPM = 0;
    public static double kV = 0;
    public static double kS = 0;
    public static double kP = 0;

    public void Run(){
        follower.update();
        llResult = limelight.getLLResult();

        headingAngle = Math.toDegrees(follower.pose().heading());//rad
        headingAngle = (headingAngle % 360 + 360) % 360;//deg

        x_position = follower.pose().x();
        y_position = follower.pose().y();
        robotVelocity = Math.abs(follower.velocity().vx) + Math.abs(follower.velocity().vy);

        robotStationary = robotVelocity < VELOCITY_TRESHOLD;
    }

    public double getRpm(){
        double encoderCPM = 28;
        return ((Shooter_Motor1.getVelocity()/encoderCPM)*60);// / gearRatio;
    }

    public void calculateFlywheelMotorPowerRPM(double distance) {
        double rpm;

        rpm = 0.0000154195 * Math.pow(distance, 4)
                - 0.00618559 * Math.pow(distance, 3)
                + 0.869772 * Math.pow(distance, 2)
                - 32.94057 * distance
                + 3258.7466;

        calculatedRPM = rpm;
    }

    public void updateFlyWheel(){
        double currentRPM = getRpm();
        double error = targetFlyWheelRPM - currentRPM;
        double power = kS + kV * targetFlyWheelRPM + kP * error;
        power = Math.max(0,Math.min(1,power));

        Shooter_Motor1.setPower(power);
        Shooter_Motor2.setPower(power);
    }

}
