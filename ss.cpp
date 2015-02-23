#include"stdio.h"
void main()
{
	int A[20]={1, 2, -1, -2, 2, 3, -2, 3, -1, 3, 2, 2, -3, 2, 3, 2, 2, 0, 1, -1};
    int N=20;
    int max, that;
	int i;
    max=0;
	that=0;
	for (i=0; i<N; i++)
	{
	   that+=A[i];
	   if ( that>max)
		   max=that;
       else if (that<0)
           that=0;	       
	}

	printf("%d",max);




}