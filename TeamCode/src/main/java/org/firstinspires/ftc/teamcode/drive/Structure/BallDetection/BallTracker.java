package org.firstinspires.ftc.teamcode.drive.Structure.BallDetection;

import java.util.ArrayList;

public class BallTracker {

    public ArrayList<LimeLightConfigAdvanced.BallPosition> balls = new ArrayList<>();

    public static double SAME_BALL_DISTANCE = 1.5;

    public void update(LimeLightConfigAdvanced.BallPosition detected){
        if(detected == null){
            return;
        }

        for(LimeLightConfigAdvanced.BallPosition ball : balls){
            double distance = Math.pow(ball.x - detected.x,2) +
                    Math.pow(ball.y - detected.y,2);

            if(distance < SAME_BALL_DISTANCE*SAME_BALL_DISTANCE){
                ball.x = detected.x;
                ball.y = detected.y;
                return;
            }
        }

        balls.add(detected);
    }

}
