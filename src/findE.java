public class findE {
	private int pa;
	findE(int a){
		this.pa=a;
	}
	public int find(int rand){
		int return_val=rand;
		int a=pa,b=rand;
		int r,q;
		while(true){
			a=pa;
			b=return_val;
			while(true){
				q = a / b;
				r = a % b;
				if(r==0)
					break;
				a=b;
				b=r;
			}
			if(b==1)
				return return_val;
			else{
				return_val++;
			}
		}
	}
	public int find(int w,int p){
		int a=w,b=p;
		int r,q;
		while(true){
			q=a/b;
			r=a%b;
			if(r==0)
				break;
			a=b;
			b=r;
		}
		if(b==1);
		else;
		return b;
	}
}
