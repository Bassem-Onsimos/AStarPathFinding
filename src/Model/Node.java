
package Model;

import Game.Controller;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Node {
    
    private int row, column;
    //
    private int x, y;
    private int size;
    //
    private boolean obstacle;
    //
                                //heuristic: the estimated distance from from this node to the end point  
    private double localGoal;   //local goal: distance from the start point to this node
    private double globalGoal;  //global goal = local goal + heuristic   
                                //            => an estimation of the total distance from start to end, if we followed this path. The lower, the better.
    

//
    private Node parent;
    private boolean visited;
    //
    private ArrayList<Node> neighbours;
    //
    private Controller controller;
    
    public Node(int row, int column, Controller controller) {
        this.row = row;
        this.column = column;
        this.size = controller.getNodeSize();
                
        this.x = size*(2*(column + 1) - 1);
        this.y = size*(2*(row + 1) - 1);
        
        this.controller = controller;
        
        neighbours = new ArrayList<>();
        reset();
    }
    
    public void render(Graphics2D g) {
        if(row == controller.getStart().y && column == controller.getStart().x) g.setColor(new Color(60, 179, 113));
        else if(row == controller.getEnd().y && column == controller.getEnd().x) g.setColor(new Color(30, 144, 255));
        else if(obstacle) g.setColor(Color.darkGray);
        else if(visited) g.setColor(new Color(193, 0, 0));
        else g.setColor(Color.white);
        
        g.fillOval(x, y, size, size);
    }
    
    public void renderPath(Graphics2D g) {
        g.setColor(Color.yellow);
        g.fillOval(x, y, size, size);
    }
    
    public void reset() {
        globalGoal = Double.POSITIVE_INFINITY;
        localGoal = Double.POSITIVE_INFINITY;
        parent = null;
        visited = false;
    }

    public boolean isObstacle() {
        return obstacle;
    }
    
    public void toggleObstacle() {
        obstacle = !obstacle;
    }
    
    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    public double getGlobalGoal() {
        return globalGoal;
    }

    public void setGlobalGoal(double globalGoal) {
        this.globalGoal = globalGoal;
    }

    public double getLocalGoal() {
        return localGoal;
    }

    public void setLocalGoal(double localGoal) {
        this.localGoal = localGoal;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Node neighbour) {
        if(neighbour != null) neighbours.add(neighbour);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    
    
}
