/*package invalidname;*/
import java.util.Arrays;

public class LunarLanderAgentBase {
    // The resolution of the observation space
    // The four variables of the observation space, from left to right:
    //   0: X component of the vector pointing to the middle of the platform from the lander
    //   1: Y component of the vector pointing to the middle of the platform from the lander
    //   2: X component of the velocity vector of the lander
    //   3: Y component of the velocity vector of the lander
    static final int[] OBSERVATION_SPACE_RESOLUTION = {7, 5, 7, 5}; // TODO

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
    double alpha = 0.7;
    double gamma = 0.95;
    double alphaMax = 0.95;
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
        if (tarX < -100)
            a = 6;
        if (tarX >= -100)
            a = 5;
        if (tarX >= -40)
            a = 4;
        if (-17 <= tarX && tarX <= 17)
            a = 3;
        if (tarX <= 40)
            a = 2;
        if (tarX <= 100)
            a = 1;
        if (tarX > 100)
            a = 0;


        double tarY = state[1];
        int b = 0;
        if (tarY > 30)
            b = 4;
        if (tarY <= 30)
            b = 3;
        if (tarY <= 10)
            b = 2;
        if (tarY <= 4)
            b = 1;
        if (tarY <= 1)
            b= 0;

        double velX = state[2];
        int c = 0;
        if (velX < -5)
            c = 6;
        if (velX >= -5)
            c = 5;
        if (velX >= -2.4)
            c = 4;
        if (velX >= -0.75 && velX <= 0.75)
            c = 3;
        if (velX > 0.75)
            c = 2;
        if (velX >= 2.4)
            c = 1;
        if (velX > 5)
            c = 0;

        double velY = state[3];
        int d = 0;
        if (velY >= 5)
            d = 4;
        if (velY < 5)
            d = 3;
        if (velY < 2.4)
            d = 2;
        if (velY < 1.6)
            d = 1;
        if (velY < -0.3)
            d = 0;

        return new int[]{a, b, c, d}; // TODO
    }

    public void epochEnd(double epochRewardSum) {
        epoch++;
        double di = iteration;
        epsilon = 1 - (di*3.5/nIterations * epsilon_decay);
        if(epsilon <= epsilonMin )
            epsilon = epsilonMin;

        /*alpha = 0.7 + (di/nIterations * epsilon_decay);
        if(alpha >= alphaMax )
            alpha = alphaMax;*/


/*        if (epoch % 2000 == 0) {
            System.out.println(epsilon);
        }*/
        // TODO
    }

    public void learn(double[] oldState, int action, double[] newState, double reward) {
        double rewardFactor = 1;
        if (bestReward <= reward){
            bestReward = reward;
            rewardFactor = 1.5;
        }


        iteration++;
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
         value = value + alpha * (rewardFactor * reward + gamma * newMax - value);

        //value = value * (1 - alpha) + alpha * ( reward + gamma * newMax );

        qTable[oldS[0]][oldS[1]][oldS[2]][oldS[3]][action] = value;
        // TODO
        lastReward = reward;
    }

    public void trainEnd() {
        // ... TODO????
        //System.out.println(qTable);
        qTable = this.qTable; // TODO
        test = true;
        /*for (int i = 0; i < OBSERVATION_SPACE_RESOLUTION[0]; i++) {
            for (int j = 0; j < OBSERVATION_SPACE_RESOLUTION[1]; j++) {
                for (int k = 0; k < OBSERVATION_SPACE_RESOLUTION[2]; k++) {
                    for (int l = 0; l < OBSERVATION_SPACE_RESOLUTION[3]; l++) {
                        for (int m = 0; m < envActionSpace.length; m++) {
                            System.out.print(qTable[i][j][k][l][m] + ' ');
                        }
                        System.out.println('\n');
                    }
                    System.out.println("--\n");
                }
                System.out.println(" * \n");
            }
            System.out.println(" // \n");
        }*/
    }
}
