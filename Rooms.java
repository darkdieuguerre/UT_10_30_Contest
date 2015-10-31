import java.io.*;
import java.util.*;
public class Rooms {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			boolean[][] grid = new boolean[n][m];
			for(int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				int k = Integer.parseInt(st.nextToken());
				while(k-- > 0) {
					grid[i][Integer.parseInt(st.nextToken())-1] = true;
				}
			}
			System.out.println(match(grid, new int[n], new int[m]));
		}
	}
	public static boolean match(int i, boolean[][] w, int[] mr, int[] mc, boolean[] seen) {
		for (int j = 0; j < w[i].length; j++) {
			if (w[i][j] && !seen[j]) {
				seen[j] = true;
				if (mc[j] < 0) {
					mr[i] = j;
					mc[j] = i;
					return true;
				}
				seen[j] = false;
			}
		}
		for (int j = 0; j < w[i].length; j++) {
			if (w[i][j] && !seen[j]) {
				seen[j] = true;
				if (match(mc[j], w, mr, mc, seen)) {
					mr[i] = j;
					mc[j] = i;
					return true;
				}
			}
		}
		return false;
	}
	public static int match(boolean[][] w, int[] mr, int[] mc) {
		Arrays.fill(mr, -1);
		Arrays.fill(mc, -1);
		int ct = 0;
		for (int i = 0; i < w.length; i++) {
			boolean[] seen = new boolean[w[0].length];
			if(match(i, w, mr, mc, seen))
				ct++;
		}
		return ct;
	}

}
