package org.firstinspires.ftc.teamcode.drive.Structure.BallDetection;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.drive.Structure.Systems;

@Configurable
public class LimeLightConfigAdvanced {
    public static Limelight3A limelight;
    Pose3D botPose;
    Systems systems;

    static double cameraH = 25.093596; //cm
    static double cameraForwardOffset = 18.243467; //cm
    static double cameraLateralOffset = 0; //cm
    double razaPollen = 3.68500; //cm
    static double beta = 1.30899694; //rad

    public static final double CM_TO_INCHES = 1.0/2.54;

    public static class BallPosition{
        public double x;
        public double y;
        public double distance;
        public double forward;
        public double lateral;

        public BallPosition(double x, double y, double distance, double forward, double lateral){
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.forward = forward;
            this.lateral = lateral;
        }
    }

    public void init(HardwareMap hardwareMap, Systems systems){
        this.systems = systems;

        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class,"limelight");
        limelight.pipelineSwitch(2);
        limelight.setPollRateHz(150);
        limelight.start();

    }
    public LLResult getLLResult(){
        return limelight.getLatestResult();
    }

    public BallPosition getBallPosition(){

        LLResult llResult = getLLResult();

        if(llResult == null || !llResult.isValid() || llResult.getStaleness() > 200 || llResult.getPipelineIndex() != 2){
            return null;
        }

        double tx = Math.toRadians(llResult.getTx());
        double ty = Math.toRadians(llResult.getTy());

        double height  = cameraH - razaPollen;
        double numitor = Math.sin(beta) - Math.tan(ty) * Math.cos(beta);

        if (height <= 0 || numitor <= 0.001) {
            return null;
        }

        double df = height / Math.tan(beta - ty);
        double dl = height * Math.tan(tx) / numitor;

        double forward = (df+cameraForwardOffset) * CM_TO_INCHES;
        double lateral = (dl+cameraLateralOffset) * CM_TO_INCHES;

        double robotX = systems.follower.pose().x();
        double robotY = systems.follower.pose().y();
        double robotHeading = systems.follower.pose().heading();

        double ballX = robotX
                + forward * Math.cos(robotHeading)
                + lateral * Math.sin(robotHeading);

        double ballY = robotY
                + forward * Math.sin(robotHeading)
                - lateral * Math.cos(robotHeading);

        double distance = Math.hypot(forward, lateral);

        return new BallPosition(ballX,ballY,distance,forward,lateral);
    }




}

