import numpy as np

# np.random.seed(0)


# The resolution of the observation space
# The four variables of the observation space, from left to right:
#   0: X component of the vector pointing to the middle of the platform from the lander
#   1: Y component of the vector pointing to the middle of the platform from the lander
#   2: X component of the velocity vector of the lander
#   3: Y component of the velocity vector of the lander
OBSERVATION_SPACE_RESOLUTION = [5, 4, 3, 3]  # TODO


class LunarLanderAgentBase:
    def __init__(self, observation_space, action_space, n_iterations):
        self.observation_space = observation_space
        self.q_table = np.zeros([*OBSERVATION_SPACE_RESOLUTION, len(action_space)])
        self.env_action_space = action_space
        self.n_iterations = n_iterations

        self.epsilon = 1.
        self.iteration = 0
        self.test = False

        self.alpha = 0.2  # 0.1, 0.99
        self.gamma = 0.99

        self.best_table = np.zeros([*OBSERVATION_SPACE_RESOLUTION, len(action_space)])
        self.best_reward = -200
        self.epsilon_decay = 0.0001
        self.max_epsilon = 1
        self.min_epsilon = 0.001
        self.epoch = 0

        self.epsilon_step = 100
        self.save_interval = 1000
        self.min_alpha = 0.1
        self.prev_value = 0

    @staticmethod
    def quantize_state(observation_space, state):

        a = 0
        b = 0
        c = 0
        d = 0

        tX = state[0]
        # if tX <= -280:
        #     a = 2
        if (tX < -17) & (tX > -300):
            a = 2
        if (tX >= -17) & (tX <= 17):
            a = 1
        if (tX < 300) & (tX > 17):
            a = 0



        tY = state[1]
        if 10 < tY:
            b = 2
        if 10 >= tY:
            b = 1
        if 4 >= tY:
             b = 1
        if 1 >= tY:
            b = 0

        vX = state[2]
        if vX < -0.75:
            c = 2
        if (vX >= -0.75) & (0.75 >= vX):
            c = 1
        if 0.75 < vX:
            c = 0

        vY = state[3]
        if vY > 2.0:
            d = 2
        if (vY <= 2.0) & (vY >= -0.3):
            d = 1
        if vY < -0.3:
            d = 0

        return a, b, c, d  # TODO

    def epoch_end(self, epoch_reward_sum):
        #
        # self.epsilon = self.min_epsilon + (self.max_epsilon - self.min_epsilon) * np.exp(
        #     self.epsilon_decay * self.epoch)

        # if self.iteration >= 600000:
        #
        #     if self.epoch % 8000 == 0:
        #         if self.epsilon > self.min_epsilon:
        #             self.epsilon = self.epsilon - self.epsilon_decay
        #         else:
        #             self.epsilon = self.min_epsilon


        if self.epsilon <= self.min_epsilon:
            self.epsilon = self.min_epsilon
        else:
            self.epsilon = 1 - (self.iteration * 3 / self.n_iterations)

        if epoch_reward_sum > self.best_reward:
            self.best_reward = epoch_reward_sum
            self.best_table = self.q_table

        pass  # TODO

    def learn(self, old_state, action, new_state, reward):
        state = self.quantize_state(self.observation_space, old_state)
        n_state = self.quantize_state(self.observation_space, new_state)
        # print(state)
        actions = self.q_table[state]
        value = actions[action]
        value = value * (1 - self.alpha) \
                + self.alpha * (reward + self.gamma * np.max(self.q_table[n_state, :]))
        # value = value + self.alpha * (reward + self.gamma * np.max(self.q_table[n_state, :]) - value )
        index = state + (action,)
        self.q_table[index] = value
        pass  # TODO

    def train_end(self):
        # ... TODO

        self.q_table = self.q_table  # TODO
        self.test = True
