package run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> _ids = new ArrayList<Integer>();
        ArrayList<Integer> _parents = new ArrayList<Integer>();
        ArrayList<Integer> _costs = new ArrayList<Integer>();

        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while(line != null && !line.isEmpty()) {
            if(line.trim().equals("0")) break;
            String []values = line.trim().split(" ");
            if(values.length != 3) {
                break;
            }
            _ids.add(Integer.parseInt(values[0]));
            _parents.add(Integer.parseInt(values[1]));
            _costs.add(Integer.parseInt(values[2]));
            line = in.nextLine();
        }
        
        in.close();
        
        int res = resolve(_ids, _parents, _costs);

        System.out.println(String.valueOf(res));
    }

         // write your code here
    public static int resolve(ArrayList<Integer> ids, ArrayList<Integer> parents, ArrayList<Integer> costs) {
    	HashMap<Integer, Task> leafs = new HashMap<>();
    	HashMap<Integer, Task> roots = new HashMap<>();
    	for (int i = 0; i < ids.size(); i++) {
    		if (parents.get(i) != 0) {
    			leafs.put(ids.get(i), new Task(ids.get(i), parents.get(i), costs.get(i)));
    		}
    		else {
    			if (!parents.contains(ids.get(i))) {
    				leafs.put(ids.get(i), new Task(ids.get(i), parents.get(i), costs.get(i)));
    			}
    			else {
    				roots.put(ids.get(i), new Task(ids.get(i), parents.get(i), costs.get(i)));
    			}
    		}
    	}
    	int max = 0;
    	for (Entry<Integer, Task> task : leafs.entrySet()) {
    		int sum = 0;
    		Task temp = task.getValue();
    		while (temp.getParent() != 0) {
    			sum += temp.getCost();
    			if (leafs.get(temp.getParent()) == null) {
    				temp = roots.get(temp.getParent());
    			}
    			else {
        			temp = leafs.get(temp.getParent());
    			}
    		}
    		
    		sum += temp.getCost();
    		
    		if (sum > max) {
    			max = sum;
    		}
    	}
        return max;
    }
    
    public static class Task {
    	private int id;
    	
    	private int parent;
    	
    	private int cost;
    	
		public Task(int id, int parent, int cost) {
			this.id = id;
			this.parent = parent;
			this.cost = cost;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getParent() {
			return parent;
		}

		public void setParent(int parent) {
			this.parent = parent;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}
    }
}
