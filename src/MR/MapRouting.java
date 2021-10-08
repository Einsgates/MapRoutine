package MR;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Scanner;

class point2D {
	private double x;
	private double y;

	public point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distanceTo(point2D that) {
		double dx = this.x - that.x;
		double dy = this.y - that.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
}

class DijkstraUndirectedSP {
	private double[] distTo;
	private Edge[] edgeTo;
	private indexPQ<Double> pq;
	private EdgeWeightedGraph mGraph;
	private point2D[] point2DS;
	private int from;
	private int to;

	public DijkstraUndirectedSP(EdgeWeightedGraph G, point2D[] point) {
		point2DS = point;
		mGraph = G;
		for (Edge e : G.edges()) {
			if (e.weight() < 0)
				throw new IllegalArgumentException("Weights cannot be a negative number!");
		}
		distTo = new double[G.V()];
		edgeTo = new Edge[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		pq = new indexPQ<>(G.V());
	}

	public void setFrom(int from) {
		initDijkstra();
		this.from = from;
		distTo[from] = 0.0;
		pq.insert(from, distTo[from]);
	}

	public void setTo(int to) {
		this.to = to;
	}

	private void relax(Edge e, int v) {
		int w = e.other(v);
		double weight = distTo[v] + e.weight();
		// 1: A* Algorithm
		// Update d[w] to d[v] + dist(v,w) + dist(w,d) - dist(v,d)
//		double weight = distTo[v] + e.weight() + point2DS[v].distanceTo(point2DS[to]) - point2DS[w].distanceTo(point2DS[to]) ;
		if (distTo[w] > weight) {
			distTo[w] = weight;
			edgeTo[w] = e;
			if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
			else pq.insert(w, distTo[w]);
		}
	}

	public double distTo(int v) {
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		while (!pq.isEmpty()) {
			int x = pq.delMin();
			// 2: When target node is found, quit the loop 
			if (x == v) {
				return true;
			}
			for (Edge e : mGraph.adj(x))
				relax(e, x);
		}
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	public void initDijkstra() {
		for (int i = 0; i < mGraph.V(); i++) {
			if (pq.contains(i)) {
				pq.delete(i);
			}
			if (edgeTo[i] != null) {
				edgeTo[i] = null;
			}
			if (!Double.isInfinite(distTo(i))) {
				distTo[i] = Double.POSITIVE_INFINITY;
			}
		}
	}

	public Iterable<Edge> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		Stack<Edge> path = new Stack<Edge>();
		int x = v;
		for (Edge e = edgeTo[v]; e != null; e = edgeTo[x]) {
			path.push(e);
			x = e.other(x);
		}
		return path;
	}
}

public class MapRouting {
	public static void init_Point(In in, point2D[] p) {
		for (int i = 0; i < p.length; i++) {
			int q = in.readInt();
			int x = in.readInt();
			int y = in.readInt();
			p[i] = new point2D(x, y);
		}

	}

	public static void init_Graph(In in, int b, EdgeWeightedGraph e, point2D[] a) {
		for (int i = 0; i < b; i++) {
			int q = in.readInt();
			int l = in.readInt();
			double weight = a[q].distanceTo(a[l]);
			e.addEdge(new Edge(q, l, weight));
		}

	}

	public static void findRoute(EdgeWeightedGraph mDigraph, point2D[] mpoint) {
		Scanner in = new Scanner(System.in);

		DijkstraUndirectedSP msp = new DijkstraUndirectedSP(mDigraph, mpoint);

		while (true) {
			int from = in.nextInt();
			int to = in.nextInt();
			msp.setFrom(from);
			msp.setTo(to);
			boolean boo = msp.hasPathTo(to);
			if (boo) {
				StdOut.println("The shortest path is: ");
				for (Edge e : msp.pathTo(to)) {
					StdOut.println(e + " ");
				}
				StdOut.println(from + " --> " + to + "the shortest path is ：" + msp.distTo(to));
				StdOut.println();
			} else {
				StdOut.println("No path exists");
			}
		}
	}

	public static void main(String[] args) {
		In in = new In("D:\\Java\\coding\\Arithemtic\\Exp\\exp3\\Data.txt");

		int a = in.readInt();
		int b = in.readInt();

		EdgeWeightedGraph mDigraph = new EdgeWeightedGraph(a);
		point2D[] mPoints = new point2D[a];
		init_Point(in, mPoints);
		init_Graph(in, b, mDigraph, mPoints);

		findRoute(mDigraph, mPoints);
	}
}
