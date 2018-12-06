package evaluation;

import bot.BotState;

public class BoardEvaluation {
	public BotState state;
	
	public void GameStateEvaluator(BotState state) {
		this.state = state;
	}
	
    public boolean CompletelyLost = false;

	public void EvaluateGameState()
    {
        // Don't switch from a lost game to an open game
        if (CompletelyLost)
            return;

        // never finished
    }
	
	
}
