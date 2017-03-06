package ejemplo;

public class GameStats {
	private int level, hp;
	public GameStats(int hp) {
		this.hp = hp;
	}
	public int getLevel() {
		return level;
	}
	public void nextLevel() {
		level++;
	}
	public double levelMultiplicator() {
		return 1.0 + (level*0.1);
	}
	public void resetState() {
		level = 0;
	}
	public String getRoundText() {
		return "Ronda: " + level + "/20";
	}
	public String getHpText() {
		return "Vida: " + hp + "/100";
	}
	public void takeALife() {
		hp--;
	}
}
