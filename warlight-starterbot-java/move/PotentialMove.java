package move;

import bot.BotState;
import move.AttackTransferMove;
import main.Region;

public class PotentialMove extends AttackTransferMove {

	  private int minArmies;
	  private int maxArmies;
	  private int score;
	  
	  public PotentialMove(BotState state, Region fromRegion, Region toRegion, int armies) {
	    super(state.getMyPlayerName(), fromRegion, toRegion, armies);
	    
	    this.minArmies = armies;
	    this.maxArmies = Integer.MAX_VALUE;
	    this.score = 0;
	  }
	  
	  public void setMaxArmies (int maxArmies) {
	    this.maxArmies = maxArmies;
	  }
	  
	  public int getMaxArmies () {
	    return this.maxArmies;
	  }
	  
	  public void setScore (int score) {
	    this.score = score;
	  }
	  
	  public int getScore () {
	    return this.score;
	  }
}
