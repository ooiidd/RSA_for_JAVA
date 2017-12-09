import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MainFrame_S extends JFrame {
	private static int key_p = 31;
	private static int key_q = 37;
	private FileOutputStream fout;
	private BufferedWriter fwriter;
	private JPanel contentPane;
	private JScrollPane transportScroll;
	private JScrollPane receiveScroll;

	private static JTextArea receiveArea;
	private JTextArea transportArea;

	private JLabel receiveLabel;
	private JLabel transportLabel;
	private JButton encodingBtn;
	private int key_e, key_d, N = key_p * key_q;

	private static RSAServer server;
	public static String output = "";

	public static void main(String[] args) {
		// test

		MainFrame_S frame = new MainFrame_S();
		frame.setVisible(true);

		server = new RSAServer();
		output = server.init(key_p, key_q);
		System.out.println("server main" + output);
		screen();
	}

	public static void screen() {
		String[] val = output.split("\n");
		int check = Integer.parseInt(val[1]);

		if (check == 1) {
			int file_length = Integer.parseInt(val[3]);
			int[] int_arr = new int[file_length];

			String screen_str = "";
			for (int i = 0; i < file_length; i++) {
				screen_str += val[i + 4];
			}
			receiveArea.setText(screen_str);
		}else{
			receiveArea.setText(val[2]);
		}
	}

	public MainFrame_S() {
		super("서버");
		int piN = (key_p - 1) * (key_q - 1);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		transportLabel = new JLabel("받은 메시지");
		transportLabel.setBounds(230, 20, 170, 40);
		getContentPane().add(transportLabel);

		receiveArea = new JTextArea();
		receiveArea.setEditable(false);
		receiveScroll = new JScrollPane(receiveArea);
		receiveScroll.setBounds(120, 62, 270, 280);
		getContentPane().add(receiveScroll);

		transportLabel = new JLabel("복호화 메시지 창");
		transportLabel.setBounds(500, 20, 170, 40);
		getContentPane().add(transportLabel);

		transportArea = new JTextArea();
		transportArea.setEditable(false);
		transportScroll = new JScrollPane(transportArea);
		transportScroll.setBounds(400, 62, 270, 280);
		getContentPane().add(transportScroll);

		encodingBtn = new JButton("복호화");
		encodingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] val = output.split("\n");
				key_d = Integer.parseInt(val[0]);
				int check = Integer.parseInt(val[1]);

				if (check == 1) {
					try {
						// 여긴 왔음.
						String filename = val[2];
						int file_length = Integer.parseInt(val[3]);
						int[] int_arr = new int[file_length];
						for (int i = 0; i < file_length; i++) {
							int_arr[i] = Integer.parseInt(val[i + 4]);
						}
						Encoding byDec = new Encoding(key_d, N);
						int[] int_out = byDec.encode_byte(int_arr);

						byte bt_arr[] = new byte[file_length];
						for (int i = 0; i < file_length; i++) {
							bt_arr[i] = (byte) int_out[i];
						}
						FileOutputStream fileout = new FileOutputStream(filename);
						fileout.write(bt_arr);

						fileout.close();
						// fout = new FileOutputStream("file.txt");
						// fwriter = new BufferedWriter(new
						// OutputStreamWriter(fout));
						// fwriter.write(p);
						// fwriter.flush();
						JOptionPane.showMessageDialog(null, "저장되었습니다.");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {

					Encoding enc = new Encoding(val[2], key_d, N);
					String p;
					p = enc.encode();
					transportArea.setText(p);
				}
				server.close();
			}
		});
		encodingBtn.setBounds(10, 160, 100, 40);
		contentPane.add(encodingBtn);

	}
}
