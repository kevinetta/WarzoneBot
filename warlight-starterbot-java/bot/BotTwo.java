/**
 * Warlight AI Game Bot
 *
 * Last update: April 02, 2014
 *
 * @author Jim van Eeden
 * @version 1.0
 * @License MIT License (http://opensource.org/Licenses/MIT)
 *
*
 * Total Territories owned 

Total Armies owned 

Border Territories 

Border Armies 

Are these all of the parameters we�re planning on using for now?^ 

I simplified it to total territories and total armies 

Bot: 

Evaluate the board 

Neural network with the weights 

Inputs 

Total territories 

Border armies 

Use a file that the bot reads in the weights to use 

Board Successor function(currentBoard) 

Output list of next boards 

Each board has a board score 

Minimax 
*/

package bot;

/**
 * This is a simple bot that does random (but correct) moves.
 * This class implements the Bot interface and overrides its Move methods.
 *  You can implements these methods yourself very easily now,
 * since you can retrieve all information about the match from variable â€œstateâ€�.
 * When the bot decided on the move to make, it returns an ArrayList of Moves. 
 * The bot is started by creating a Parser to which you add
 * a new instance of your bot, and then the parser is started.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import main.Region;
import move.AttackTransferMove;
import move.PlaceArmiesMove;

public class BotTwo implements Bot 
{
	private double w1, w2;
	@Override
	/**
	 * A method used at the start of the game to decide which player start with what Regions. 6 Regions are required to be returned.
	 * This example randomly picks 6 regions from the pickable starting Regions given by the engine.
	 * @return : a list of m (m=6) Regions starting with the most preferred Region and ending with the least preferred Region to start with 
	 */
	public ArrayList<Region> getPreferredStartingRegions(BotState state, Long timeOut)
	{
		int m = 6;
		ArrayList<Region> preferredStartingRegions = new ArrayList<Region>();
		for(int i=0; i<m; i++)
		{
			double rand = Math.random();
			int r = (int) (rand*state.getPickableStartingRegions().size());
			int regionId = state.getPickableStartingRegions().get(r).getId();
			Region region = state.getFullMap().getRegion(regionId);

			if(!preferredStartingRegions.contains(region))
				preferredStartingRegions.add(region);
			else
				i--;
		}
		
		return preferredStartingRegions;
	}

	@Override
	/**
	 * This method is called for at first part of each round. This example puts two armies on random regions
	 * until he has no more armies left to place.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut) 
	{
		
		ArrayList<PlaceArmiesMove> placeArmiesMoves = new ArrayList<PlaceArmiesMove>();
		String myName = state.getMyPlayerName();
		int armies = 1;
		int armiesLeft = state.getStartingArmies();
		LinkedList<Region> visibleRegions = state.getVisibleMap().getRegions();
		
		while(armiesLeft > 0)
		{
			double rand = Math.random();
			int r = (int) (rand*visibleRegions.size());
			// get our regions
			LinkedList<Region> ownedRegions = new LinkedList<Region>();
			for(int i = 0; i < visibleRegions.size(); i++)
			{
				if(visibleRegions.get(i).ownedByPlayer(myName))
				{
					ownedRegions.add(visibleRegions.get(i));
				}
			}
			Region region = ownedRegions.get(r);
			LinkedList<Region> neighborsList = region.getNeighbors();
			boolean hasEnemyNeighbor = false;
			for(int i = 0; i < neighborsList.size(); i++)
			{
				if(!neighborsList.get(i).ownedByPlayer(myName))
				{
					hasEnemyNeighbor = true;
				}
			}
			if(hasEnemyNeighbor)
			{
				placeArmiesMoves.add(new PlaceArmiesMove(myName, region, armies));
				armiesLeft -= armies;
			}
		}
		
		return placeArmiesMoves;
	}

	@Override
	/**
	 * This method is called for at the second part of each round. This example attacks if a region has
	 * more than 6 armies on it, and transfers if it has less than 6 and a neighboring owned region.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut) 
	{
		ArrayList<AttackTransferMove> attackTransferMoves = new ArrayList<AttackTransferMove>();
		String myName = state.getMyPlayerName();
		int armies = 5;
		
		for(Region fromRegion : state.getVisibleMap().getRegions())
		{
			if(fromRegion.ownedByPlayer(myName)) //do an attack
			{
				ArrayList<Region> possibleToRegions = new ArrayList<Region>();
				possibleToRegions.addAll(fromRegion.getNeighbors());
				
				while(!possibleToRegions.isEmpty())
				{
					double rand = Math.random();
					int r = (int) (rand*possibleToRegions.size());
					Region toRegion = possibleToRegions.get(r);
					
					if(!toRegion.getPlayerName().equals(myName)) //do an attack
					{
						if(fromRegion.getArmies()*1.5 > toRegion.getArmies())
						{
							attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
							break;
						}
					}
					else if(toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
					{
						attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
						break;
					}
					else
						possibleToRegions.remove(toRegion);
				}
			}
		}
		try {
			double value = evaluateBoard(state, attackTransferMoves);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return attackTransferMoves;
	}

	public double evaluateBoard(BotState state, ArrayList<AttackTransferMove> attackTransferMoves) throws IOException
	{
		// get weights
		File file = new File("data/weights.txt");
		BufferedReader fr = null;
		fr = new BufferedReader(new FileReader(file));
		String s = null;
		s = fr.readLine();
		String[] weights = s.split(",");
		w1 = Double.parseDouble(weights[0]);
		w2 = Double.parseDouble(weights[1]);
		double boardScore = state.getMyTotalArmies() * w1 + state.getMyTotalTerritories() * w2;
		fr.close();
		return boardScore;
	}
	
	public static void main(String[] args)
	{
		BotParser parser = new BotParser(new BotTwo());
		parser.run();
	}

}
