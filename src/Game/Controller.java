
package Game;

import Algorithm.AStar;
import GamePanel.DoublePanelItem;
import GamePanel.GameData;
import GamePanel.IntegerPanelItem;
import Model.Grid;
import Model.Node;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Controller {
    
    private Game game;
    //
    private int rows = 14, columns = 14;
    private int nodeSize = 24;
    //
    private Grid grid; 
    //
    private Point start, end;
    private boolean movingStart, movingEnd;
    //
    private AStar aStar;
    //
    private IntegerPanelItem steps;
    private DoublePanelItem distance;
    //
    private boolean optimized;
    //
    
    public Controller(Game game) {
        this.game = game;
        
        steps = new IntegerPanelItem("Steps to reach destination", 0);
        distance = new DoublePanelItem("Distance to destination", 0.0);
        
        game.addGamePanel(new GameData() {
            @Override
            public void initiate() {
                addItem(distance);
                addItem(steps);
            }
        });
           
    }
    
    public void initiate() {
        start = new Point(0, 0);
        end = new Point(columns - 1, rows - 1);
        optimized = true;
        grid = new Grid(this);
        aStar = new AStar(this);        
    }
    
    public void update() {
        
        if(game.getInput().isButtonUp(MouseEvent.BUTTON3) || game.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            Node node = grid.getNodeExact(game.getInput().getMouseX(), game.getInput().getMouseY());
            if(node != null && !start.equals(new Point(node.getColumn(), node.getRow())) && !end.equals(new Point(node.getColumn(), node.getRow()))) {
                node.toggleObstacle();
                aStar.findPath();
            }
        }
        
        if(game.getInput().isButtonDown(MouseEvent.BUTTON1)) {
            Node node = grid.getNodeExact(game.getInput().getMouseX(), game.getInput().getMouseY());
            if(node != null) {
                if(start.equals(new Point(node.getColumn(), node.getRow()))) movingStart = true;
                
                else if(end.equals(new Point(node.getColumn(), node.getRow()))) movingEnd = true;
            }
        }
        
        if(game.getInput().isButton(MouseEvent.BUTTON1)) {
            Node node = grid.getNode(game.getInput().getMouseX(), game.getInput().getMouseY());
            if(node != null) {              
                if(!node.isObstacle()) {
                    if(movingStart) {
                        Point oldStart = start;
                        start = new Point(node.getColumn(), node.getRow());

                        if(!start.equals(oldStart)) aStar.findPath();
                    }
                    if(movingEnd) {
                        Point oldEnd = end;
                        end = new Point(node.getColumn(), node.getRow());

                        if(!end.equals(oldEnd)) aStar.findPath();
                    }
                }
            }
        }
        
        if(game.getInput().isButtonUp(MouseEvent.BUTTON1)) {
            movingStart = false;
            movingEnd = false;
        }
               
    }
    
    public void render(Graphics2D g) {
        grid.render(g);
        aStar.drawPath(g);
    }
    
    public void findPath() {
        aStar.findPath();
    }
    
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public Grid getGrid() {
        return grid;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public IntegerPanelItem getSteps() {
        return steps;
    }

    public DoublePanelItem getDistance() {
        return distance;
    }
    
    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public boolean isOptimized() {
        return optimized;
    }
    
    
}
