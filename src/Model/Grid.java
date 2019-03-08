
package Model;

import Game.Controller;
import java.awt.Color;
import java.awt.Graphics2D;

public class Grid {

    private Controller controller;
    //
    private int rows, columns;
    private int nodeSize;
    private Node[][] nodes;
    //

    public Grid(Controller controller) {
        this.controller = controller;
        this.rows = controller.getRows();
        this.columns = controller.getColumns();
        this.nodeSize = controller.getNodeSize();
        
        nodes = new Node[rows][columns];
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                nodes[row][column] = new Node(row, column, controller);
            }
        }
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {             
                nodes[row][column].addNeighbour(get(row + 1, column));
                nodes[row][column].addNeighbour(get(row, column + 1));
                nodes[row][column].addNeighbour(get(row - 1, column));
                nodes[row][column].addNeighbour(get(row, column - 1));
                //
                
                nodes[row][column].addNeighbour(get(row + 1, column + 1));
                nodes[row][column].addNeighbour(get(row - 1, column - 1));
                nodes[row][column].addNeighbour(get(row + 1, column - 1));
                nodes[row][column].addNeighbour(get(row - 1, column + 1));
                
            }
        }
    }
    
    public void render(Graphics2D g) {
        
        g.setColor(Color.red);
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                for(Node neighbour : nodes[row][column].getNeighbours()) {
                    int x1 = nodes[row][column].getX() + nodeSize/2;
                    int y1 = nodes[row][column].getY() + nodeSize/2;
                    int x2 = neighbour.getX() + nodeSize/2;
                    int y2 = neighbour.getY() + nodeSize/2;
                    
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                nodes[row][column].render(g);
            }
        }
    }
    
    public boolean isValid(int row, int column) {
        return (row>=0) && (column>=0) && (row<rows) && (column<columns);
    }
    
    public Node get(int row, int column) {
        if(isValid(row, column)) return nodes[row][column];
        return null;
    }
    
    public Node getNodeExact(int x, int y) {
        int column = x/nodeSize;
        int row = y/nodeSize;
        
        if(column%2 == 0 || row%2 == 0) return null;
        
        column = (column + 1) / 2 - 1;
        row = (row + 1) / 2 - 1;
        
        return get(row, column);
    }
    
    public Node getNode(int x, int y) {
        int column = (x/nodeSize + 1) / 2 - 1;
        int row = (y/nodeSize + 1) / 2 - 1;
        
        return get(row, column);
    }
    
}
