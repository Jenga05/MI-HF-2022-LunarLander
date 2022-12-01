package hello;

import java.util.Arrays;

public class LunarLanderAgentBase {
    // The resolution of the observation space
    // The four variables of the observation space, from left to right:
    //   0: X component of the vector pointing to the middle of the platform from the lander
    //   1: Y component of the vector pointing to the middle of the platform from the lander
    //   2: X component of the velocity vector of the lander
    //   3: Y component of the velocity vector of the lander
    static final int[] OBSERVATION_SPACE_RESOLUTION = {5, 4, 3, 3}; // TODO

    final double[][] observationSpace;
    double[][][][][] qTable;
    final int[] envActionSpace;
    private final int nIterations;

    double epsilon = 1.0;
    int iteration = 0;
    boolean test = false;

    // your variables here
    // ...



    double[][][][][] bestTable;
    double bestReward = -200;
    double lastReward = -200;

    double epsilonMin = 0.001;
    double alpha = 0.2;
    double gamma = 0.99;
    int epsilon_step = 100;
    double epsilon_decay = 0.9;
    int save_interval = 1000;

    int epoch = 0;

    public LunarLanderAgentBase(double[][] observationSpace, int[] actionSpace, int nIterations) {
        this.observationSpace = observationSpace;
        this.qTable =
                new double[OBSERVATION_SPACE_RESOLUTION[0]]
                        [OBSERVATION_SPACE_RESOLUTION[1]]
                        [OBSERVATION_SPACE_RESOLUTION[2]]
                        [OBSERVATION_SPACE_RESOLUTION[3]]
                        [actionSpace.length];
        this.envActionSpace = actionSpace;
        this.nIterations = nIterations;
    }

    public static int[] quantizeState(double[][] observationSpace, double[] state) {

        double tarX = state[0];
        int a = 0;
        if (tarX < -40)
            a = 4;
        if (tarX >= -40)
            a = 3;
        if (-17 <= tarX && tarX <= 17)
            a = 2;
        if (tarX <= 40)
            a = 1;
        if (tarX > 40)
            a = 0;


        double tarY = state[1];
        int b = 0;

        if (tarY > 10)
            b = 3;
        if (tarY <= 10)
            b = 2;
        if (tarY <= 4)
            b = 1;
        if (tarY <= 1)
            b= 0;

        double velX = state[2];
        int c = 0;
        if (velX < -0.75)
            c = 2;
        if (velX >= -0.75 && velX < 0.75)
            c = 1;
        if (velX > 0.75)
            c = 0;

        double velY = state[3];
        int d = 0;
        if (velY > 2.0)
            d = 2;
        if (velY <= 2.0 && velY >= -0.3)
            d = 1;
        if (velY < -0.3)
            d = 0;

        return new int[]{a, b, c, d}; // TODO
    }

    public void epochEnd(double epochRewardSum) {

        double di = iteration;
        if(epsilon <= epsilonMin )
            epsilon = epsilonMin;
        else
            epsilon = 1 - (di * 3 / nIterations);

         // TODO
    }

    public void learn(double[] oldState, int action, double[] newState, double reward) {

        int[] newS = quantizeState(observationSpace, newState);
        double newMax = -200, newValue;
        int temp;
        for (int i = 0; i < envActionSpace.length; i++) {
            temp = envActionSpace[i];
            newValue = qTable[newS[0]][newS[1]][newS[2]][newS[3]][temp];
            if (newValue > newMax)
                newMax = newValue;
        }
        
        int[] oldS = quantizeState(observationSpace, oldState);
        double value = qTable[oldS[0]][oldS[1]][oldS[2]][oldS[3]][action];
       // value = value + alpha * ( reward + gamma * newMax - value);
        //source:
        value = value * (1 - alpha) + alpha * ( reward + gamma * newMax );

        qTable[oldS[0]][oldS[1]][oldS[2]][oldS[3]][action] = value;
        // TODO
    }

    public void trainEnd() {
        // ... TODO????

        qTable = this.qTable; // TODO
        test = true;
    }
}
