public class Key {
	private int a,b;
	Key(int a,int b){
		this.a=a;
		this.b=b;
	}
	public int inverse(){
		int r1,r2,q,r,t,t1,t2;
		r1=a;
		r2=b;
		t1=0;t2=1;
		while(r1!=1){
			q=r2/r1;
			r=r2-r1*q;
			t=t1-t2*q;
			r2=r1;
			r1=r;
			t1=t2;
			t2=t;
		}
		if(t2<0)
			t2=t2+b;
		return t2;
	}
}
