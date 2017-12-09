import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RSAServer {
	private ServerSocket theServer;
	private Socket theSocket = null;
	private InputStream is;
	private OutputStream os;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String theLine, data;
	private int key_d,key_p, key_q;
	private int piN;
	private String output_string="";
	public void close(){
		try {
			System.out.println("server close");
			writer.close();
			reader.close();
			is.close();
			os.close();
			theSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String init(int p, int q) {
		try {
			key_p = p;
			key_q = q;
			piN = (p-1)*(q-1);
			theServer = new ServerSocket(1993);

			System.out.println("서버 시작.");
			theSocket = theServer.accept();
			System.out.println("서버 연결 됨.");
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			
			
			os = theSocket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));

			String message;
			while ((message = reader.readLine()) != null) {
				if(message.compareTo("key2")==0){
					writer.write(String.valueOf(key_p*key_q)+'\n');
					writer.flush();
				}
				if (message.compareTo("key") == 0) {
					findE FE = new findE(piN);
					int temp_i,random=(int)((Math.random()*(piN-1000))+100);
					temp_i=FE.find(random);
					writer.write(String.valueOf(temp_i)+'\n');
					
					
					//d생성
					Key keyclass = new Key(temp_i,piN);
					key_d = keyclass.inverse();
					output_string+=String.valueOf(key_d)+"\n";
					writer.flush();
				}
				if(message.compareTo("transport")==0){
					String a;
					output_string+="0\n";
					while((a=reader.readLine())!=null && a.compareTo("end")!=0){
						output_string+=a;
					}
					return output_string;
				}
				if(message.compareTo("transport_file")==0){
					String a;
					output_string+="1\n";
					while((a=reader.readLine())!=null && a.compareTo("end")!=0){
						output_string+=a;
					}
					return output_string;
				}
				if(message.compareTo("trans_file")==0){
					String a;
					output_string+="1\n";
					while((a=reader.readLine())!=null && a.compareTo("end")!=0){
						output_string+=a;
						output_string+="\n";
					}
					//System.out.println(output_string);
					return output_string;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "a";
	}

	public void data(boolean a) {
		if (a) {

		} else {

		}
	}
}
