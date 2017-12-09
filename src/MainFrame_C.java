import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MainFrame_C extends JFrame {
	private JPanel contentPane;
	private JTextArea inputtextArea;
	private JScrollPane inputtextScroll;
	private JScrollPane transportScroll;
	private JTextArea transportArea;

	private int file_length;
	private File selectedFile = null;
	private boolean server_check = false;
	private boolean key_check = false;
	private boolean file_check=false, pass_check = false;

	private int [] arr;
	private JLabel keyLabel;
	private JLabel keyvalLabel;
	private JLabel transportLabel;
	private JLabel inputtextLabel;
	private JFileChooser chooser;
	private JButton keyrequestBtn;
	private JButton filechooseBtn;
	private JButton serverBtn;
	private JButton encodingBtn;
	private JButton transportBtn;
	private int key_e, N;

	private RSAClient client = null;

	public static void main(String[] args) {
		// test

		String a = "호엉";
		int p = a.charAt(0);
		char kk = (char) p;

		MainFrame_C frame = new MainFrame_C();
		frame.setVisible(true);
	}

	public MainFrame_C() {
		super("클라이언트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		keyLabel = new JLabel("키 값   : ");
		keyLabel.setBounds(10, 10, 50, 40);
		getContentPane().add(keyLabel);

		keyvalLabel = new JLabel("");
		keyvalLabel.setBounds(60, 10, 140, 40);
		getContentPane().add(keyvalLabel);

		inputtextLabel = new JLabel("입력창");
		inputtextLabel.setBounds(240, 20, 70, 40);
		getContentPane().add(inputtextLabel);

		transportLabel = new JLabel("암호화창");
		transportLabel.setBounds(510, 20, 70, 40);
		getContentPane().add(transportLabel);

		inputtextArea = new JTextArea();
		inputtextScroll = new JScrollPane(inputtextArea);
		inputtextScroll.setBounds(120, 62, 270, 280);
		getContentPane().add(inputtextScroll);

		transportArea = new JTextArea();
		transportArea.setEditable(false);
		transportScroll = new JScrollPane(transportArea);
		transportScroll.setBounds(400, 62, 270, 280);
		getContentPane().add(transportScroll);

		serverBtn = new JButton("서버연결");
		serverBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server_check = true;

				client = new RSAClient();
				String co = client.conn();
				if (co.compareTo("연결 성공") == 0) {
					JOptionPane.showMessageDialog(null, "연결 되었습니다.");
				} else {
					JOptionPane.showMessageDialog(null, "연결 실패!");
				}
			}
		});
		serverBtn.setBounds(10, 60, 100, 40);
		contentPane.add(serverBtn);

		encodingBtn = new JButton("암호화");
		encodingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (inputtextArea.getText().length() == 0 && !file_check) {
					JOptionPane.showMessageDialog(null, "입력해야합니다");
				} else if (!key_check) {
					JOptionPane.showMessageDialog(null, "키가 없습니다.");
				} else {
					pass_check = true;
					if (file_check) {
						byte[] bt;
						try {
							bt = getBytesFromFile(selectedFile);
							int []temp_arr = new int[bt.length];
							for(int i=0;i<bt.length;i++){
								temp_arr[i]=bt[i];
							}
							Encoding byEnc = new Encoding(key_e,N);
							arr = new int[bt.length];
							arr = byEnc.encode_byte(temp_arr);
							String all="";
							for(int a:arr){
								all+=a;
							}
							transportArea.setText(all);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					} else {
						// 리스너
						String temp;
						temp = inputtextArea.getText();
						Encoding enc = new Encoding(temp, key_e, N);
						temp = enc.encode();
						transportArea.setText(temp);
					}
				}

			}
		});
		encodingBtn.setBounds(10, 160, 100, 40);
		contentPane.add(encodingBtn);

		transportBtn = new JButton("전송");
		transportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pass_check) {
					// 리스너
					if (file_check) {
						client.transfer_file(selectedFile,arr, key_e, N);
						// client.transfer_file(transportArea.getText());
					} else {
						client.transfer(transportArea.getText());
					}
					JOptionPane.showMessageDialog(null, "전송 성공.");

					server_check = false;
					key_check = false;
					file_check = false;
					pass_check = false;
					//inputtextArea.setText("");
					//transportArea.setText("");
					//keyLabel.setText("");
					client.close();

				} else {
					JOptionPane.showMessageDialog(null, "암호화 해 주세요.");
				}
			}
		});
		transportBtn.setBounds(10, 260, 100, 40);
		contentPane.add(transportBtn);

		keyrequestBtn = new JButton("키 요청");
		keyrequestBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (server_check) {
					key_check = true;
					key_e = client.key();
					N = client.key2();
					keyvalLabel.setText(String.valueOf(key_e) + ":" + String.valueOf(N));

				} else {
					JOptionPane.showMessageDialog(null, "서버와 연결되어있지 않습니다.");
				}
			}
		});
		keyrequestBtn.setBounds(10, 110, 100, 40);
		contentPane.add(keyrequestBtn);

		filechooseBtn = new JButton("파일선택");
		filechooseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String output = "";
				String temp_str;
				chooser = new JFileChooser();
				// FileNameExtensionFilter filter = new
				// FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				// chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(MainFrame_C.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "선택되었습니다.");
				}
				selectedFile = chooser.getSelectedFile();
				try {
					if (selectedFile.isFile()) {
						file_check = true;
						String fileName = selectedFile.getName();
						// InputStreamReader in;
						// FileInputStream fis;
						// BufferedReader br;
						// try {
						//// fis = new FileInputStream(selectedFile);
						//// in = new InputStreamReader(fis, "utf-8");
						//// br = new BufferedReader(in);
						//// while ((temp_str = br.readLine()) != null) {
						//// output += temp_str;
						//// output += "\r\n";
						//// }
						// inputtextArea.setText(output);
						// } catch (FileNotFoundException e1) {
						// // TODO Auto-generated catch block
						// e1.printStackTrace();
						// } catch (UnsupportedEncodingException e1) {
						// // TODO Auto-generated catch block
						// e1.printStackTrace();
						// } catch (IOException e1) {
						// // TODO Auto-generated catch block
						// e1.printStackTrace();
						// }
					} else {
						JOptionPane.showMessageDialog(null, "파일이 아님니다.");
					}
				} catch (NullPointerException ne) {
				}

			}
		});
		filechooseBtn.setBounds(10, 210, 100, 40);
		contentPane.add(filechooseBtn);

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
