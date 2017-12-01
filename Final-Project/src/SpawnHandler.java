import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import screen.Screen;

public class SpawnHandler {

	private AnimationTimer spawnTimer;
	private UnitHandler unitHandler;
	private Screen screen;
	private Text[] warnings;
	
	public SpawnHandler(UnitHandler u, Screen s) {
		unitHandler = u;
		screen = s;
	}
	
	public void setupTimer() {
		warnings = new Text[5];
		for(int i = 0; i < warnings.length; i++) {
			warnings[i] = new Text("!");
			
			warnings[i].setFont(Font.font("Times New Roman",FontWeight.BOLD,screen.getHeight()/15));
			screen.addNode(warnings[i]);
		}
		spawnTimer = new AnimationTimer() {
			int i = 0;
			boolean gaveWarning = false;
			int direction = 0;
			public void handle(long now) {
				
				if(i>300&&!gaveWarning) {
					direction = (int)(Math.random()*4);
					giveWarning(direction);
					gaveWarning = true;
				}
				if(i>500) {
					for(int i = 0; i < warnings.length; i++)
						warnings[i].setFill(Color.TRANSPARENT);
					spawnUnits(direction);
					i = 0;
					gaveWarning = false;
				}
				i++;
			}
		};
	}
	
	public void startTimer() {
		spawnTimer.start();
	}
	public void stopTimer() {
		spawnTimer.stop();
	}
	
	public void giveWarning(int direction) {
		for(int i = 0; i < warnings.length; i++) {
			warnings[i].setFill(Color.RED);
			if(direction == 0) {
				warnings[i].setX(screen.getHeight()/25);
				warnings[i].setY((i+1)*screen.getHeight()/(warnings.length+1));
			}
			if(direction == 1) {
				warnings[i].setX(screen.getWidth()-screen.getHeight()*2/25);
				warnings[i].setY((i+1)*screen.getHeight()/(warnings.length+1));
			}
			if(direction == 2) {
				warnings[i].setX((i+1)*screen.getWidth()/(warnings.length+1));
				warnings[i].setY(screen.getHeight()*2/25);
			}
			if(direction == 3) {
				warnings[i].setX((i+1)*screen.getWidth()/(warnings.length+1));
				warnings[i].setY(screen.getHeight()*24/25);
			}
		}
	}
	
	public void spawnUnits(int direction) {
		for(int i = 0; i < 5; i++) {
			Enemy e = new Enemy(screen.getHeight()/50);
			if(direction == 0) //left
				e.move(-1*e.getRadius()*2, (i+1)*screen.getHeight()/6);
			if(direction == 1) //right
				e.move(screen.getWidth(), (i+1)*screen.getHeight()/6);
			if(direction == 2) //up
				e.move((i+1)*screen.getWidth()/6, -1*e.getRadius()*2);
			if(direction == 3) //down
				e.move((i+1)*screen.getWidth()/6, screen.getHeight());
			unitHandler.addUnit(UnitHandler.ENEMY, e);
			screen.addNode(e.getShape());
			screen.addNode(e.getAttackLine());
			screen.addNode(e.getAttackRange());
			e.setAttackAnimation(unitHandler, screen.getPane());
			e.startAttackAnimation();
			e.setMoveAnimation(screen.getWidth(), screen.getHeight());
			e.startMoveAnimation();
		}
	};
	
}
