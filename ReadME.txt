This project needs json-simple-1.1.jar

Function explanation:
	Read - The function traverses through each objects of Json Arrray in input files and put the same objects in the result array.
	Write - If( resultArray < max_size ){
			write resultArray to one file;
		}else{
			split resultArray into sub arrays so that it's size does not exceed max_size and then write that subarrays to different output files accordingly.
		}

Time Complexity : 
	O(n) where n = number of total objects in input files.
	There may be nested loops but every loop iterates on only one parameter which is total number of objects in input files.
	So time complexity is O(n);

