// N e 받아서 계싼 (공개키) 클라이언트 클래스임.
public class Encoding {
	private String str;
	private int key_e;
	private int N;
	Encoding(String str,int key_e,int N){
		this.str = str;
		this.key_e = key_e;
		this.N = N;
		
	}
	Encoding(int key_e,int N){
		this.key_e = key_e;
		this.N=N;
	}
	public String encoding(){
		String temp_str="";
		char temp_char;
		for(int i=0;i<str.length();i++){
			temp_char=str.charAt(i);
			System.out.println(temp_char);
			temp_char=(char)(temp_char+key_e);
			temp_str+=temp_char;
		}
		return temp_str;
	}
	public String encode(){
		String temp_str = Integer.toBinaryString(key_e);
		temp_str = reverseString(temp_str);
		
		String return_str="";
		
		//스트링 하나하나 마다 처리
		for(int i=0;i<str.length();i++){
			int r=1;
			int m=0;
			char temp_char=str.charAt(i);
			int y=temp_char;
//			int y = str.charAt(i);
			System.out.println(y);
			for(int t=0;t<temp_str.length();t++){
				if(temp_str.charAt(m) == '1'){
					r=((r*y)%N);
				}
				y=(y*y)%N;
				m++;
			}
			y=str.charAt(i);
//			System.out.println(y);
			System.out.println("r: "+r);
			char temp_char2;
			temp_char2=(char) r;
			return_str+=temp_char2;
			
			
		}
		return return_str;
	}
	public int[] encode_byte(int[] bt){
		String temp_str = Integer.toBinaryString(key_e);
		temp_str = reverseString(temp_str);
		
	
		int length = bt.length;
		int []return_bt = new int[length];
		
		for(int i=0;i<length;i++){
			int r=1;
			int m=0;
			
			int temp_int = bt[i];
			int y=temp_int;
			for(int t=0;t<temp_str.length();t++){
				if(temp_str.charAt(m)=='1'){
					r=((r*y)%N);
				}
				y=(y*y)%N;
				m++;
			}
			return_bt[i]=r;
		}
		return return_bt;
	}
	public static String reverseString(String s) {
		return (new StringBuffer(s)).reverse().toString();
	}
}
