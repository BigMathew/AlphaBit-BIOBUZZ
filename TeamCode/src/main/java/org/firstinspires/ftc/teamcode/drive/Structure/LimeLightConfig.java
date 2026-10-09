package org.firstinspires.ftc.teamcode.drive.Structure;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class LimeLightConfig {
    public static Limelight3A limelight;
    Pose3D botPose;

    public void init(HardwareMap hardwareMap){
        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class,"limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(150);
        limelight.start();
    }

    public LLResult getLLResult(){
        return limelight.getLatestResult();
    }
}