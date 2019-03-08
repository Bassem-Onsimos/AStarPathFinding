
package Algorithm;

import Game.Controller;
import Model.Grid;
import Model.Node;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar {
    
    private int rows;
    private int columns;
    private Grid grid;
    //
    private boolean pathCreated;
    //
    private Controller controller;
    
    public AStar(Controller controller) {
        rows = controller.getRows();
        columns = controller.getColumns();
        grid = controller.getGrid();
        
        this.controller = controller;
        pathCreated = false;
        
        findPath();
    }
    
    public void findPath() {
        pathCreated = false;
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                grid.get(row, column).reset();
            }
        }
        
        Node start = grid.get(controller.getStart().y, controller.getStart().x);
        Node end = grid.get(controller.getEnd().y, controller.getEnd().x);
                
        start.setLocalGoal(0);
        start.setGlobalGoal(calculateHeuristic(start, end));
        
        Node current = null;
        
        PriorityQueue<Node> newUnvisitedNodes = new PriorityQueue<>(new sortNodesByGlobalGoal());
        
        newUnvisitedNodes.add(start);
        
        while(!newUnvisitedNodes.isEmpty() && !isDone(current, end)) {
                        
            while(!newUnvisitedNodes.isEmpty() && newUnvisitedNodes.peek().isVisited()) newUnvisitedNodes.poll();
            
            if(newUnvisitedNodes.isEmpty()) break;
            
            current = newUnvisitedNodes.poll();
            current.setVisited(true);
            
            for(Node neighbour : current.getNeighbours()) {
                if(!neighbour.isVisited() && !neighbour.isObstacle()) newUnvisitedNodes.add(neighbour);
                
                double testNeighbourLocalGoal = current.getLocalGoal() + calculateDistance(current, neighbour);
                
                if(testNeighbourLocalGoal < neighbour.getLocalGoal()) {
                    neighbour.setParent(current);
                    neighbour.setLocalGoal(testNeighbourLocalGoal);
                    
                    neighbour.setGlobalGoal(neighbour.getLocalGoal() + calculateHeuristic(neighbour, end));
                }   
            }   
        }
        pathCreated = true;
    }
    
    public void drawPath(Graphics2D g) {
        if(!pathCreated) return;
        
        int steps = 0;
        double distance = 0.0;
        
        Node end = grid.get(controller.getEnd().y, controller.getEnd().x);
        Node start = grid.get(controller.getStart().y, controller.getStart().x);

        Node node = end;
        
        if(end.getParent() != null && start != end) {
            
            g.setColor(Color.yellow);
            g.setStroke(new BasicStroke(3));
            
            while(node.getParent()!=null) {
                int x1 = node.getX() + controller.getNodeSize()/2;
                int y1 = node.getY() + controller.getNodeSize()/2;
                int x2 = node.getParent().getX() + controller.getNodeSize()/2;
                int y2 = node.getParent().getY() + controller.getNodeSize()/2;
                   
                g.drawLine(x1, y1, x2, y2);
                if(node != end) node.renderPath(g);
                
                distance += calculateDistance(node, node.getParent());
                
                node = node.getParent();
                
                if(node == null) break;
                
                steps++;
   
            }
                   
        }
        controller.getSteps().setValue(steps); 
        controller.getDistance().setValue(Math.round(distance * 100d)/100d);
    }
    
    private boolean isDone(Node current, Node end) {
        
        if(controller.isOptimized()) {
            return current == end;
        }
        else
            return false;
        
    }
    
    private double calculateDistance(Node a, Node b) {
        return Math.sqrt( Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2) );                
    }
    
    private double calculateHeuristic(Node a, Node b) {
        return calculateDistance(a, b);   //Euclidean Distance
    }
    
    class sortNodesByGlobalGoal implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            return (int)(a.getGlobalGoal() - b.getGlobalGoal());
        }
        
    }
    
}
