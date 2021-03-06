import java.util.HashSet;

import screen.ScreenManager;

public class UnitHandler {
	
	HashSet<GameUnit> playerUnits;
	private HashSet<GameUnit> enemyUnits;
	private HashSet<GameUnit> selectedUnits;
	
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	public static final int SELECTED = 2;
	
	GameScreenManager manager;
	SpawnHandler spawn;
	
	public UnitHandler(GameScreenManager m) {
		playerUnits = new HashSet<>();
		enemyUnits = new HashSet<>();
		selectedUnits = new HashSet<>();
		manager = m;
	}
	
	public void setSpawnHandler(SpawnHandler s) {
		spawn = s;
	}
	
	public HashSet<GameUnit> getSet(int i){
		switch(i) {
			case PLAYER:
				return playerUnits;
			case ENEMY:
				return enemyUnits;
			case SELECTED:
				return selectedUnits;
		}
		return null;
	}
	
	public void addUnit(int i, GameUnit u){
		switch(i) {
			case PLAYER:
				playerUnits.add((PlayerUnit)u);
				break;
			case ENEMY:
				enemyUnits.add(u);
				break;
			case SELECTED:
				selectedUnits.add((PlayerUnit)u);
				((PlayerUnit)u).toggleSelect();
				break;
		}
	}
	public void removeUnit(int i, GameUnit u){
		switch(i) {
			case PLAYER:
				playerUnits.remove(u);
				selectedUnits.remove(u);
				if(!playerUnits.stream().anyMatch(un->un instanceof AttackUnit)) {
					enemyUnits.stream().filter(un-> un instanceof AttackUnit).forEach(un->un.stopAttackAnimation());
					spawn.stopTimer();
					clearAll();
					manager.setState(ScreenManager.SCORE_STATE);
					
				}
					
				break;
			case ENEMY:
				
				if(enemyUnits.remove(u))
					Settings.score+=1;
				break;
			case SELECTED:
				selectedUnits.remove(u);
				((PlayerUnit)u).toggleSelect();
				break;
		}
	}
	public void clearUnits(int i){
		switch(i) {
			case PLAYER:
				playerUnits = new HashSet<>();
				break;
			case ENEMY:
				enemyUnits = new HashSet<>();
				break;
			case SELECTED:
				selectedUnits.stream().forEach(u -> ((PlayerUnit)u).toggleSelect());
				selectedUnits = new HashSet<>();
				break;
		}
	}
	public void clearAll() {
		clearUnits(PLAYER);
		clearUnits(ENEMY);
		clearUnits(SELECTED);
	}
}
