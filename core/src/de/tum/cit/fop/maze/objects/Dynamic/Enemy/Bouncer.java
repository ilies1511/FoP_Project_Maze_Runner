package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.Player;

import java.util.*;

public class Bouncer extends Enemy {

    private static final float MOVE_SPEED = 1.5f;

    public Bouncer(Vector2 startPos) {
        super(startPos, AnimationType.BOUNCER_DOWN);
        this.speed = MOVE_SPEED;
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        List<Vector2> path = findPath(this.position, playerPosition, gameField);

        if (path != null && !path.isEmpty() && !player.isInvis()) {
            Vector2 target = path.get(0);

            // calculate
            Vector2 direction = new Vector2(target.x + 0.5f, target.y + 0.5f).sub(this.position.x + 0.5f/2f, this.position.y + 0.5f/2f);

            if (direction.len() > 0.1f) {
                direction.nor();
                this.velocity = direction.scl(this.speed);
            } else {
                this.velocity.set(0, 0);
            }
        } else {
            this.velocity.set(0, 0);
        }

        updateAnimationDirection();
    }

    private void updateAnimationDirection() {
        if (velocity.len2() < 0.1f) return;

        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) changeAnimation(AnimationType.BOUNCER_RIGHT);
            else changeAnimation(AnimationType.BOUNCER_LEFT);
        } else {
            if (velocity.y > 0) changeAnimation(AnimationType.BOUNCER_UP);
            else changeAnimation(AnimationType.BOUNCER_DOWN);
        }
    }

    // A*
    private List<Vector2> findPath(Vector2 start, Vector2 end, GameField map) {
        int startX = (int) start.x;
        int startY = (int) start.y;
        int endX = (int) end.x;
        int endY = (int) end.y;

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Set<String> closedSet = new HashSet<>();

        // nodes
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY, null, 0, heuristic(startX, startY, endX, endY));
        openSet.add(startNode);
        allNodes.put(key(startX, startY), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            closedSet.add(key(current.x, current.y));

            // target?
            if (current.x == endX && current.y == endY) {
                return retracePath(current);
            }

            // neighbors
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    if (xOffset == 0 && yOffset == 0) continue;

                    int neighborX = current.x + xOffset;
                    int neighborY = current.y + yOffset;

                    // collision
                    if (neighborX < 0 || neighborX >= map.getWidth() || neighborY < 0 || neighborY >= map.getHeight()) continue;
                    if (map.getTile(neighborX, neighborY) == TileType.WALL) continue;
                    if (closedSet.contains(key(neighborX, neighborY))) continue;

                    // costs
                    double moveCost = (Math.abs(xOffset) + Math.abs(yOffset) == 2) ? 1.414 : 1.0;
                    double newGCost = current.gCost + moveCost;

                    Node neighbor = allNodes.getOrDefault(key(neighborX, neighborY), new Node(neighborX, neighborY));

                    if (newGCost < neighbor.gCost || !openSet.contains(neighbor)) {
                        neighbor.gCost = newGCost;
                        neighbor.hCost = heuristic(neighborX, neighborY, endX, endY);
                        neighbor.fCost = neighbor.gCost + neighbor.hCost;
                        neighbor.parent = current;

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                            allNodes.put(key(neighborX, neighborY), neighbor);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(); // error
    }

    private List<Vector2> retracePath(Node endNode) {
        List<Vector2> path = new ArrayList<>();
        Node current = endNode;
        while (current.parent != null) {
            path.add(new Vector2(current.x, current.y));
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private double heuristic(int x1, int y1, int x2, int y2) {
        // diagonal movement
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private String key(int x, int y) {
        return x + "," + y;
    }

    // Helper class for A* nodes
    private static class Node {
        int x, y;
        Node parent;
        double gCost; double hCost; double fCost;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.gCost = Double.MAX_VALUE;
        }

        public Node(int x, int y, Node parent, double gCost, double hCost) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }
}