package org.firstinspires.ftc.teamcode.drive.Structure.FlyWheelSetup;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Configurable
@TeleOp
//@Disabled

public class kPTuner extends OpMode {
    Flywheel flywheel = new Flywheel();

    public static double kV = 0;
    public static double kS = 0;
    public static double kP = 0;

    public static double goalRPM = 0;// aici sa pun cat vr la maxim testez singur si dupa verific daca si cum merge cat vr sa traga maxim pot pune 2 valori in dashboard sa vad daca da scale corect

    @Override
    public void init() {
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flywheel.init(hardwareMap);
    }

    @Override
    public void loop() {

        double feedForward = (kV * goalRPM) + kS;
        double error = goalRPM - flywheel.getRpm();
        double feedBack = error * kP;

        flywheel.setMotorPower(feedForward + feedBack);

        telemetry.addData("kV","%,.6f", kP);
        telemetry.addData("RPM",flywheel.getRpm());
        telemetry.addData("Error",error);
    }
}
