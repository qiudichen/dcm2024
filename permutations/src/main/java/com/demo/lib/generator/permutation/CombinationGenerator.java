package com.demo.lib.generator.permutation;

public class CombinationGenerator {
	/*  Forward select
	 *  start from i (m<=i<=n) as last item
	 *  then from 1 to i-1, select m-1
	 *  
	 *  For example, select 3 from 1 2 3 4 5
	 *  1 - select 3, then only 1 2, -> 1,2,3
	 *  2 - select 4, -> select 2 from 1,2,3 
	 *  2.1 - select 2, -> 1,2,4
	 *  2.2 - select 3 -> select 1 from 1,2
	 *        -> 1,3,4
	 *        -> 2,3,4
	 *  3 - select 5, -> select 2 from 1,2,3,4
	 *  3.1 - select 2 -> 1,2,5
	 *  3.2 - select 3 -> select 1 from 1,2
	 *        -> 1,3,5
	 *        -> 2,3,5
	 *  3.3 - select 4 -> select 1 from 1,2,3
	 *        -> 1,4,5
	 *        -> 2,4,5
	 *        -> 3,4,5
	 */
	private void combinate(int n, int m, int[] a, int[] b) {
		for(int i = m; i <= n; i++) {
			b[m-1] = i - 1;
			if(m > 1) {
				combinate(i-1, m-1, a, b);
				
			} else {
				//get result
				for(int j = 0; j < b.length; j++) {
					System.out.printf("%d", a[b[j]]);
				}
				System.out.println();
			}
		}
	}
	
	public void generate(int[] a, int m) {
		combinate(a.length, m, a, new int[m]);
	}
	
	public static void main(String[] args) {
		CombinationGenerator generator = new CombinationGenerator();
		int[] a = new int[] {1,2,3,4,5,6,7,8,9};
		
		//generator.generate(a, 2);
		generator.generate(a, 3);
		generator.generate(a, 4);
		
	}
}
