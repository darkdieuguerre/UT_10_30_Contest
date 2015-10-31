import java.io.*;
import java.util.*;
public class ACV {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(br.readLine());
		while(cases-- > 0) {
			int n = Integer.parseInt(br.readLine());
			double ret = 0;
			while(n-- > 0) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				st.nextToken();
				if(st.nextToken().equals("Monthly")) {
					ret += 9.99 * 12;
				}
				else {
					ret += 99;
				}
			}
			System.out.printf("%.2f\n", ret);
		}
	}
}
