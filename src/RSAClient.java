import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RSAClient {
	private Socket theSocket = null;
	private OutputStream os;
	private InputStream is;
	private BufferedReader reader;
	private BufferedWriter writer;

	public void close(){
		try {
			theSocket.close();
			os.close();
			is.close();
			reader.close();
			writer.close();
			System.out.println("client close");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String conn() {
		try {
			theSocket = new Socket("localhost", 1993);
			os = theSocket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));

		} catch (UnknownHostException e) {
			return "연결 실패";
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "연결 성공";
	}

	public int key() {
		String temp_str = "";
		try {
			writer.write("key" + '\n');
			writer.flush();


			temp_str = reader.readLine();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return Integer.parseInt(temp_str);
	}
	public int key2(){
		String temp_str = "";
		try{
			writer.write("key2" + '\n');
			writer.flush();
			temp_str = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return Integer.parseInt(temp_str);
	}
	//여러번 나눠서 보내줌.
	public void transfer(String str) {
		try {
			int i = 0;
			writer.write("transport"+'\n');
			writer.flush();
			for (; i < str.length() / 100; i++) {
				writer.write(str.substring(i * 100, i * 100 + 100)+'\n');
				writer.flush();
			}
			writer.write(str.substring(i * (str.length() / 100), str.length())+'\n');
			writer.flush();
			writer.write("end"+'\n');
			writer.flush();
		} catch (IOException e) {
		}
	}
	public void transfer_file(File file,int []arr,int key_e,int N){
		try {
			/*
			 file \
			 
			 key_p
			 filecheck
			 filename
			 length
			 */
			String trans_str="";
			trans_str+=file.getName();
			trans_str+="\n";
//			byte[] bt;
//			bt = getBytesFromFile(file);
//			int[] int_bt = new int[bt.length];
//			for(int i=0;i<bt.length;i++){
//				int_bt[i]=bt[i];
//			}
			
			trans_str+=String.valueOf(arr.length);
			trans_str+="\n";
//			
//			Encoding byEnc = new Encoding(key_e,N);
//			int []temp_byenc = byEnc.encode_byte(int_bt);
			for(int a:arr){
				trans_str+=String.valueOf(a);
				trans_str+="\n";
			}
			writer.write("trans_file"+'\n');
			writer.flush();
			writer.write(trans_str+'\n');
			writer.flush();
			writer.write("end"+'\n');
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void transfer_file(String str) {
		try {
			int i = 0;
			writer.write("transport_file"+'\n');
			writer.flush();
			for (; i < str.length() / 100; i++) {
				writer.write(str.substring(i * 100, i * 100 + 100)+'\n');
				writer.flush();
			}
			writer.write(str.substring(i * (str.length() / 100), str.length())+'\n');
			writer.flush();
			writer.write("end"+'\n');
			writer.flush();
		} catch (IOException e) {
		}
	}
	public byte[] getBytesFromFile(File file) throws IOException {
	     InputStream is = new FileInputStream(file);

	     long length = file.length();

	     // You cannot create an array using a long type.
	     // It needs to be an int type.
	     // Before converting to an int type, check
	     // to ensure that file is not larger than Integer.MAX_VALUE.
	     if (length > Integer.MAX_VALUE) {
	         // File is too large
	     }

	     // Create the byte array to hold the data
	     byte[] bytes = new byte[(int)length];

	     // Read in the bytes
	     int offset = 0;
	     int numRead = 0;
	     while (offset < bytes.length
	            && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	         offset += numRead;
	     }

	     // Ensure all the bytes have been read in
	     if (offset < bytes.length) {
	         throw new IOException("Could not completely read file "+file.getName());
	     }

	     // Close the input stream and return bytes
	     is.close();
	     return bytes;
	 }
}