package com.demo.lib.sort.heapsort;

public class HeapSortor {
	
	public static void sort(int []arr){
		if(arr.length ==1) {
			return;
		}
		
		if(arr.length ==2) {
	        if(arr[0] > arr[1]) {
	        	swap(arr,0,1);
	        }
			return;
		}
		
        //1.build heap net
        for(int i= lastNodeWithChild(arr); i>=0 ;i--){
        	//from the last node with child at bottom to up
        	//adjust from right to left
            adjustHeap(arr,i,arr.length);
        }
        
        //2.swap top with the last one, then re-adjust
        for(int j=arr.length-1;j>1;j--){
            swap(arr,0,j);//swap top with the last one
            adjustHeap(arr,0,j);//re-adjust without the last one
        }
        
        if(arr[0] > arr[1]) {
        	swap(arr,0,1);
        }
    }
	
	private static int lastNodeWithChild(int []arr) {
		return arr.length/2-1;
	}
	
	/**
	 * adjust the array to be a heap node net 
	 * (heap node - binary node and its value must be greater then both of children)
	 * @param arr - data array
	 * @param nodeIndex - array index of the node
	 * @param length - length of array to be adjusted
	 */
	public static void adjustHeap(int []arr, int nodeIndex, int length){
        int temp = arr[nodeIndex]; //store node object to a temp variable
        
        // start from left child of the node, which is 2 * nodeIndex + 1
        for(int k=nodeIndex*2+1; k<length; k=k*2+1) {
        	//if left child is less than right child, point to right child
            if(k+1<length && arr[k]<arr[k+1]){
                k++;
            }
            
            if(arr[k] >temp) {
            	//if child node is greater than node, assign child to node
            	//don't need swap, node will be assigned to child later
                arr[nodeIndex] = arr[k];
                
                //nodeIndex points to one of child node
                //If child node has children, continue to move
                nodeIndex = k;
            }else{
                break;
            }
        }
        arr[nodeIndex] = temp;//assign node to child if nodeIndex is changed. Or nothing change
    }
	/**
	 * swap value between a and b
	 * @param arr
	 * @param a
	 * @param b
	 */
	public static void swap(int []arr,int a ,int b) {
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }	
}
