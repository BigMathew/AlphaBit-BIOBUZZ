package org.firstinspires.ftc.teamcode.drive.Structure.FlyWheelSetup;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Configurable
@TeleOp
//@Disabled
public class kVTuner extends OpMode {
    //sa fie pana cand ajunge la rpm dorit fara kP sa il mentina acolo
    Flywheel flywheel = new Flywheel();

    public static double kV = 0; // schimba in 0.01 daca nu merge din prima
    public static double kS = 0; //aici pune kS de dincolo

    public static double goalRPM = 0;// aici sa pun cat vr la maxim testez singur si dupa verific daca si cum merge cat vr sa traga maxim pot pune 2 valori in dashboard sa vad daca da scale corect

    @Override
    public void init() {
       // telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flywheel.init(hardwareMap);
    }

    @Override
    public void loop() {

        double power = (kV * goalRPM) + kS;

        flywheel.setMotorPower(power);

        telemetry.addData("kV", kV);
        telemetry.addData("RPM",flywheel.getRpm());
        telemetry.addData("Ticks per Sec", flywheel.getTicksPerSec());
        telemetry.addData("TPS", flywheel.getTicksPerSec());
        telemetry.addData("CPR", Flywheel.encoderCPM);
        telemetry.addData("RPM", (flywheel.getTicksPerSec()/flywheel.encoderCPM)*60);
    }
}
