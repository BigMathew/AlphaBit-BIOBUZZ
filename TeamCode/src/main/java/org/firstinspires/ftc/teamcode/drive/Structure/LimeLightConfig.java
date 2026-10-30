package org.firstinspires.ftc.teamcode.drive.Structure;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@Configurable

public class LimeLightConfig {
    public static Limelight3A limelight;
    Pose3D botPose;
    Systems systems;

    public static double Kp = 0.025;
    public static double MAX_TURN_SPEED = 0.35;
    public static double MAX_FORWARD_SPEED = 0.28;
    public static double SEARCH_TURN_SPEED = 0.15;

    public static double MAX_SEARCH_TIME = 3.0;

    public static double DISTANCE_TRESHHOLD = 35.5;

    public enum TrackingState{
        IDLE,
        SEARCHING,
        TRACKING,
        INTAKING,
        TIMED_OUT
    }
    private TrackingState state = TrackingState.IDLE;

    private final ElapsedTime searchTimer = new ElapsedTime();
    private double searchDirection = 1;


    public void init(HardwareMap hardwareMap,Systems systems){
        this.systems = systems;
        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class,"limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(150);
        limelight.start();


    }

    public LLResult getLLResult(){
        return limelight.getLatestResult();
    }
    public TrackingState getState(){
        return state;
    }

    public void startBallSearch(){
        limelight.pipelineSwitch(1);

        searchTimer.reset();

        searchDirection = 1;
        state = TrackingState.SEARCHING;
    }

    public void rotateToFindBall(Follower follower){
        follower.manual(0,0,SEARCH_TURN_SPEED * searchDirection);
    }

    public void goTowardsBall(Follower follower){
        if(state == TrackingState.IDLE || state == TrackingState.TIMED_OUT){
            return;
        }

        LLResult llResult = limelight.getLatestResult();
        boolean ballVisible = llResult != null && llResult.isValid()
                && llResult.getStaleness() < 200 && llResult.getPipelineIndex() == 1;
        double intakeDistance = systems.intakeDistanceSensor_Distance;
        boolean ballInIntake = intakeDistance > 0 && intakeDistance < DISTANCE_TRESHHOLD;

        if(ballInIntake && (ballVisible || state == TrackingState.TRACKING || state == TrackingState.INTAKING)){
            state = TrackingState.INTAKING;
            systems.Intake();
            follower.manual(MAX_FORWARD_SPEED, 0, 0);
            return;
        }

        if(state == TrackingState.INTAKING){
            systems.stopIntake();
            startBallSearch();
            rotateToFindBall(follower);
            return;
        }

        if(ballVisible){
            state = TrackingState.TRACKING;
            searchTimer.reset();

            double  tx = llResult.getTx();

            double turn = Range.clip(tx * Kp, -MAX_TURN_SPEED,MAX_TURN_SPEED);
            double forward = MAX_FORWARD_SPEED;

            if(Math.abs(tx)> 12){
                forward = MAX_FORWARD_SPEED/2;
            }

            if(Math.abs(tx)<1.5){
                turn = 0;
            }else{
                searchDirection = Math.signum(tx);
            }
            systems.stopIntake();

            follower.manual(forward,0,turn);

        }else {
            systems.stopIntake();
            state = TrackingState.SEARCHING;

            if(searchTimer.seconds() > MAX_SEARCH_TIME){
                follower.manual(0, 0, 0);
                state = TrackingState.TIMED_OUT;
            }else{
                rotateToFindBall(follower);
            }

        }
    }

    public void stopTracking(Follower follower) {
        follower.manual(0, 0, 0);
        systems.stopIntake();
        state = TrackingState.IDLE;
    }
}
